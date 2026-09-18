package airtraffic.model;

public class Airport {
    private String name;
    private final String code;
    private double x, y;

    public Airport(String name, String code, double x, double y) {
        this.name = name;
        this.code = code;
        this.x = x;
        this.y = y;
    }

    public String getName() {return name;}
    public void setName(String name) {  this.name = name;   }
    public String getCode() {   return code;    }
    public double getX() {  return x;   }
    public void setX(double x) {    this.x = x; }
    public double getY() {  return y;   }
    public void setY(double y) {    this.y = y;}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Airport other = (Airport) obj;
        return this.code != null && this.code.equals(other.code);
    }

    @Override
    public int hashCode() {
        return code != null ? code.hashCode() : 0;
    }

    @Override
    public String toString() {
        return name + " (" + code + ")";
    }

    public String fullName(){
        return name + " - " + code + " (" + x + ", " + y + ")";
    }


}
