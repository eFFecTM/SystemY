/**
 * Created by JJTP on 25/10/2016.
 * This class is the Main class on a node (client computer). It initializes a node.
 */
package JJTP_DS_UA;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Created by JJTP on 31/10/2016.
 */
public class Main_node
{
    static Node node;
    public static void main(String[] args) throws UnknownHostException
    {
        node = new Node(textInput(), (Inet4Address) Inet4Address.getByName(textInput()));
            //getByName is een method van InetAddress, maar Inet4Address extends InetAddress
            //het geeft een inetAddress terug, dus casten naar Inet4Address
        //testPracticum3();
    }

    public static String textInput()
    {
        System.out.println("Choose a name for the node and press enter, fill in the correct ip-address and press enter.");
        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }

    public static void testPracticum3()
    {
        node.NScommunication.searchFile(textInput()); //vraagt om naam en ip, maar filenaam+extensie
    }
}
