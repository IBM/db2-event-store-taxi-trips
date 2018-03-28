package com.ibm.developer.code.patterns.db2eventstoretaxitrips;

//
// Try connecting to Db2 Event Store
//
import com.ibm.event.common.ConfigurationReader;
import com.ibm.event.oltp.EventContext;
import com.ibm.event.oltp.EventError;
import com.ibm.event.catalog.ResolvedTableSchema;
import com.ibm.event.oltp.InsertResult;
import com.ibm.event.catalog.ResolvedTableSchema;

import scala.Option;
import scala.collection.JavaConversions;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.collection.Iterator;

public class TryConnect {

    public static void main(String args[]) {
        String DBName = "TESTDB";
        String TabName = "TaxiTrips";
        String user = "admin";
        String password = "password";
        String ip = null;
        ip = args[0];
        System.out.println("IP: " + ip);
        
        ConfigurationReader.setConnectionEndpoints(ip + ":1100");
        ConfigurationReader.setEventUser(user);
        ConfigurationReader.setEventPassword(password);
        EventContext eventContext = EventContext.getEventContext(DBName);
        Option<EventError> result = eventContext.openDatabase();
        System.out.println("Got a connection, try to get a table");

        ResolvedTableSchema tab = eventContext.getTable("TaxiTrips");
        System.out.println("Table:\n" + tab.toString());
        System.exit(0);
    }
}
