package JJTP_DS_UA;

import java.net.Inet4Address;

/**
 * Created by JJTP on 25-10-2016.
 */
public class Node
{
    String name;
    Inet4Address ip;
    Node_NameServerCommunication NScommunication;

    public Node(String name, Inet4Address ip)
    {
        this.name = name;
        this.ip = ip;
        NScommunication = new Node_NameServerCommunication();
    }

    public Inet4Address getIP()
    {
        return ip;
    }

    public String getName()
    {
        return name;
    }
}
