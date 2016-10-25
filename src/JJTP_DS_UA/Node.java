package JJTP_DS_UA;

/**
 * Created by jonas on 25-10-2016.
 */
public class Node
{
    String name;
    String ip;

    public Node(String name, String ip)
    {
        this.name = name;
        this.ip = ip;
    }


    public String getIP()
    {
        return ip;
    }

    public String getName()
    {
        return name;
    }
}
