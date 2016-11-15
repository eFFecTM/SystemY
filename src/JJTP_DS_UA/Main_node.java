/**
 * Created by JJTP on 25/10/2016.
 * This class is the Main class on a node (client computer). It initializes a node.
 */
package JJTP_DS_UA;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    static GUI gui;

    public static void main(String[] args) throws SocketException, UnknownHostException
    {
        node = new Node();
        gui = new GUI(); // Zet dit in commentaar als men de GUI tijdelijk niet nodig heeft
        addActionListeners();
        setNodeName();
        node.getIP();
        node.listenMC();
    }

    private static void setNodeName()
    {
        String name;
        name = gui.setNodeName();
        node.checkName(name);
        while(node.wrongName)
        {
            name = gui.setNodeNameAgain();
            node.checkName(name);
        }
        node.startUp(name);
        gui.openPanel();
    }
/*
    public static void getIP() throws UnknownHostException, SocketException
    {
        boolean hasIP = false;
        Inet4Address IP = null;
        for (NetworkInterface netint : Collections.list(NetworkInterface.getNetworkInterfaces()))
        {
            for (InetAddress inetAddress : Collections.list(netint.getInetAddresses()))
            {
                System.out.println("Found IP's: " + inetAddress);
                if (inetAddress.toString().contains("192.168.1."))
                {
                    hasIP = true;
                    System.out.println("IP Adres: " + inetAddress);
                    IP = (Inet4Address) inetAddress;
                }
            }
        }
        if (hasIP)
        {
            node = new Node(IP);

        } else
        {
            System.out.println("IP not found! Type your local IP manually:");
            Scanner s = new Scanner(System.in);
            node = new Node((Inet4Address) Inet4Address.getByName(s.nextLine()));
        }
    }
    */
    //getByName is een method van InetAddress, maar Inet4Address extends InetAddress
    //het geeft een inetAddress terug, dus casten naar Inet4Address


    public static void addActionListeners()
    {
        gui.logOutButtonActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                node.shutDown();
            }
        });


    }


}
