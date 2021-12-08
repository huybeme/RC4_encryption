import java.util.Scanner;

public class RC4_main {

    public static void main(String[] args){

        Scanner kb = new Scanner(System.in);

        System.out.println("Enter your plain text: ");
//        String pt = "hide this message";
        String pt = kb.nextLine();

        System.out.println("Enter your key: ");
//        String key = "key";
        String key = kb.nextLine();

        while (key.length() < 2 || key.length() > 40){
            System.out.println("invalid key entry, please enter a key with length between 2 and 40 bytes");
            key = kb.nextLine();
        }

        System.out.println("How many layers of pseudo-random generations?: ");
//        int cycle = 2;
        int cycle =  kb.nextInt();

        while (cycle < 1){
            System.out.println("invalid number of layers, please enter an integer of 1 or greater:");
            cycle = kb.nextInt();
        }


        RC4 encrypt = new RC4(pt, key, cycle);
        encrypt.encrypt();

    }

}