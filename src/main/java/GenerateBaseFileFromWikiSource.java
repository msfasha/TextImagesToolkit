/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

//Create words base file, which is just a selected set of words from the origial text files
//filtered to a specific number of words with specific length
class MainModule {

   class UnicodeMapping {

       int beginRange;
       int endRange;
       int newValue;

       public UnicodeMapping(int paraBeginRange, int paraEndRange, int paraNewValue) {
           beginRange = paraBeginRange;
           endRange = paraEndRange;
           newValue = paraNewValue;
       }
   }

   private List<UnicodeMapping> unicodeMappingList;

   public MainModule() {
       unicodeMappingList = new ArrayList<>();

       unicodeMappingList.add(new UnicodeMapping(0x063B, 0x063C, 0x643));//kaaf
       unicodeMappingList.add(new UnicodeMapping(0x63D, 0x63F, 0x649));//alef maqsoura
       unicodeMappingList.add(new UnicodeMapping(0x0671, 0x0673, 0x627));//alef
       unicodeMappingList.add(new UnicodeMapping(0x0675, 0x0675, 0x0627));//
       unicodeMappingList.add(new UnicodeMapping(0x0676, 0x0677, 0x0648));//
       unicodeMappingList.add(new UnicodeMapping(0x0678, 0x0678, 0x0649));//
       unicodeMappingList.add(new UnicodeMapping(0x0679, 0x067A, 0x062A));//
       unicodeMappingList.add(new UnicodeMapping(0x067C, 0x067C, 0x062A));//taa
       unicodeMappingList.add(new UnicodeMapping(0x067D, 0x067D, 0x062A));//thaa
       unicodeMappingList.add(new UnicodeMapping(0x067E, 0x067E, 0x0628));//baa
       unicodeMappingList.add(new UnicodeMapping(0x0681, 0x0687, 0x062D));//
       unicodeMappingList.add(new UnicodeMapping(0x0688, 0x0690, 0x026F));//
       unicodeMappingList.add(new UnicodeMapping(0x0691, 0x0697, 0x0631));// reh
       unicodeMappingList.add(new UnicodeMapping(0x0698, 0x0698, 0x0632));// zain
       unicodeMappingList.add(new UnicodeMapping(0x0699, 0x0699, 0x0631));// reh
       unicodeMappingList.add(new UnicodeMapping(0x06A0, 0x06A0, 0x0639));//
       unicodeMappingList.add(new UnicodeMapping(0x06A1, 0x06A8, 0x0641));//
       unicodeMappingList.add(new UnicodeMapping(0x06A9, 0x06B4, 0x0643));//
       unicodeMappingList.add(new UnicodeMapping(0x06B5, 0x06B8, 0x0644));//
       unicodeMappingList.add(new UnicodeMapping(0x06B9, 0x06BD, 0x0646));//
       unicodeMappingList.add(new UnicodeMapping(0x06C0, 0x06C3, 0x0647));//
       unicodeMappingList.add(new UnicodeMapping(0x06C4, 0x06CA, 0x0648));//
       unicodeMappingList.add(new UnicodeMapping(0x06CC, 0x06CE, 0x0649));//
   }

   public void Run() throws UnsupportedEncodingException, FileNotFoundException, IOException {
       BufferedWriter bw;
       int counter;
       try (BufferedReader br = new BufferedReader(
               new InputStreamReader(
                       new FileInputStream(Config.WIKI_WORDS_FILE), "UTF8"))) {
           bw = new BufferedWriter(new OutputStreamWriter(
                   new FileOutputStream(Config.BASE_WORDS_FILE), "UTF8"));

           String word, line = null;
           int numberOfWordsInCurrentLine = 0;
           counter = 0;
           Set<String> hashSet = new HashSet<String>();
              
           while ((word = br.readLine()) != null) {
               if ((word.length() >= Config.MIN_WORD_LENGTH) && (word.length() <= Config.MAX_WORD_LENGTH_BEFORE_AUGMENTATION)) {

                   if (counter == Config.TOTAL_NUMBER_OF_SAMPLES) {
                       break;
                   }

                   if (hashSet.contains(word)) {
                       continue;
                   }

                   if (Config.CONVERT_EXTENDED_ARABIC_TO_BASE_ARABIC) {
                       word = ConvertExtendedCharToBaseChar(word);
                   }

                   //the method above already converts extended Arabic to Base Arabic, nevertheless
                   //the method below removes everything other than the Alphabets, it removes lating, numbers...etc
                   if (Config.LIMIT_TO_ARABIC_BASE_ALPHABET) {
                       if (!IsValidArabicCharacters(word)) {
                           continue;
                       }
                   }

                   hashSet.add(word);
                   if (Config.AUGMENT_WORD) {
                       word = AugmentWord(word);
                   }

                   if (numberOfWordsInCurrentLine < Config.NUMBER_OF_WORDS_IN_SAMPLE) {
                       if (line == null) {
                           line = word;
                       } else {
                           line = line + " " + word;
                       }
                       numberOfWordsInCurrentLine++;
                   } else {
                       numberOfWordsInCurrentLine = 0;
                       bw.write(line + "\n");
                       line = null;
                       counter++;
                   }
               }
           }
       }
       bw.close();

       System.out.printf("A total of  %d Arabic words extracted", counter);
   }

