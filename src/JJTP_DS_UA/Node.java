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
    int ownHash,prevHash,nextHash;

    public Node(String name, Inet4Address ip)
    {
        this.name = name;
        this.ip = ip;
        NScommunication = new Node_NameServerCommunication();
        Integer hashCode = name.hashCode();
        Integer ownHash = (int) Integer.toUnsignedLong(hashCode) % 32768;
        prevHash = ownHash;
        nextHash = ownHash;
        startUp();
        listenMC();
        System.out.println("test: uit de thread");
    }

    public Inet4Address getIP()
    {
        return ip;
    }

    public String getName()
    {
        return name;
    }

    public void calcPosition(String newNodeName)
    {
        Integer newHashCode = newNodeName.hashCode();
        Integer newHash = (int) Integer.toUnsignedLong(newHashCode) % 32768;


        if(ownHash<newHash && newHash<nextHash)
        {
            nextHash = newHash;
            //antwoordt met hash en nextHash aan newNode
        }
        else if(prevHash<newHash || (prevHash<(newHash+32768) && newHash<ownHash))
        {
            prevHash = newHash;
        }
        else if(prevHash==nextHash)
        {
            prevHash=newHash;
            nextHash=newHash;
        }
    }

    public void startUp()
    {
        try
        {
            int portMC = 12345; // Multicast Port waarop men gaat sturen
            Inet4Address IPMC = (Inet4Address) Inet4Address.getByName("230.0.0.0");
            // Multicast IP range: 224.0.0.0 - 239.255.255.255
            byte[] msg = (name + " " + ip.getHostAddress()).getBytes(); // naam en adres zijn gescheiden door een spatie

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
        new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    int portMC = 12345;
                    Inet4Address IPMC = (Inet4Address) Inet4Address.getByName("230.0.0.0");
                    MulticastSocket mcSocket;
                    mcSocket = new MulticastSocket(portMC);
                    mcSocket.joinGroup(IPMC);
                    DatagramPacket packet;

                    while(true)
                    {
                        packet = new DatagramPacket(new byte[1024], 1024);
                        System.out.println("Waiting for a  multicast message...");
                        mcSocket.receive(packet);
                        String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());;
                        String[] info = msg.split(" "); // het ontvangen bericht splitsen in woorden gescheiden door een spatie
                        System.out.println("Naam: " + info[0]);
                        System.out.println("IP: " + info[1]);
                    }
                } catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
