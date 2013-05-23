import com.google.common.geometry.*;

public class EnergyGlob{
    public S2LatLng s2ll;
    public String name;
    public EnergyGlob(String raw){
        this.name = raw;
        this.s2ll = S2CellId.fromToken(raw.substring(0,16)).toLatLng();
    }
}
