package JJTP_DS_UA;

import java.util.Scanner;

/**
 * Created by JJTP on 31/10/2016.
 */
public class Main_node
{
    public static void main(String[] args)
    {
        Node node = new Node(textInput(), textInput());
    }

    public static String textInput()
    {
        System.out.println("Choose a name for the node and press enter, fill in the correct ip-address and press enter.");
        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }

}
