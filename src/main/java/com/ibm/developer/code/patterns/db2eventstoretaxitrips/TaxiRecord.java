package com.ibm.developer.code.patterns.db2eventstoretaxitrips;

//
// Class to contain Taxi record
//
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaxiRecord {
    public String taxi_id;
    public String trip_id;
    public Double pickup_centroid_longitude, pickup_centroid_latitude;
    public Double dropoff_centroid_longitude, dropoff_centroid_latitude;
    public Double trip_total;
    public Double trip_miles, trip_seconds;
    public String trip_start_timestamp, trip_end_timestamp;

    public TaxiRecord() {
        this.taxi_id = null;
        this.trip_id = null;
        this.pickup_centroid_longitude = null;
        this.pickup_centroid_latitude = null;
        this.dropoff_centroid_longitude = null;
        this.dropoff_centroid_latitude = null;
        this.trip_total = null;
        this.trip_miles = null;
        this.trip_seconds = null;
        this.trip_start_timestamp = null;
        this.trip_end_timestamp = null;
    }
    public String toString() {
        String ret = 
        taxi_id + "|" + trip_id + "|" +
        trip_start_timestamp + "|" + trip_end_timestamp + "|" +
        pickup_centroid_longitude + "|" + pickup_centroid_latitude + "|" +
        dropoff_centroid_longitude + "|" + dropoff_centroid_latitude + "|" +
        trip_seconds + "|" + trip_miles + "|" + trip_total;
        return(ret);
    }
}
