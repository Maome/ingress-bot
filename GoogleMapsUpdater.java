import java.lang.*;
import java.net.*;
import javax.swing.*;

public class GoogleMapsUpdater extends Thread{
    private JLabel gUILabel;
    private String webAddress;
    private JFrame frame;
    
    public GoogleMapsUpdater(JLabel label, String address, JFrame theFrame){
        this.gUILabel = label;
        this.webAddress = address;
        this.frame = theFrame;
    }
    
    public void run(){
        try{
            for(int i = 0; i < 10; i++){
                gUILabel.setIcon(new ImageIcon(new URL(webAddress + Integer.toString(i))));
                frame.pack();
                Thread.sleep(1000);
            }
            //frame.repaint();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
            
    }
        
}