   public boolean IsValidArabicCharacters(String word) {

       boolean validCharacters = true;

       for (int i = 0; i < word.length(); i++) {
           int c = word.codePointAt(i);

           if (!(c >= 0x0620 && c <= 0x063A) && !(c >= 0x0640 && c <= 0x064A)) {
               validCharacters = false;
               break;
           }
       }
       return validCharacters;
   }

   public String AugmentWord(String word) {

       String characterGroup1 = "ئبتثجحخسشصضطظعغـفقكلمنهىيپچڤکگیێ";
       String characterGroup2 = "ءآأؤإاةدذرزوژێ";

       String newWord = "";
       Dictionary<Integer, String> elagonationDictionay = new Hashtable<Integer, String>();
       Dictionary<Integer, String> spaceDictionay = new Hashtable<Integer, String>();
       int randomValue = 0;
       Random rand = new Random();
       boolean spaceSet = false;

       for (int i = 0; i < word.length() - 1; i++) {//loop until before last character

           char c = word.charAt(i);
           if (characterGroup1.indexOf(c) >= 0) {
               randomValue = rand.nextInt(10);
               if (randomValue < 7)//randoly check if we hould use random
               {
                   randomValue = rand.nextInt(2);
                   elagonationDictionay.put(i, "ـ".repeat(randomValue));
               }
           }

           if (!spaceSet) {
               if (characterGroup2.indexOf(c) >= 0) {
                   randomValue = rand.nextInt(10);
                   if (randomValue < 7)//randoly check if we hould use random
                   {
                       randomValue = rand.nextInt(2);
                       spaceDictionay.put(i, " ".repeat(randomValue));
                       spaceSet = true; //to limit augmentation to one space only
                   }
               }
           }
       }

       for (int i = 0; i < word.length(); i++) {
           newWord = newWord + word.charAt(i);

           if (!elagonationDictionay.isEmpty()) {
               String value = elagonationDictionay.get(i);
               if (!(value == null)) {
                   if ((newWord.length() + value.length() + (word.length() - (i + 1))) <= Config.MAX_ALLOWED_WORD_LENGTH_AFTER_AUGMENTATION) {
                       newWord = newWord + value;
                   }
               }
           }

           if (!spaceDictionay.isEmpty()) {
               String value = spaceDictionay.get(i);
               if (!(value == null)) {
                   if ((newWord.length() + value.length() + (word.length() - (i + 1))) <= Config.MAX_ALLOWED_WORD_LENGTH_AFTER_AUGMENTATION) {
                       newWord = newWord + value;
                   }
               }
           }
       }

       return newWord;
   }//AugmentWord

   public String ConvertExtendedCharToBaseChar(String paraWord) {

       int newWord[] = new int[paraWord.length()];

       for (int i = 0; i < paraWord.length(); i++) {

           int c = paraWord.codePointAt(i);
           Iterator iter = unicodeMappingList.iterator();

           while (iter.hasNext()) {
               UnicodeMapping unicodeMapping = (UnicodeMapping) iter.next();

               if (c >= unicodeMapping.beginRange && c <= unicodeMapping.endRange) {
                   c = unicodeMapping.newValue;
                   break;
               }
           }

           newWord[i] = c;
       }

       return new String(newWord, 0, newWord.length);
   }
}

public class GenerateBaseFileFromWikiSource {

   public static void main(String[] args) throws Exception {

       MainModule mainModule = new MainModule();
       mainModule.Run();
   }
}
