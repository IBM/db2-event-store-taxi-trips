package com.ibm.developer.code.patterns.db2eventstoretaxitrips;

//
// Read a Taxi records file and write to Event Store
//

import com.ibm.event.catalog.ResolvedTableSchema;
import com.ibm.event.common.ConfigurationReader;
import com.ibm.event.oltp.EventContext;
import com.ibm.event.oltp.EventError;
import com.ibm.event.oltp.InsertResult;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.codehaus.jackson.map.ObjectMapper;
import scala.Option;
import scala.collection.JavaConversions;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

public class EventStoreWriter implements Runnable {
  public String ip = null;
  public int rate = 5;
  public String file = "data/Taxi50k.json";
  public String DBName = "TESTDB";
  public String TabName = "TaxiTrips";
  public String user = null;
  public String password = null;
  public int keepRunning = 1;

  // Set the IP to use and the number of records per second to insert
  public EventStoreWriter(String ip, int rate) {
    this.ip = ip;
    this.rate = rate;
  }

  public EventStoreWriter(
      String ip, int rate, String dbname, String tabname, String usr, String pwd) {
    this.ip = ip;
    this.rate = rate;
    this.DBName = dbname;
    this.TabName = tabname;
    this.user = usr;
    this.password = pwd;
  }

  public void run() {
    StringBuffer buf = new StringBuffer(500);
    String strline;
    int count;
    long recordNo, startMillis, endMillis, timeout;
    long startM, stopM;
    long totalInsertTime;
    TaxiRecord record;
    InsertResult insertResult;

    // System.out.println("EventStoreWriter: IP: " + this.ip);
    // System.out.println("EventStoreWriter: DB: " + this.DBName);
    // System.out.println("EventStoreWriter: Tab: " + this.TabName);
    // System.out.println("EventStoreWriter: usr: " + this.user);
    // System.out.println("EventStoreWriter: pwd: " + this.password);

    ConfigurationReader.setConnectionEndpoints(this.ip + ":1100");
    ConfigurationReader.setEventUser(this.user);
    ConfigurationReader.setEventPassword(this.password);
    EventContext eventContext = EventContext.getEventContext(this.DBName);
    Option<EventError> result = eventContext.openDatabase();
    ArrayList<Row> rows = new ArrayList<>(this.rate);

    // Start inserting in the table at the given rate
    try {
      FileInputStream fstream = new FileInputStream(this.file);
      BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
      ResolvedTableSchema taxiTab = eventContext.getTable(this.TabName);
      ObjectMapper objectMapper = new ObjectMapper();

      recordNo = 0L;
      count = 0; // count
      totalInsertTime = 0;
      startMillis = System.currentTimeMillis(); // for insert rate
      timeout = startMillis;

      //
      // TODO: Add reopen file when reaching the end?
      //
      while ((strline = br.readLine()) != null) {
        if (keepRunning != 1) {
          System.out.println("EventStoreWriter thread exiting");
          return;
        }
        // I have a JSON doc, now process it
        recordNo++;
        count++;
        record = objectMapper.readValue(strline, TaxiRecord.class);
        ;
        rows.add(cre8Row(recordNo, record));

        if (count >= rate) {
          count = 0;
          // Write to Event Store
          startM = System.currentTimeMillis(); // for insert rate
          Future<InsertResult> future =
              eventContext.batchInsertAsync(
                  taxiTab, JavaConversions.asScalaBuffer(rows).toIndexedSeq(), true);
          insertResult = Await.result(future, Duration.Inf());
          stopM = System.currentTimeMillis(); // for insert rate
          totalInsertTime += (stopM - startM);
          rows = new ArrayList<>(rate);
          if (insertResult.failed()) System.out.println("insertResult: " + insertResult.toString());
          if (0 == (recordNo) % (4 * rate))
            System.out.println(
                "Number of records inserted: "
                    + recordNo
                    + ", total time: "
                    + totalInsertTime
                    + "ms");
          endMillis = System.currentTimeMillis();
          if (1000 > (endMillis - startMillis)) {
            // sleep
            try {
              Thread.sleep(1000 - (endMillis - startMillis));
            } catch (InterruptedException e2) {
              return;
            }
            // Exit after 30 minutes
            if ((30 * 60 * 1000) < (endMillis - timeout)) return;
          }
          startMillis = endMillis;
        }
      }

    } catch (Exception e3) {
      System.out.println("Error: " + e3.toString());
      return;
    }
    eventContext.cleanUp();
    return;
  }
  // **************************************************************************
  static Timestamp toTimestamp(String ts) {
    if (ts == null) return (null);
    String ts2 = ts.replace('T', ' ');
    return (Timestamp.valueOf(ts2));
  }
  // **************************************************************************
  static Row cre8Row(long recordNo, TaxiRecord record) {
    return (RowFactory.create(
        recordNo,
        record.taxi_id,
        record.trip_id,
        toTimestamp(record.trip_start_timestamp),
        toTimestamp(record.trip_end_timestamp),
        record.pickup_centroid_longitude,
        record.pickup_centroid_latitude,
        record.dropoff_centroid_longitude,
        record.dropoff_centroid_latitude,
        record.trip_total,
        record.trip_miles,
        record.trip_seconds));
  }
}
