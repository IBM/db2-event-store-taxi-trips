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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** Unit test for simple TaxiRecord. */
public class TaxiRecordTest extends TestCase {
  /**
   * Test the constructor and toString.
   *
   * @param testName name of the test case
   */
  public TaxiRecordTest(String testName) {
    super(testName);
  }

  /** @return the suite of tests being tested */
  public static Test suite() {
    return new TestSuite(TaxiRecordTest.class);
  }

  public void testTaxiRecordDefaults() {
    TaxiRecord tr = new TaxiRecord();
    assertEquals(tr.toString(), "null|null|null|null|null|null|null|null|null|null|null");
  }
}
