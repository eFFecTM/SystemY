package JJTP_DS_UA;

import java.rmi.RemoteException;

public class Main_nameServer
{

    public static void main(String[] args) throws RemoteException
    {
        Node node1 = new Node("jonas","192.168.1.2");
        Node node2 = new Node("jan","192.168.1.1");
        NameServer nameServer = new NameServer();
        nameServer.addName(node1.getName(), node1.getIP());
        nameServer.addName(node2.getName(), node2.getIP());
    }
}
