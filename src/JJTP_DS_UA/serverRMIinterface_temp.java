package JJTP_DS_UA;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by JJTP on 25-10-2016.
 */
public interface serverRMIinterface_temp extends Remote
{
    public String findFile(String fileName) throws RemoteException;
}
