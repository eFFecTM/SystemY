/**
 * Created by JJTP on 25/10/2016.
 * This class is the Main class on a node (client computer). It initializes a node.
 */
package JJTP_DS_UA;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.lang.String;

/**
 * Created by JJTP on 31/10/2016.
 */
public class Main_node
{
    static Node node;
    public static void main(String[] args) throws UnknownHostException
    {
        node = new Node((Inet4Address) Inet4Address.getByName("192.168.1.1"));
            //getByName is een method van InetAddress, maar Inet4Address extends InetAddress
            //het geeft een inetAddress terug, dus casten naar Inet4Address
        //testPracticum3();
        try
        {
            Thread.sleep(5000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        node.updateNewNodeNeighbours("192.168.1.5");

        /*
        for(int i=0;i<1000000;i++)
        {
            System.out.println(node.nextHash + " en " + node.prevHash);
            try
            {
                Thread.sleep(5000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        */

    }

    public static void testPracticum3()
    {
       // node.NScommunication.searchFile(textInput()); //vraagt om naam en ip, maar filenaam+extensie
    }
}
