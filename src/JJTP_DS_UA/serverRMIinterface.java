package JJTP_DS_UA;

import java.net.Inet4Address;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by JJTP on 25-10-2016.
 */
public interface ServerRMIinterface extends Remote
{
    public Inet4Address findFile(String fileName) throws RemoteException;
    public int checkAmountOfNodes();
}
