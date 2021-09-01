/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author me
 */
public class FontHelper {

    Map<String, String> fontsDictionary;

    public void FontHelper() {
    }

    public List<Font> GetAllFonts(int fontSize, boolean boldFont, boolean italicFont) throws IOException, FontFormatException {

        File dir = new File("../fonts/");

        File[] files = dir.listFiles();

        List<Font> fontsList = new ArrayList<>();
        Font font = null;
        
        for (File file : files) {

            if (!file.isDirectory()) {
                try {
                    font = CreateFont(file.getCanonicalPath(), fontSize, boldFont, italicFont);

                    System.out.println("Created font : " + file);
                    fontsList.add(font);
                } catch (Exception ex) {
                    System.out.println("Failed font creation " + font.getFontName() + "\n" + ex.getMessage());
                }
            }
        }

        return fontsList;
    }

    public Font GetFont(String fileName, int fontSize, boolean boldFont, boolean italicFont) throws IOException, FontFormatException {

        return CreateFont("./fonts/" + fileName, fontSize, boldFont, italicFont);

    }

    public Font CreateFont(String fontFileName,
            int fontSize, boolean boldFont, boolean italicFont) throws IOException, FontFormatException {

        Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fontFileName)).deriveFont(12f);

        if (boldFont) {
            font = font.deriveFont(Font.BOLD, fontSize);
        }

        if (italicFont) {
            font = font.deriveFont(Font.ITALIC, fontSize);
        }

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //register the font
        ge.registerFont(font);

        return font;

    }
}
