/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 *
 * @author me
 */
public class GenerateBaseFileAsAutoSegments {

    static String wordBegin = "آأإابتثجحخدذرزسشصضطظعغفقكلمنهوي";
    static String wordMiddle = "ءآأؤإئابتثجحخدذرزسشصضطظعغفقكلمنهوي";
    static String wordEnd = "ءأؤابتثجحخدذرزسشصضطظعغفقكلمنهويى";    

    public static void main(String[] args) throws Exception {

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(Config.BASE_WORDS_FILE), "UTF8"));

        //Generate 3 characters words
        GenerateThreeCharWords(bw);

        //Generate 4 characters words
        //GenerateFourCharWords(bw);
    }

    private static void GenerateThreeCharWords(BufferedWriter bw) throws Exception {
        for (int i1 = 0; i1 < wordBegin.length(); i1++) {
            for (int i2 = 0; i2 < wordMiddle.length(); i2++) {
                for (int i3 = 0; i3 < wordEnd.length(); i3++) {
                    String result = String.valueOf(wordBegin.charAt(i1)) + String.valueOf(wordMiddle.charAt(i2)) + String.valueOf(wordEnd.charAt(i3));
                    bw.write(result + "\n");
                }
            }
        }
    }

    private static void GenerateFourCharWords(BufferedWriter bw) throws Exception {
        for (int i1 = 0; i1 < wordBegin.length(); i1++) {
            for (int i2 = 0; i2 < wordMiddle.length(); i2++) {
                for (int i3 = 0; i3 < wordMiddle.length(); i3++) {
                    for (int i4 = 0; i4 < wordEnd.length(); i4++) {

                        String result = String.valueOf(wordBegin.charAt(i1)) + String.valueOf(wordMiddle.charAt(i2)) + String.valueOf(wordMiddle.charAt(i3)) + String.valueOf(wordEnd.charAt(i4));
                        bw.write(result + "\n");
                    }
                }
            }
        }
    }
}
