package JJTP_DS_UA;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by jonas on 31-10-2016.
 */
public class serverRMIklasse_temp extends UnicastRemoteObject implements serverRMIinterface_temp
{
    String filename;
    String returnIP;
    NameServer nameserver;


    public serverRMIklasse_temp(NameServer n) throws RemoteException{
        super();
        nameserver = n;
    }

    public String getOwner(String fileName) throws RemoteException
    {
        return nameserver.lookup(fileName);
    }

    public String getFileNaam()
    {
        return filename;
    }

    public void putIP(String ip)
    {
        returnIP = ip;
    }
}
