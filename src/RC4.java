public class RC4 {

    public String plainText, key;
    public int cycle;
    public String[] ctBin;

    public RC4(String pt, String k, int c){
        this.plainText = pt;
        this.key = k;
        this.cycle = c;
    }

    public RC4(String[] ctBin, String k){
        this.ctBin = ctBin;
        this.key = k;
    }

    public void decrypt(String[] ct, String key){
        int[] keyDecArr = stringToDecimal(key);
        ;

        int[] sBox = new int[256];
        int[] tStream = new int[256];
        for(int i = 0; i < 256; i++){
            sBox[i] = i;
            tStream[i] = keyDecArr[i% keyDecArr.length];
        }

        setKSA(sBox, tStream);

        int[] keyStreamDec = getPRGA(sBox);
        String[] keyStreamBinArr = getBinArr(keyStreamDec);
        printStrArr(keyStreamBinArr);

    }

    public String encrypt(){
        int[] keyDecArr = stringToDecimal(key);

        int[] sBox = new int[256];
        int[] tStream = new int[256];
        for(int i = 0; i < 256; i++){
            sBox[i] = i;
            tStream[i] = keyDecArr[i% keyDecArr.length];
        }

        setKSA(sBox, tStream);

        int[] keyStreamDec = getPRGA(sBox);
        String[] keyStreamBinArr = getBinArr(keyStreamDec);

        int[] ptDecArr = stringToDecimal(plainText);
        String[] ptBinArr = getBinArr(ptDecArr);
        String[] ctBinArr = getXOR(ptBinArr, keyStreamBinArr);
        String cipherText = getBinaryToASCII(ctBinArr);

        System.out.println("your cipher text (ASCII): ");
        System.out.println(cipherText);
        System.out.println("your cipher text (Binary): ");
        printStrArr(ctBinArr);

        return getBinaryToASCII(ctBinArr);
    } // end of encrypt

    private String getBinaryToASCII(String[] binArr){
        String str = "";
        for(int i = 0; i < binArr.length; i++){
//            System.out.print((char)Integer.parseInt(binArr[i], 2));
            str = str + (char)Integer.parseInt(binArr[i], 2);
        }
        return str;
    }

    private String[] getXOR(String[] text, String[] keyStream){

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

    private String[] getBinArr(int[] intArr){
        String[] binArr = new String[intArr.length];

        for(int i = 0; i < intArr.length; i++){
            binArr[i] = String.format("%8s", Integer.toBinaryString(intArr[i])).replace(" ", "0");
        }

        return binArr;
    }

    private void printStrArr(String[] arr){
        for(int i = 0; i < arr.length; i++){
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    private int[] getPRGA(int[] s){
        int i = 0, j = 0;
        int count = 0, numPRGA = 2;
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

    private void setKSA(int[] s, int[] t){
        int j = 0;
        for (int i = 0; i < s.length; i++){
            j = (j + s[i] + t[i]) % s.length;

            int temp = s[i];
            s[i] = s[j];
            s[j] = temp;
        }
    }

    private int[] stringToDecimal(String text){
        int[] decArr = new int[text.length()];
        for(int i = 0; i < text.length(); i++){
            decArr[i] = text.charAt(i);
        }
        return decArr;
    }

}
