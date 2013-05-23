import com.google.common.geometry.*;

public class Portal{

    public String guid;
    public String title;
    public S2LatLng location;
    
    public Portal(String iguid, String ititle, S2LatLng ilocation){
        this.guid = iguid;
        this.title = ititle;
        this.location = ilocation;
    }
    
    public Portal(String iguid, S2LatLng ilocation){
        this.guid = iguid;
        this.title = "Unknown title";
        this.location = ilocation;
    }
}
