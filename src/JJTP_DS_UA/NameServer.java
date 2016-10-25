package JJTP_DS_UA;

import java.net.InetAddress;
import java.util.HashMap;

/**
 * Created by JJTP on 25/10/2016.
 */
public class NameServer
{
    HashMap<Integer,String> map;

    public NameServer()
    {
        map = new HashMap<Integer,String>(); //InetAddress
    }

    public void addName(String name)
    {
        Integer hashCode = name.hashCode();
        Integer hash = (int) Integer.toUnsignedLong(hashCode) % 32768;
        map.put(hash,name);
    }

    public void printTable()
    {

    }

    public String getOwner(String fileName)
    {
        return "test"
    }
}
