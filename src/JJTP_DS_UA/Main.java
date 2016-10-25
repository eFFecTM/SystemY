package JJTP_DS_UA;

public class Main
{

    public static void main(String[] args)
    {
        // Hello world!
        String string = "Jönas is nen heumö";
        Integer hash  = string.hashCode();
        long test = Integer.toUnsignedLong(hash);
        System.out.println(test % 32768);
    }
}
// jonas aanpas test : thomas is homo
// thomas aanpas test: jonas is een grotere homo
// Peter aanpas test : Allemaal homo's