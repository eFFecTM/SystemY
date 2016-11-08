import java.util.Scanner;

/**
 * Created by jonas on 8-11-2016.
 */
public class HASHCALDTEST
{
    public static void main(String[] args)
    {
        Scanner s = new Scanner(System.in);
        String str = s.nextLine();
        int hash = calcHash(str);
        System.out.println(hash);
    }
    public static int calcHash(String name)
    {
        return Math.abs(name.hashCode()%32768);
    }
}
