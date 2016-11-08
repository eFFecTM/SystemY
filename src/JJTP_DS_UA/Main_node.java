/**
 * Created by JJTP on 25/10/2016.
 * This class is the Main class on a node (client computer). It initializes a node.
 */
package JJTP_DS_UA;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.lang.String;

/**
 * Created by JJTP on 31/10/2016.
 */

// Onder: Node
public class Main_node
{
    static Node node;
    public static void main(String[] args) throws UnknownHostException
    {
        node = new Node((Inet4Address) Inet4Address.getLocalHost());
            //getByName is een method van InetAddress, maar Inet4Address extends InetAddress
            //het geeft een inetAddress terug, dus casten naar Inet4Address
    }
}
