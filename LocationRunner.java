import java.io.*;
import com.google.common.geometry.*;

import java.lang.*;

public class LocationRunner{
    private BufferedReader br;
    private ClientWrapper cw;
    private S2LatLng currentLocation;
    public LocationRunner(FileReader fileReader, ClientWrapper clientWrapper) throws Exception {
        this.br = new BufferedReader(fileReader);  
        this.cw = clientWrapper;          
    }
    
    public void run() throws Exception{
        String curLine = br.readLine();
        String[] firstLocation = curLine.split(",");
        currentLocation = S2LatLng.fromDegrees(Double.parseDouble(firstLocation[0]),Double.parseDouble(firstLocation[1]));
        
        cw.newLocation(currentLocation);
        cw.printLocalHackablePortalNames();
        //cw.hackLocalPortals();
        
        GUI gui = new GUI(cw.player);
        
        while( (curLine = br.readLine()) != null ){
        
            cw.getInventory();
        
            System.out.println("Moving to location: " + curLine);
            DebugHandler.debugln("Moving to location: " + curLine);
           
            System.out.println(curLine.toString());
            DebugHandler.debugln(curLine.toString());
            
            
            String[] newLocation = curLine.split(",");
            S2LatLng newLoc = S2LatLng.fromDegrees(Double.parseDouble(newLocation[0]),Double.parseDouble(newLocation[1]));
            
            //get distance in meters
            Double dist = S2Wrapper.GreatEarthDistance(currentLocation, newLoc);
            TransitHandler th = new TransitHandler(currentLocation, newLoc, gui);
            th.start();
            int waitTimeSeconds = (int) (dist/5.0);
            
            System.out.println("Waiting " + waitTimeSeconds + " seconds to arrive.");
            DebugHandler.debugln("Waiting " + waitTimeSeconds + " seconds to arrive.");
            th.join();
            
            
            //Thread.sleep(1000 * waitTimeSeconds);
            
            System.out.println("Arrived!");
            DebugHandler.debugln("Arrived!");
            
            currentLocation = newLoc;
            cw.newLocation(newLoc);
            
            cw.printLocalHackablePortalNames();
            //cw.hackLocalPortals();
       }
            
    }
}
