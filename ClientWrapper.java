import com.google.common.geometry.*;

import java.math.BigInteger;

import org.json.simple.*;
import org.json.simple.parser.*;

import java.net.*;
import java.io.*;

import java.util.ArrayList;
import java.util.LinkedList;

public class ClientWrapper{
    private ArrayList<EnergyGlob> localEnergyGlobs = new ArrayList<EnergyGlob>();
    private ArrayList<S2CellId> localCellIds = new ArrayList<S2CellId>();
    private S2LatLng currentLocation = new S2LatLng();
    //Array list for portals?
    
    private String authCookie;
    private String nickname;
    private String xsrfToken;
    private long syncTimestamp;
    
    //Final connection strings for initializing a connection
    public final String baseURL = "https://m-dot-betaspike.appspot.com";
    public final String nemesisDeviceID = "json=%7B%22nemesisSoftwareVersion%22%3A%222013-05-03T19%3A32%3A11Z+929c2cce62eb+opt%22%2C%22deviceSoftwareVersion%22%3A%222.3.3%22%7D";
    
    public ClientWrapper(String authCookie){
        this.authCookie = authCookie;
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////
    // handshake()
    // Performs a secure handshake with the remote google server and collects connection information
    // for use with later messages
    // Side Effects : nickname, xsrfToken, syncTimestamp
    //////////////////////////////////////////////////////////////////////////////////////////////////
    public void handshake() throws Exception{
        DebugHandler.debugln("Handshaking...");
        
        //Setup the URL connection
        URL handshakeURL = new URL(baseURL + "/handshake?" + nemesisDeviceID);
        URLConnection handshakeCon = handshakeURL.openConnection();
        handshakeCon.setRequestProperty("Cookie", authCookie);
        handshakeCon.setRequestProperty("User-Agent", "Nemesis (gzip)");
        
        //Use a buffered reader to read the json response
        BufferedReader br = new BufferedReader(new InputStreamReader(handshakeCon.getInputStream()));
        String response = br.readLine();
        response = response.substring(9); // This removes the while(1); from the begining of googles response
        br.close();
        
        //Setup the JSON reader
        JSONParser jp = new JSONParser();
        JSONObject obj = (JSONObject) jp.parse(response);
        JSONObject result = (JSONObject) (obj.get("result"));
        
        //Read Strings from JSON objects
        this.nickname = (String) result.get("nickname");
        this.xsrfToken = (String) result.get("xsrfToken");
        String syncTimestampSTR = (String) ((JSONObject) result.get("initialKnobs")).get("syncTimestamp");
        this.syncTimestamp = Long.parseLong(syncTimestampSTR, 10);
        
        //Give results to the debug handler
        DebugHandler.debugln("\tNickname: " + nickname);
        DebugHandler.debugln("\tSyncTime: " + syncTimestampSTR);
        DebugHandler.debugln("\tXSRF-Tok: " + xsrfToken);
        DebugHandler.debugln("\tSuccess!");
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////
    // getObjectsInCells()
    // Updates the ClientWrapper object with a new set of energyGlobs based on it's current cells
    // by querying the remote server for all objects in the area
    // TODO : Add parsing of more than just energy globs (get portal information)
    // Side Effects : localEnergyGlobs
    //////////////////////////////////////////////////////////////////////////////////////////////////
    @SuppressWarnings("unchecked")
    public void getObjectsInCells() throws Exception{
    
        //Begin setting up JSON Objects
        LinkedList<String> ll = new LinkedList<String>();
        JSONArray cellsAsHex = new JSONArray();
        JSONArray dates = new JSONArray();
        JSONArray energyGlobGuids = new JSONArray();
        JSONObject main = new JSONObject();
        JSONObject params = new JSONObject();
        JSONObject clientBasket = new JSONObject();
        
        //Add local cells and 0 dates to arrays
        for(int i = 0; i < this.localCellIds.size(); i++){
            cellsAsHex.add(Long.toHexString(this.localCellIds.get(i).id()));
            dates.add(0);
        }
        
        //Setup edible globs here
            //TODO!
        
        //Setup the json message structure
        clientBasket.put("clientBlob",null);
        params.put("cellsAsHex", cellsAsHex);
        params.put("dates", dates);
        params.put("clientBasket", clientBasket);
        params.put("cells", null);
        params.put("energyGlobGuids", energyGlobGuids);
        params.put("playerLocation", S2Wrapper.encodeLocation(this.currentLocation));
        params.put("knobSyncTimestamp", this.syncTimestamp);
        main.put("params", params);
        
        //Setup the json connection
        URL getObjectsURL = new URL(baseURL + "/rpc/gameplay/getObjectsInCells");
        URLConnection getObjectsCon = getObjectsURL.openConnection();
        getObjectsCon.setRequestProperty("Cookie", authCookie);
        getObjectsCon.setRequestProperty("X-XsrfToken", this.xsrfToken);
        getObjectsCon.setDoOutput(true);
        
        //Setup and use the writer
        OutputStreamWriter out = new OutputStreamWriter(getObjectsCon.getOutputStream());
        out.write(main.toString());  //Write our json object to the connection
        out.close();
        
        //Setup and use the reader
        BufferedReader br = new BufferedReader(new InputStreamReader(getObjectsCon.getInputStream()));
        String line = br.readLine();
        br.close();
        
        //Decode the (interesting parts of the) json response
        JSONParser jp = new JSONParser();
        JSONObject obj = (JSONObject) jp.parse(line);
        JSONObject gameBasket = (JSONObject) obj.get("gameBasket");
        JSONArray responseEnergyGlobGuids = (JSONArray) (gameBasket.get("energyGlobGuids"));

        //Update our localEnergyGlobs array list
        for(int i = 0; i < responseEnergyGlobGuids.size(); i++)
            localEnergyGlobs.add(new EnergyGlob(responseEnergyGlobGuids.get(i).toString()));
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////
    // getSurroundingCells()
    // Updates the ClientWrapper object with a new set of cells based on it's current location
    // Side Effects : localCellIds
    //////////////////////////////////////////////////////////////////////////////////////////////////
    private void getSurroundingCells(){   
        //Setup the coverer and the distance from
        S2RegionCoverer coverer = new S2RegionCoverer();
        S2LatLng size = S2LatLng.fromDegrees(0.0045,0.0045);
        coverer.setMinLevel(16);
        coverer.setMaxLevel(16);

        //Create the rectangle
        S2LatLngRect rect = S2LatLngRect.fromCenterSize(this.currentLocation, size);

        //Use the coverer to fill localCellIds
        coverer.getCovering(rect, localCellIds);
    }   
    
    //////////////////////////////////////////////////////////////////////////////////////////////////
    // newLocation(S2LatLng)
    // Updates the ClientWrapper object with a new location. Old location reliant information is
    // cleared and new cells and object requests are called
    // TODO : Automatically check xm and eat available energyglobs
    // Side Effects : curentLocation, localCellIds, localEnergyGlobs
    //////////////////////////////////////////////////////////////////////////////////////////////////
    public void newLocation(S2LatLng newLocation) throws Exception{
        //clear current data?
        clearLocalData();
        
        //call anything that changes on new location
        currentLocation = newLocation;
        getObjectsInCells();        
        
        //get objects in cells, then call again to eat?
            //check xm
            //if low then eat
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////
    // clearLocalData()
    // Clears data stored in array lists, called when updating location information
    // Side Effects : localCellIds, localEnergyGlobs
    //////////////////////////////////////////////////////////////////////////////////////////////////    
    private void clearLocalData(){
        localEnergyGlobs.clear();
        localCellIds.clear();
    }
    

}
