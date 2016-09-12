package com.ef.newlead.domain.location;

/**
 * Created by seanzhou on 9/12/16.
 */
public class GeoPosition {
    private final double longitude;
    private final double latitude;

    public GeoPosition(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    @Override
    public int hashCode() {
        int result = 17;
        long lon = Double.doubleToLongBits(longitude);
        long lat = Double.doubleToLongBits(latitude);

        result = 31 * result + (int) (lon ^ (lon >> 32));
        result = 31 * result + (int) (lat ^ (lat >> 32));

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GeoPosition))
            return false;

        GeoPosition gp = (GeoPosition) o;

        return Double.compare(gp.getLatitude(), latitude) == 0 &&
                Double.compare(gp.getLongitude(), longitude) == 0;
    }
}
