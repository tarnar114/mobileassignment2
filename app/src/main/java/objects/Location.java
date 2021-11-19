package objects;

public class Location {
    private String id;
    private Integer longitude;
    private Integer latitude;
    private String address;
    public Location(String id,Integer longitude,Integer latitude,String address){
        this.id=id;
        this.longitude=longitude;
        this.latitude=latitude;
        this.address=address;
    }
    public String getId(){return id;}
    public Integer getLongitude(){return longitude;}
    public Integer getLatitude(){return latitude;}
    public String getAddress(){return address;}

}
