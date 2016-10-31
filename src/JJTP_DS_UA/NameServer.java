package JJTP_DS_UA;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by JJTP on 25/10/2016.
 */
public class NameServer
{
    TreeMap<Integer,String> nodeMap;


    public NameServer()
    {
        nodeMap = new TreeMap<>(); //InetAddress
    }

    public void addName(String name, String IP)
    {
        Integer hashCode = name.hashCode();
        Integer hash = (int) Integer.toUnsignedLong(hashCode) % 32768;
        nodeMap.put(hash,IP);
    }

    public String getOwner(String fileName)
    {
        // @TODO opzoek schrijven
        return "";
    }
}
