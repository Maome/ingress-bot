import com.google.common.geometry.S2LatLng;
import java.util.ArrayList;

public class Player{
    public ArrayList<IngressItem> inventory;
    public S2LatLng currentLocation;
    
    public Player(){
        this.inventory = new ArrayList<IngressItem>();
    }
    
    public void setLocation(S2LatLng newLocation){
        this.currentLocation = newLocation;
    }
    
}
