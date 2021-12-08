public class main {

    public static void main(String[] args){

        // plain text as string, array of decimals, and array of binary
        String pt = "Trying to understand and learn the two's complement is very confusing, especially very late at night.";
        int[] ptDecArr = stringToDecimal(pt);
        String[] ptBin = getBinArr(ptDecArr);

        // key string as string and array of decimals
        String key = "homework";
        int[] keyDecArr = stringToDecimal(key);

        // initialization of s will be values of 0...256 and t is repeated decimal values of key at same length
        int[] s = new int[256];
        int[] t = new int[256];
        for(int i = 0; i < 256; i++){
            s[i] = i;
            t[i] = keyDecArr[i%keyDecArr.length];
        }

        setKSA(s, t);   // permutate array s. permutation is dependent on key

        // determine key stream in binary format    - this is hard coded for 2 total swaps (initial 256 bytes not used)
        int[] keyStream = getPRGA(s);
        String[] keyStreamBin = getBinArr(keyStream);

        // determine cipher text by XOR plain text with key stream
        String[] ctBin = getXOR(ptBin, keyStreamBin);

        // print out different casts of the process
        System.out.println("\n|--------plain text--------|");
        System.out.println("(ASCII)\n" + pt);
        System.out.println("(decimal)");
        printIntArr(ptDecArr);
        System.out.println("(binary)");
        printStrArr(ptBin);
        System.out.println();

        System.out.println("|--------key --------|");
        System.out.println("(ASCII)\n" + key);
        System.out.println("(decimal)");
        printIntArr(keyDecArr);
        System.out.println();

        System.out.println("|--------key stream--------|");
        System.out.println("(ASCII)");
        printBinaryToASCII(keyStreamBin);
        System.out.println("(binary)");
        printStrArr(keyStreamBin);
        System.out.println();

        System.out.println("\n|--------cipher text--------|");
        System.out.println("(ASCII)");
        printBinaryToASCII(ctBin);
        System.out.println("(binary)");
        printStrArr(ctBin);
        System.out.println();

        // decrypt cipher text using key stream
        String[] decryptBin = getXOR(ctBin, keyStreamBin);

        System.out.println("\n|--------decrypted text--------|");
        System.out.println("(ASCII)");
        printBinaryToASCII(decryptBin);
        System.out.println("(binary)");
        printStrArr(decryptBin);
        System.out.println();

        /*      PT = I hope I got this code right.
                    encrypted using https://www.dcode.fr/rc4-cipher with the same key as this program
                    This website uses one cycle of PRGA for its decryption therefore this program will not work
                        unless the numPRGA is set to 1 in getPRGA
         */

        System.out.println("\n|--------test--------|");
        String[] ctTest = {"00011011", "00100001", "11000110", "10101101", "11111011", "00001100", "10000001", "01100111", "01000100", "01011001", "11100110", "10100101", "11000100", "10011101", "11001110", "10011101", "10111101", "11101100", "10111100", "11010011", "00101110", "11000100", "10101110", "00111111", "11111101", "01011111", "10111101", "01101100", "00110011"};
        String[] ctTestXOR = getXOR(ctTest, keyStreamBin);
        System.out.println("cipher text binary and ASCII");
        printStrArr(ctTest);
        printBinaryToASCII(ctTest);
        System.out.println("decrypted text binary and ASCII");
        printStrArr(ctTestXOR);
        printBinaryToASCII(ctTestXOR);
        System.out.println();

        // generated using RC4_main to generate encrypted message using the same key and cycle
        String[] ctTest2 = {"01000010", "00001001", "01000001", "10111011", "01100011", "10111001", "10101000", "10001111", "01111111", "11000001", "10001111", "10111110", "01101101", "11101101", "01110111", "00010010", "01000110", "00100111", "00001000", "01110011"};
        String[] ctTestXOR2 = getXOR(ctTest2, keyStreamBin);
        System.out.println("cipher text binary and ASCII");
        printStrArr(ctTest2);
        printBinaryToASCII(ctTest2);
        System.out.println("decrypted text binary and ASCII");
        printStrArr(ctTestXOR2);
        printBinaryToASCII(ctTestXOR2);

    }// end of main

    public static void printBinaryToASCII(String[] binArr){
        for(int i = 0; i < binArr.length; i++){
            System.out.print((char)Integer.parseInt(binArr[i], 2));
        }
        System.out.println();
    }

    public static String[] getXOR(String[] text, String[] keyStream){

        String[] xorText = new String[text.length];
        for (int i = 0; i < text.length; i++){

            String temp = "";
            for (int j = 0; j < text[i].length(); j++){
                if (text[i].charAt(j) == keyStream[i].charAt(j)){
                    temp = temp + 0;
                }
                else {
                    temp = temp + 1;
                }
            }// end of inner for

            xorText[i] = temp;
        }// end of outer for
        return xorText;
    }

    public static int[] getPRGA(int[] s){
        /*  PRGA = Pseudo-Random Generation Algorithm
                Modifies the state and outputs a byte of the key stream in a pseudo
                random order.
         */

        int i = 0, j = 0;
        int count = 0, numPRGA = 1;         // <---- change numPRGA to 1 to decrypt the test
        int[] keyStream = new int[s.length];

        while (count < (256 * numPRGA)){
            i = (i + 1) % 256;
            j = (j + s[i]) % 256;

            int temp = s[i];
            s[i] = s[j];
            s[j] = temp;

            int t = (s[i] + s[j]) % 256;
            keyStream[count % 256] = s[t];
            count++;
        }
        return keyStream;
    }

    public static void setKSA(int[] s, int[] t){
        /*  KSA = Key Scheduling algorithm
                Initialize a permutation of an array with key length between 0 and 255
         */

        int j = 0;
        for (int i = 0; i < s.length; i++){
            j = (j + s[i] + t[i]) % s.length;

            int temp = s[i];
            s[i] = s[j];
            s[j] = temp;
        }
    }

    public static String[] getBinArr(int[] intArr){
        String[] binArr = new String[intArr.length];

        for(int i = 0; i < intArr.length; i++){
            binArr[i] = String.format("%8s", Integer.toBinaryString(intArr[i])).replace(" ", "0");
        }

        return binArr;
    }

    public static int[] stringToDecimal(String text){
        int[] decArr = new int[text.length()];
        for(int i = 0; i < text.length(); i++){
            decArr[i] = text.charAt(i);
        }
        return decArr;
    }

    public static void printIntArr(int[] arr){
        for (int i = 0; i < arr.length; i++){
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static void printStrArr(String[] arr){
        for (int i = 0; i < arr.length; i++){
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static void testPrint(String str){

        for(int i = 0; i < str.length(); i++){
            byte b = (byte) str.charAt(i);
            int bInt = b & 0xff;
            String temp = String.format("%8s", Integer.toBinaryString(bInt)).replace(" ", "0");
            System.out.println(str.charAt(i) + " " + temp + " " + (int)str.charAt(i));
        }
        System.out.println();
    }
}