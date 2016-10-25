package JJTP_DS_UA;

public class Main
{

    public static void main(String[] args)
    {
        Node node = new Node("jonas","192.168.1.2");
        String nodeName = node.getName();
        NameServer naming = new NameServer();
        naming.addName(nodeName);

    }
}
