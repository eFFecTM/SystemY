/**
 * Created by JJTP on 25/10/2016.
 * This class is the Main class on the NameServer (server computer). It initializes a nameserver.
 */
package JJTP_DS_UA;

import java.net.UnknownHostException;
import java.rmi.RemoteException;

// Onder: NameServer
public class Main_nameServer
{
    static NameServer nameServer;

    public static void main(String[] args) throws RemoteException, UnknownHostException, InterruptedException
    {
        nameServer = new NameServer();
    }
}
