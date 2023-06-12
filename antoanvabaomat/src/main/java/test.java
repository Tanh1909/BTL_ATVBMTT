import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import service.Convert;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

public class test {

    public static void main(String[] args) {
        String str="đây là tiếng nói việt nam";

        String hex=Convert.binaryToHex(Convert.TextToBlock(str));
        System.out.println(hex);
        String bin=Convert.hexToBinary(hex);
        System.out.println(Convert.BitToText(bin));

    }



}
