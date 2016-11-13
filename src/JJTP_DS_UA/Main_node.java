/**
 * Created by JJTP on 25/10/2016.
 * This class is the Main class on a node (client computer). It initializes a node.
 */
package JJTP_DS_UA;

import JJTP_DS_UA_GUI.MainPanel;
import JJTP_DS_UA_GUI.StartPanel;

import javax.swing.*;
import java.net.*;
import java.lang.String;
import java.util.Collections;
import java.util.Scanner;

/**
 * Created by JJTP on 31/10/2016.
 */

// Onder: Node
public class Main_node
{
    static Node node;
    private StartPanel startPanel;

    public static void main(String[] args) throws UnknownHostException, SocketException
    {
        Main_node main_node = new Main_node();
        main_node.start();

        //gui aanmaken (zie tabpaneel landbouw)
        //tabPaneel = new TabPaneel(this);
    }

    public void start()
    {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        startPanel = new StartPanel(this);
    }

    public void addNode()
    {


        boolean hasIP = false;

        GUI gui = new GUI(); // Zet dit in commentaar als men de GUI tijdelijk niet nodig heeft

        for (NetworkInterface netint : Collections.list(NetworkInterface.getNetworkInterfaces()))
        {
            for (InetAddress inetAddress : Collections.list(netint.getInetAddresses()))
            {
                System.out.println("Found IP's: " + inetAddress);
                if(inetAddress.toString().contains("192.168.1."))
                {
                    hasIP = true;
                    System.out.println("IP Adres: "+ inetAddress);
                    node = new Node((Inet4Address) inetAddress);
                }
            }
        }

        if(!hasIP)
        {
            System.out.println("IP not found! Type your local IP manually:");
            Scanner s = new Scanner(System.in);
            node = new Node ((Inet4Address) Inet4Address.getByName(s.nextLine()));
        }

        //getByName is een method van InetAddress, maar Inet4Address extends InetAddress
        //het geeft een inetAddress terug, dus casten naar Inet4Address
    }

    public Node getNode()
    {
        return this.node;
    }
}
