import com.google.common.geometry.*;
import java.io.*;

public class Pygress{
    static boolean debug = true;

    //how to get your authcookie
    //m-dot-betaspike.appspot.com/handshake
    //google log in with your account
    //check cookies from m-dot-betaspike.appspot.com for SACSID

    private static String authCookie;    
    public static void main(String[] args) throws Exception{
    
        FileReader authCookieInputStream = new FileReader("authcookie");
        BufferedReader br = new BufferedReader(authCookieInputStream);
        authCookie = br.readLine();
        br.close();
        
        Player player = new Player();
        
        ClientWrapper clientWrapper = new ClientWrapper(authCookie, player);
        clientWrapper.handshake();
        clientWrapper.getInventory();


        FileReader locationsFile = new FileReader("locations");
        LocationRunner locRunner = new LocationRunner(locationsFile, clientWrapper);
        
        locRunner.run();
 
        //clientWrapper.getInventory();
 
        System.out.println("\n\n\ncomplete!\n\n");
        /*
        ClientWrapper clientWrapper = new ClientWrapper(authCookie);
        clientWrapper.handshake();
        
        /*
        S2LatLng lowellpark = S2LatLng.fromDegrees(48.7268, -122.4926);
        S2LatLng fxcollege = S2LatLng.fromDegrees(48.7304, -122.4856);
        
        double dist = S2Wrapper.GreatEarthDistance(lowellpark, fxcollege);
        System.out.println(dist);
        */
        
          /*      
        S2LatLng testll = S2LatLng.fromDegrees(48.73410, -122.48640);
        //S2LatLng testll = S2LatLng.fromDegrees(48.73258,-122.48634);
        S2CellId testID = S2CellId.fromToken("5485a3cbd1100000");
        S2LatLng testll2 = testID.toLatLng();
        clientWrapper.newLocation(testll);
        clientWrapper.printLocalHackablePortalNames();
        clientWrapper.hackLocalPortals();
        
        //Read file with path
        //Calculate times
        //Fuzz path (normal distrobution)
        //Run path
            //Hack portals along the way
        */
    }
}

//ToDo:
//For each energy glob left, check the ammount, the distance, and your current xm
//If it's close enough (for your level, check that too) grab it and block for a response (so we don't grab too many of them and try to eat them when we are at max xm)
//
//Location is stored as cell in the first 16 chars (as hex) of the energy glob
//Ammount of xm in the glob is the last four chars as hex



/*
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
*/



   /*
        System.out.println("\n\nRunning s2cell selftest...");
        S2LatLng testll, testll2;
        testll = S2LatLng.fromDegrees(48.755821, -122.483697);
        //testll2 = S2LatLng.fromDegrees(48.855821, -122.583697);
        home = testll;
        S2CellId testID = S2CellId.fromToken("5485a3cbd1100000");
        testll2 = testID.toLatLng();
        System.out.println("\t" + testll2.lngDegrees() + " : " + testll2.latDegrees());
        
        

        ArrayList<S2CellId> cells = getSurroundingCells(testll);
        System.out.println("[" + ANSI_GREEN + "Successful" + ANSI_RESET + "] s2cell seftest" + "\n");
        
        System.out.println("Running great earth distance selftest...");
        System.out.println("\t" + gcdistance(testll, testll2) + ANSI_RESET);
        System.out.println("[" + ANSI_GREEN + "Successful" + ANSI_RESET + "] great earth distance selftest" + "\n");
    */

        //long st = handshake();

        //System.out.println(Integer.toHexString( Float.floatToIntBits((float) home.latDegrees())));
        /*
        BigInteger big = new BigInteger("f8b303b3",16);
        System.out.println((double) big.intValue()/1000000);
        */
/*
        System.out.println("Origin location: 02e79982,f8b3020c");
        S2LatLng t = decodeLocation("02e79982,f8b3020c");
        System.out.println("Decode location test: " + t.latDegrees() + " : " + t.lngDegrees());
        System.out.println("Reencoded location test : " + encodeLocation(t));
        
*/
