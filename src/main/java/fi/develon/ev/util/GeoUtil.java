package fi.develon.ev.util;

import java.math.BigDecimal;

/**
 * @author mahmood
 * @since 9/11/21
 */
public class GeoUtil {

    private GeoUtil() {
    }

    /**
     * Function to calculate distance between two point
     *
     * @param lat1 point 1 latitude
     * @param lon1 point 1 longitude
     * @param lat2 point 2 latitude
     * @param lon2 point 2 longitude
     * @return distance in kilometer
     */
    private static double distance(BigDecimal lat1, BigDecimal lon1, BigDecimal lat2, BigDecimal lon2) {
        double theta = lon1.doubleValue() - lon2.doubleValue();
        double dist = Math.sin(Math.toRadians(lat1.doubleValue())) *
                Math.sin(Math.toRadians(lat2.doubleValue())) +
                Math.cos(Math.toRadians(lat1.doubleValue())) *
                        Math.cos(Math.toRadians(lat2.doubleValue())) *
                        Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        return (dist);
    }

}
