import service.Convert;

import javax.swing.*;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

public class test {

    public static void main(String[] args) {
        String str="1132abc,.-!@#$%^&*()1-31-203/12";
        System.out.println(Convert.TextToBlock(str));
        System.out.println(Convert.BitToText(Convert.TextToBlock(str)));
    }



}
