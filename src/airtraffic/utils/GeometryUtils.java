package airtraffic.utils;

public class GeometryUtils {

    private GeometryUtils() {}

    public static double distanceSq(double x1, double y1, double x2, double y2){
        double dx = x1 - x2;
        double dy = y1 - y2;
        return dx * dx + dy * dy;
    }

    public static double distance(double x1, double y1, double x2, double y2){
        return Math.sqrt(distanceSq(x1, y1, x2, y2));
    }

    public static boolean pointInCircle(double px, double py, double cx, double cy, double r){
        return distanceSq(px, py, cx, cy) <= r * r;
    }

    public static boolean pointInCenteredRect(double px, double py, double cx, double cy, double halfW, double halfH){
        return Math.abs(px - cx) <= halfW && Math.abs(py - cy) <= halfH;
    }

    public static double lerp(double a, double b, double t){
        return a + t * (b - a);
    }
}
