package JJTP_DS_UA;

import java.rmi.RemoteException;
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
    }

    public void addName(String name, String IP)
    {
        Integer hashCode = name.hashCode();
        Integer hash = (int) Integer.toUnsignedLong(hashCode) % 32768;
        nodeMap.put(hash,IP);
    }

    public String lookup(String fileName)
    {
        Integer fileNameHash = (int) (Integer.toUnsignedLong(fileName.hashCode()) % 32768);
        if(nodeMap.lowerKey(fileNameHash)==null) // returned key < dan de meegegeven paramater of null als die niet bestaat
            return nodeMap.get(nodeMap.lastKey()); //returned de grootste key uit de map
        else
            return nodeMap.get(nodeMap.lowerKey(fileNameHash));
    }

}
