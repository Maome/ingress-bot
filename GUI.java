import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.awt.image.*;
import javax.imageio.ImageIO;



import com.google.common.geometry.*;

public class GUI{

    public Player player;

    public JTextArea debugJTArea;
    public JScrollBar debugVertical;
    
    public JTextArea itemsJTArea;
    
    public JLabel jLabel;
    public JFrame frame;

    //public static void main(String[] args) throws Exception{
    //    GUI gui = new GUI();
    //}
    
    public GUI(Player player) throws Exception{
        this.player = player;
        /*
        URLConnection con = new URL("http://maps.google.com/maps/api/staticmap?center=48.73400,-122.48640&zoom=15&size=512x512&maptype=roadmap&markers=color:blue|label:S|48.73400,-122.48640&sensor=true").openConnection();
        InputStream is = con.getInputStream();
        byte[] bytes = new byte[con.getContentLength()];
        is.read(bytes);
        is.close();
        
        
        
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
        */
        /* 
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(5,1));
 
        JTextField textField;
        JTextArea textArea;
    
        textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        */
        
        JPanel mainTop = new JPanel(new BorderLayout(5,5));
        JPanel mainBot = new JPanel(new BorderLayout(5,5));
        
        JPanel topLeftMap = new JPanel(new BorderLayout(5,5));
        JPanel topRightInfo = new JPanel(new BorderLayout(5,5));
        
        JPanel infoControls = new JPanel(new GridLayout(1,3));
        
        JButton forceQuit = new JButton("Force Quit");
        JButton loadWaypoints = new JButton("Load Waypoints");
        JButton start = new JButton("Start");
        
        infoControls.add(start);
        infoControls.add(forceQuit);
        infoControls.add(loadWaypoints);
        
        
        //rightPanel.add(scrollPane);
        //rightPanel.add(new JLabel("test"));
        
        
        //Toolkit tk = new Toolkit();
        jLabel = new JLabel();
        String address = "http://maps.google.com/maps/api/staticmap?zoom=15&size=512x512&maptype=roadmap&sensor=true&markers=color:blue|label:Sderp|48.73400,-122.486&center=48.73400,-122.486";
        //jLabel.setIcon(new ImageIcon(new URL(address)));
        
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(5,5));
        
        debugJTArea = new JTextArea(10, 80);
        debugJTArea.setLineWrap(true);
        JScrollPane debugScrollPane = new JScrollPane(debugJTArea);
        debugVertical = debugScrollPane.getVerticalScrollBar();
        


        mainBot.add(debugScrollPane, BorderLayout.PAGE_START);
        
        topLeftMap.add(jLabel);
        
        JTextArea infoJTArea = new JTextArea(15, 30);
        itemsJTArea = new JTextArea(15,30);
        JScrollPane itemScrollPane = new JScrollPane(itemsJTArea);
        topRightInfo.add(itemScrollPane, BorderLayout.CENTER);
        topRightInfo.add(infoControls, BorderLayout.PAGE_START);
        topRightInfo.add(infoJTArea, BorderLayout.PAGE_END);
        
        
        mainTop.add(topLeftMap, BorderLayout.LINE_START);
        mainTop.add(topRightInfo, BorderLayout.LINE_END);
        
        frame.add(mainTop, BorderLayout.PAGE_START);
        frame.add(mainBot, BorderLayout.PAGE_END);
        
        
        
        //frame.add(jLabel, BorderLayout.LINE_START);
        //frame.add(rightPanel, BorderLayout.LINE_END);
        frame.pack(); // small race condition, image may not be fully prepared
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        //GoogleMapsUpdater gmu = new GoogleMapsUpdater(jLabel, address, frame);
        //gmu.start();
        
        
        
        
    }
    
    public void updateGUI() throws Exception{
        
    }
    
    public void updateLocation(S2LatLng s2ll) throws Exception{
        double lat = s2ll.latDegrees();
        double lng = s2ll.lngDegrees();
        
        String add = "http://maps.google.com/maps/api/staticmap?zoom=18&size=512x512&maptype=roadmap&sensor=true&markers=color:blue|label:U|" + Double.toString(lat) + "," + Double.toString(lng) + "&center=" + Double.toString(lat) + "," + Double.toString(lng);
        System.out.println(add);
        jLabel.setIcon(new ImageIcon(new URL(add)));
        
        debugJTArea.setText(DebugHandler.debugLines);
        
        debugVertical.setValue( debugVertical.getMaximum() );
        
        String inventorySTR = "";
        for(int i = 0; i < player.inventory.size();i++)
            inventorySTR += player.inventory.get(i).toString() + "\n";
            
        itemsJTArea.setText(inventorySTR);
        
        frame.pack();
     }
}


/*
TODO : for gui
make get portals from client wrapper and get locations from file
and get location string from client wrapper
*/
