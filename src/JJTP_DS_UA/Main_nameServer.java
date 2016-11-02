package JJTP_DS_UA;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

public class Main_nameServer
{
    public static void main(String[] args) throws RemoteException, UnknownHostException
    {
        // TODO: Herschrijven van Main_nameServer, is dit puur nog een testsituatie?
        Node node1 = new Node("jonas",(Inet4Address) Inet4Address.getByName("192.168.1.2"));
        Node node2 = new Node("jan",(Inet4Address) Inet4Address.getByName("192.168.1.1"));
        NameServer nameServer = new NameServer();
        nameServer.addName(node1.getName(), node1.getIP());
        nameServer.addName(node2.getName(), node2.getIP());
    }
}
