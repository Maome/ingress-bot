import com.google.common.geometry.*;
import java.math.BigInteger;

public class S2Wrapper{
    
    private final static double EARTH_RADIUS = 6371000;
    
    public static double GreatEarthDistance(S2LatLng a, S2LatLng b){
        double angle = Haversin(a.latDegrees() - b.latDegrees()) + Math.cos(a.latDegrees()) * Math.cos(b.latDegrees()) * Haversin(a.latDegrees() - b.latDegrees()); 
        return angle * EARTH_RADIUS;
    }
    
    public static double Haversin(double a){
        return ((1 - Math.cos(a))/2);
    }
    
    public static S2LatLng decodeLocation(String locationString){
        String[] locationStringArray = locationString.split(",");
        BigInteger lat = new BigInteger(locationStringArray[0],16);
        BigInteger lng = new BigInteger(locationStringArray[1],16);
        double dlat = (double) lat.intValue() / 1000000;
        double dlng = (double) lng.intValue() / 1000000;
        S2LatLng ret = S2LatLng.fromDegrees(dlat, dlng);
        return ret;
    }
    
    public static String encodeLocation(S2LatLng s2ll){
        int lat = (int) (s2ll.latDegrees() * 1000000);
        int lng = (int) (s2ll.lngDegrees() * 1000000);
        String ret = String.format("%08x,%08x", Integer.valueOf(lat), Integer.valueOf(lng));
        return ret;
    }
}
