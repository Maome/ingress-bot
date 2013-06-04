public class DebugHandler{
    public static String debugLines = "";
    public static String infoLines = "";
    public static boolean debug = true;
    public static void debugln(String output){
        debugLines += "\n" + output;
        if(debug)
            System.out.println(output);
    }
    public static void debugInfo(String output){
        infoLines += "\n" + output;
    }
    
}
