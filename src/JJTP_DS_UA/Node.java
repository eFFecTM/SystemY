package JJTP_DS_UA;

import java.util.Scanner;

/**
 * Created by JJTP on 25-10-2016.
 */
public class Node
{
    String name;
    String ip;
    Node_NameServerCommunication NScommunication;

    public Node(String name, String ip)
    {
        this.name = name;
        this.ip = ip;
        NScommunication = new Node_NameServerCommunication();
    }

    public String searchFile()
    {
        System.out.println("Give a filename you would like to search: ");
        Scanner s = new Scanner(System.in);
        String fileName = s.nextLine();
        return NScommunication.searchFile(fileName); //returned het ip waar de file zich bevindt
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
