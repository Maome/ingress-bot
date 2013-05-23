public class DebugHandler{
    public static boolean debug = true;
    public static void debugln(String output){
        if(debug)
            System.out.println(output);
    }
}
