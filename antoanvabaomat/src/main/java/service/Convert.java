package service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;

public class Convert {

    public static String BitToText(String block){
        int cnt=0;
        for(int i=0;i<block.length()/8;i++){
            String t=block.substring(i*8,i*8+8);
            int bit=Integer.parseInt(t);
            if(bit==0){
                cnt++;
            }
        }
        block=block.substring(cnt*8);
        int binaryStringLength = block.length();
        int numBytes = binaryStringLength / 8;
        byte[] byteArray = new byte[numBytes];
        for (int i = 0; i < numBytes; i++) {
            String binaryByte = block.substring(i * 8, (i + 1) * 8);
            int decimalValue = Integer.parseInt(binaryByte, 2);
            byteArray[i] = (byte) decimalValue;
        }
        return new String(byteArray,StandardCharsets.UTF_8);
    }
    public static String TextToBlock(String text){
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        StringBuilder binaryString = new StringBuilder();
        for (byte b : bytes) {
            String binary = Integer.toBinaryString(b & 0xFF); // Chuyển đổi byte thành chuỗi nhị phân
            // Đảm bảo chuỗi nhị phân có 8 ký tự bằng cách thêm các số 0 phía trước nếu cần
            while (binary.length() < 8) {
                binary = "0" + binary;
            }
            binaryString.append(binary);
        }
        String str=binaryString.toString();
        while(str.length()%64!=0){
           str="0"+str;
        }
      return str;
    }
    public static String BitSetToBit(BitSet bitSet) {
        String str="";
        for(int i=0;i<64;i++){
            if(bitSet.get(i)){
                str+="1";
            }
            else{
                str+="0";
            }
        }
        return str;
    }
    public static BitSet BitToBitSet(String binaryString) {
        BitSet bitSet = new BitSet(binaryString.length());
        for (int i = 0; i < binaryString.length(); i++) {
            char bitChar = binaryString.charAt(i);
            if (bitChar == '1') {
                bitSet.set(i);
            }
        }
        return bitSet;
    }

}