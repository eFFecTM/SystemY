package JJTP_DS_UA;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by jonas on 31-10-2016.
 */
public class serverRMI extends UnicastRemoteObject implements serverRMIinterface
{
    NameServer nameserver;


    public serverRMI(NameServer n) throws RemoteException{
        super();
        nameserver = n;
    }

    public String findFile (String fileName) throws RemoteException
    {
        return nameserver.lookup(fileName);
    }

}
