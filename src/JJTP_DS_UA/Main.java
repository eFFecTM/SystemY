package JJTP_DS_UA;

public class Main
{

    public static void main(String[] args)
    {
        Node node1 = new Node("jonas","192.168.1.2");
        Node node2 = new Node("jan","192.168.1.1");
        String nodeName1 = node1.getName();
        String nodeName2 = node2.getName();
        NameServer nameServer = new NameServer();
        nameServer.addName(nodeName1);
        nameServer.addName(nodeName2);
    }
}
