package JJTP_DS_UA;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

public class Main_nameServer
{
    public static void main(String[] args) throws RemoteException, UnknownHostException
    {
        NameServer nameServer = new NameServer();
    }
}
