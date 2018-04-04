/**
 * Copyright 2018 IBM Corp. All Rights Reserved.
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibm.developer.code.patterns.db2eventstoretaxitrips;

import java.sql.Timestamp;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;

/** Unit test for EventStoreWriter. */
public class EventStoreWriterTest extends TestCase {
  /**
   * Test the constructor and toString.
   *
   * @param testName name of the test case
   */
  public EventStoreWriterTest(String testName) {
    super(testName);
  }

  /** @return the suite of tests being tested */
  public static Test suite() {
    return new TestSuite(EventStoreWriterTest.class);
  }

  public void testEventStoreWriterIPRateDefaults() {
    EventStoreWriter esw = new EventStoreWriter("s1", 1234);
    assertEquals(
        esw.toString(),
        "EventStoreWriter{ip='s1', rate=1234, file='data/Taxi50k.json', DBName='TESTDB', TabName='TaxiTrips', user='null', password='null', keepRunning=1}");
  }

  public void testEventStoreWriterArgs() {
    EventStoreWriter esw = new EventStoreWriter("ip", 100, "dbname", "tabname", "user", "password");
    assertEquals(
        esw.toString(),
        "EventStoreWriter{ip='ip', rate=100, file='data/Taxi50k.json', DBName='dbname', TabName='tabname', user='user', password='password', keepRunning=1}");
  }

  public void testToTimeStampNull() {
    Timestamp ts = EventStoreWriter.toTimestamp(null);
    assertNull(ts);
  }

  public void testToTimeStamp() {
    Timestamp ts = EventStoreWriter.toTimestamp("2018-04-03T22:32:54");
    assertEquals(Timestamp.valueOf("2018-04-03 22:32:54"), ts);
  }

  public void testCre8Row() {
    Row row = EventStoreWriter.cre8Row(999, new TaxiRecord());
    assertEquals(
        row,
        RowFactory.create(999, null, null, null, null, null, null, null, null, null, null, null));
  }
}
