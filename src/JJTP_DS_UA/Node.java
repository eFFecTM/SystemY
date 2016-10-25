package JJTP_DS_UA;

/**
 * Created by jonas on 25-10-2016.
 */
public class Node
{
    String name;
    String ip;
    int hash;

    public Node(String name, String ip)
    {
        this.name = name;
        this.ip = ip;
    }

    public int getHash()
    {
        Integer hash  = name.hashCode();
        long hashLong = Integer.toUnsignedLong(hash);
        this.hash = (int)hashLong % 32768;
        return hash;
    }

    public String getIP()
    {
        return ip;
    }
}
