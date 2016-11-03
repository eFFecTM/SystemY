package JJTP_DS_UA;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

public class Main_nameServer
{
    static NameServer nameServer;

    public static void main(String[] args) throws RemoteException, UnknownHostException
    {
        nameServer = new NameServer();
        testPracticum3();

    }

    public static void testPracticum3() throws UnknownHostException
    {
        nameServer.addNode("jonas",(Inet4Address) Inet4Address.getByName("192.168.1.3"));
        nameServer.addNode("jonas", (Inet4Address) Inet4Address.getByName("192.168.1.2"));
        //nameServer.deleteNode("jonas");
        //nameServer.deleteNode("jonas");
    }
}
