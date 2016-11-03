package JJTP_DS_UA;

import java.io.IOException;
import java.net.*;

/**
 * Created by JJTP on 25-10-2016.
 */
public class Node
{
    String name;
    Inet4Address ip;
    Node_NameServerCommunication NScommunication;

    public Node(String name, Inet4Address ip)
    {
        this.name = name;
        this.ip = ip;
        NScommunication = new Node_NameServerCommunication();
        startUp();
        listenMC();

    }

    public Inet4Address getIP()
    {
        return ip;
    }

    public String getName()
    {
        return name;
    }

    public void startUp()
    {
        try
        {
            int portMC = 12345; // Multicast Port waarop men gaat sturen
            Inet4Address IPMC = (Inet4Address) Inet4Address.getByName("230.0.0.0");
            // Multicast IP range: 224.0.0.0 - 239.255.255.255
            Inet4Address IP = (Inet4Address) Inet4Address.getByName("192.168.1.10");
            byte[] msg = (name + " " + IP.getHostAddress()).getBytes(); // naam en adres zijn gescheiden door een spatie

            DatagramSocket socket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(msg, msg.length);
            packet.setAddress(IPMC);
            packet.setPort(portMC);
            socket.send(packet);

            System.out.println("Multicast message send.");
            socket.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void listenMC()
    {
        int portMC = 12345;
        Inet4Address IPMC = null;
        try
        {
            IPMC = (Inet4Address) Inet4Address.getByName("230.0.0.0");
            MulticastSocket mcSocket;
            mcSocket = new MulticastSocket(portMC);
            mcSocket.joinGroup(IPMC);

            DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
            System.out.println("Waiting for a  multicast message...");
            mcSocket.receive(packet);
            String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());
            System.out.println("Multicast Received: " + msg);
            String[] info = msg.split(" "); // het ontvangen bericht splitsen in woorden gescheiden door een spatie
            System.out.println("Naam: " + info[0]);
            System.out.println("IP: " + info[1]);

            mcSocket.leaveGroup(IPMC);
            mcSocket.close();
            listenMC();
        } catch(IOException e)
        {
            e.printStackTrace();
        }

    }
}
