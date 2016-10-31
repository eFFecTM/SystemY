package JJTP_DS_UA;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by JJTP on 25/10/2016.
 */
public class NameServer
{
    TreeMap<Integer,String> nodeMap;


    public NameServer() throws RemoteException
    {
        nodeMap = new TreeMap<>(); //InetAddress
        serverRMIklasse_temp RMIklasse = new serverRMIklasse_temp(this);
        String filenaam = RMIklasse.getFileNaam();
    }

    public void addName(String name, String IP)
    {
        Integer hashCode = name.hashCode();
        Integer hash = (int) Integer.toUnsignedLong(hashCode) % 32768;
        nodeMap.put(hash,IP);
    }

    public String lookup(String fileName)
    {
        // @TODO opzoek schrijven
        return "";
    }

}
