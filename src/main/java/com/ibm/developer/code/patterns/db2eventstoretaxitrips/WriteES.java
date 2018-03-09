package com.ibm.developer.code.patterns.db2eventstoretaxitrips;

//
// Read a Taxi records file and write to Event Store
//
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

import java.lang.String;
import java.lang.StringBuffer;
import java.lang.Thread;
import java.lang.NumberFormatException;
import java.lang.InterruptedException;

import java.sql.Timestamp;

import scala.Option;

import org.codehaus.jackson.map.ObjectMapper;

import com.ibm.event.common.ConfigurationReader;
import com.ibm.event.oltp.EventContext;
import com.ibm.event.oltp.EventError;
import com.ibm.event.catalog.ResolvedTableSchema;
import com.ibm.event.oltp.InsertResult;

import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.Row;

public class WriteES {
    public static void main(String args[]) {
        StringBuffer buf = new StringBuffer(500);
        String strline;
        int rate = 0;
        int count;
        long recordNo, recordsInserted, startMillis, endMillis;
        long startM, stopM;
        long totalInsertTime;
        TaxiRecord record;
        InsertResult insertResult;

        if (args.length != 3) {
            System.err.println("usage: WriteES <IP address> <rate> <file>");
            System.exit(1);
        }
        try {
            rate = Integer.parseInt(args[1]);
        } catch(NumberFormatException e1) {
            System.err.println("Number format exception on: " + args[1]);
            System.exit(1);
        }
        if (rate <= 0) {
            System.err.println("The rate cannot be smaller or equal to zero");
            System.exit(1);
        }
        System.out.println("Rate: " + rate + " records/second");

        // The IP changes each time we restart Event Store Desktop
        // The IP is in ifconfig at utun1
        // ConfigurationReader.setConnectionEndpoints("9.80.110.74:5555");
        ConfigurationReader.setConnectionEndpoints(args[0] + ":1100");
        EventContext eventContext = EventContext.getEventContext("TESTDB");
        Option<EventError> result = eventContext.openDatabase();

        try {
            FileInputStream fstream = new FileInputStream(args[2]);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            ResolvedTableSchema taxiTab = eventContext.getTable("TaxiTrips");
            ObjectMapper objectMapper = new ObjectMapper();

            int everyNth = 3;  // Speed this up w/ every Nth record sampling
            recordNo = 1L;
            recordsInserted = 0;
            count = 0; // count
            totalInsertTime = 0;
            startMillis = System.currentTimeMillis(); // for insert rate
            while ((strline = br.readLine()) != null)   {
               if (0 != recordNo % everyNth) {
                   recordNo++;
                   continue;
               }
               // I have a JSON doc, now process it
               record = objectMapper.readValue(strline, TaxiRecord.class);;
               Row row = cre8Row(recordNo, record);

               // Write to Event Store
               // Batch inserts are better. Use sync insert for now
               startM = System.currentTimeMillis(); // for insert rate
               insertResult = eventContext.insert(taxiTab, row);
               stopM = System.currentTimeMillis(); // for insert rate
               totalInsertTime += (stopM -startM);
               if (insertResult.failed())
                   System.out.println("insertResult: " +
                                      insertResult.toString());
               recordsInserted++;  // Keep track for when we insert
               if (0 == (recordNo) % 20) {
                   System.out.println("Number of records inserted: " +
                           recordsInserted + ", total time: " +
                           totalInsertTime + "ms");
               }
               recordNo++;
               count++;
               // If we have the rate number of records, see if we need to sleep
               if (count >= rate) {
                  count = 0;
                  endMillis = System.currentTimeMillis();
                  if (1000 > (endMillis - startMillis)) {
                     // sleep
                     try {
                         Thread.sleep(1000 - (endMillis - startMillis));
                     } catch (InterruptedException e2) {
                       return;
                     }
                  }
                  startMillis = endMillis;
               }
            }

        } catch (IOException e3) {
            System.err.println("Error: " + e3.toString());
            System.exit(1);
        }
        eventContext.cleanUp();
        return;
    }
//**************************************************************************
    static Timestamp toTimestamp(String ts) {
        if (ts == null)
           return(null);
        String ts2 = ts.replace('T', ' ');
        return(Timestamp.valueOf(ts2));
    }
//**************************************************************************
    static Row cre8Row(long recordNo, TaxiRecord record) {
        return(RowFactory.create(recordNo, record.taxi_id, record.trip_id, 
               toTimestamp(record.trip_start_timestamp),
               toTimestamp(record.trip_end_timestamp),
               record.pickup_centroid_longitude,
               record.pickup_centroid_latitude,
               record.dropoff_centroid_longitude,
               record.dropoff_centroid_latitude,
               record.trip_total, record.trip_miles,
               record.trip_seconds)
              );
    }

}
