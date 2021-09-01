/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author me
 */
public class DatasetGenerator {

    //class level variable, need to retain its value when creating binary images
    //for different loops of fonts
    long imageOutputStreamPosition = 0;
    int samplesCounter = 0;
    int perFontCounter = 0;

    //open labels file for write-append
    BufferedWriter binaryImagesLabelsFile = null;
    BufferedWriter pngLabelsFile = null;
    OutputStream imageOutputStream = null;//used to write in binary file/if needed        

    ImageHelper imageHelper = new ImageHelper();

    ImageSize largestImageSize = null;
    ImageSize specificSize = null;

    Config.ImageSaveMethodEnum imageSaveMethod;
    Config.ImageSizeOperationEnum imageSizeOperation;

    public DatasetGenerator(Config.ImageSaveMethodEnum paraImageSaveMethod, Config.ImageSizeOperationEnum paraImageSizeOperation, ImageSize paraSpecificSize, boolean paraAppend) throws UnsupportedEncodingException, FileNotFoundException, IOException, FontFormatException {

        this.imageSaveMethod = paraImageSaveMethod;
        this.imageSizeOperation = paraImageSizeOperation;

        switch (paraImageSizeOperation) {
            case SetToLargestImageSize:
                //compute largest image size in all samples, we need to compute it only once
                FontHelper fontHelper = new FontHelper();
                List<Font> allFontsList = fontHelper.GetAllFonts(Config.FONT_SIZE, Config.FONT_BOLD, Config.FONT_ITALIC);
                largestImageSize = imageHelper.ComputerMaxTextImageSizeAllFonts(Config.BASE_WORDS_FILE, allFontsList);
                break;
            case ResizeNoStrectch:
            case ResizeWithStretchAndAspectRatio:
            case ResizeWithStretchWithoutAspectRatio:
            case ResizeToFitFixedBoundary:

                if (paraSpecificSize == null) {
                    System.out.println("Required image size must be provided...");
                    return;
                }

                this.specificSize = paraSpecificSize;
                break;
        }

        PrepareOutputFiles(paraImageSaveMethod, paraAppend);
    }

    public void PrepareOutputFiles(Config.ImageSaveMethodEnum paraImageSaveMethod, boolean paraAppend) throws UnsupportedEncodingException, FileNotFoundException {
        switch (paraImageSaveMethod) {
            case SaveAsPNGFIles:
                pngLabelsFile = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(Config.LABELS_PNG_FILE, paraAppend), "UTF8"));
                break;

            case SaveAsSingleBinaryFile:
                binaryImagesLabelsFile = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(Config.LABELS_BINARY_FILE, paraAppend), "UTF8"));

                imageOutputStream = new FileOutputStream(Config.BINARY_IMAGES_FILE, paraAppend);

                break;

            case SaveBothPNGandBinary:
                pngLabelsFile = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(Config.LABELS_PNG_FILE, paraAppend), "UTF8"));

                binaryImagesLabelsFile = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(Config.LABELS_BINARY_FILE, paraAppend), "UTF8"));

                imageOutputStream = new FileOutputStream(Config.BINARY_IMAGES_FILE, paraAppend);

                break;
        }
    }

    public void RotateThroughSamples() throws IOException, FontFormatException {
        String word;

        FontHelper fontHelper = new FontHelper();
        List<Font> allFontsList = fontHelper.GetAllFonts(Config.FONT_SIZE, Config.FONT_BOLD, Config.FONT_ITALIC);
        Iterator<Font> fontsIterator = allFontsList.iterator();

        //open base words file for reading
        samplesCounter = 0;
        perFontCounter = 0;

        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(Config.BASE_WORDS_FILE), "UTF8"))) {

            Font font = null;

            while ((word = bufferedReader.readLine()) != null) {

                if (Config.TOTAL_SAMPLES > 0) {
                    if (samplesCounter >= Config.TOTAL_SAMPLES) {
                        break;
                    }
                }

                if (!fontsIterator.hasNext()) {
                    fontsIterator = allFontsList.iterator();
                }

                font = fontsIterator.next();

                CreateImages(font, word);

                samplesCounter++;
                perFontCounter++;

            }//end while loop
        }//BufferenReader
    }//RotateThroughSamples

    public void RotateThroughFonts() throws IOException, FontFormatException {

        FontHelper fontHelper = new FontHelper();
        List<Font> allFontsList = fontHelper.GetAllFonts(Config.FONT_SIZE, Config.FONT_BOLD, Config.FONT_ITALIC);

        //Create single Binary
        samplesCounter = 0;
        for (Font font : allFontsList) {
            perFontCounter = 0;
            String word;

            //open base words file for reading
            try (BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(Config.BASE_WORDS_FILE), "UTF8"))) {

                //loop through all words
                while ((word = bufferedReader.readLine()) != null) {

                    //check if SAMPLES_LIMIT_PER_FONT > 0 then counter is set
                    //therefore limit the number of generated images to the specified number
                    //otherwise skip it an process all samples
                    if (Config.SAMPLES_PER_FONT_LIMIT > 0) {
                        if (perFontCounter >= Config.SAMPLES_PER_FONT_LIMIT) {
                            break;
                        }
                    }

                    CreateImages(font, word);

                    samplesCounter++;
                    perFontCounter++;
                } //end while loop
            }
        } // for loop  

        System.gc();
    }

    public void CreateImages(Font font, String paraWord)
            throws IOException, FontFormatException {
        //This procedure can create files as single PNG files or as byte records in a single binary file
        //The ImageSaveMethod parameter determines the image save method
        //For each type of operation a label file is generated
        //The procedure can create files for different fonts, the fonts that are lsited in the 
        //designated fonts directory in GLOBL.FONTS_PATH
        //The procedure can create images with different size settings:
        //1- SetToOriginalImageSize, the image is created with a size according to the fonts details and the selected words.
        //2-ResizeNoStrectch, the image can be inserted into a fixed size image, therefore parts of the words might get clipped.
        //3-ResizeWithStretchAndAspectRatio, the image is created according to a fixed size, only the width is maintained.
        //as the image height is adjusted according to the ascpect ratio (to maintain image credebility)
        //4-ReizeWithStretchWithoutAspectRatio, after creating the image according to its orginal size, it is stretched
        //without maintaining the aspect ratio, therefore the shape of the text in the image might change i.e. thicker text according to text length and fixed image size
        //5-SetToLargestImageSize, the max size of all images/words is calculated under all font types, and that size is used to create all images

        String word = paraWord;
        BufferedImage image = null;
        ImageSize originalImageSize;

        switch (imageSizeOperation) {
            case SetToOriginalImageSize:
                originalImageSize = imageHelper.ComputeTextImageSizeSingleFont(word, font);
                image = imageHelper.CreateImageFromTextString(word, font, originalImageSize.Width, originalImageSize.Height);
                break;
            case SetToLargestImageSize:
                //Largest image size is computed above - out of loop section
                image = imageHelper.CreateImageFromTextString(word, font, largestImageSize.Width, largestImageSize.Height);
                break;
            case ResizeNoStrectch:
                image = imageHelper.CreateImageFromTextString(word, font, specificSize.Width, specificSize.Height);
                break;

            case ResizeWithStretchWithoutAspectRatio:
                originalImageSize = imageHelper.ComputeTextImageSizeSingleFont(word, font);
                image = imageHelper.CreateImageFromTextString(word, font, originalImageSize.Width, originalImageSize.Height);
                image = imageHelper.ResizeImage(image, specificSize.Width, specificSize.Height);
                break;

            case ResizeWithStretchAndAspectRatio:
                originalImageSize = imageHelper.ComputeTextImageSizeSingleFont(word, font);
                image = imageHelper.CreateImageFromTextString(word, font, originalImageSize.Width, originalImageSize.Height);

                //maintain aspect ratio
                double aspectRatio = (double) image.getWidth(null) / (double) image.getHeight(null);

                image = imageHelper.ResizeImage(image, specificSize.Width, (int) (100 / aspectRatio));
                break;

            case ResizeToFitFixedBoundary:
                //Set to bold and italic randomly
                if (Config.RANDOMIZE_FONT_STYLE) {
                    if (Math.random() < 0.5) {
                        font = font.deriveFont(Font.BOLD);
                    } else {
                        font = font.deriveFont(Font.PLAIN);
                    }
                    if (Math.random() < 0.5) {
                        font = font.deriveFont(font.getStyle() | Font.ITALIC);
                    }
                }

                int fontSize = imageHelper.getMaxFittingFontSize(word, font, specificSize.Width, specificSize.Height);
                font = font.deriveFont(font.getStyle(), fontSize);

                image = imageHelper.CreateImageFromTextString(word, font, specificSize.Width, specificSize.Height);
                break;
        }

        switch (imageSaveMethod) {
            case SaveAsPNGFIles:
                //Save image as PNG
                SavePNGFile(word, font, image, imageHelper, pngLabelsFile);
                break;

            case SaveAsSingleBinaryFile:
                //Save image into binary file
                SaveBinaryImageRecord(word, font, image, binaryImagesLabelsFile, imageOutputStream);
                break;

            case SaveBothPNGandBinary:
                SavePNGFile(word, font, image, imageHelper, pngLabelsFile);
                SaveBinaryImageRecord(word, font, image, binaryImagesLabelsFile, imageOutputStream);
                break;
        }
    } //CreateImages

    private void SavePNGFile(String word, Font font, BufferedImage image, ImageHelper imageHelper, BufferedWriter bw) throws IOException {
        String imageName = font.getName() + "_" + perFontCounter + ".png";
        imageHelper.SaveImageAsPNGFile(image, Config.IMAGES_PATH + imageName);

        String saveString = imageName + ";";
        saveString = saveString + "image height:" + image.getHeight() + ";";
        saveString = saveString + "image width:" + image.getWidth() + ";";
        saveString = saveString + "font name:" + font.getFontName() + ";";
        saveString = saveString + "font size:" + font.getSize() + ";";
        saveString = saveString + "bold:" + font.isBold() + ";";
        saveString = saveString + "italic:" + font.isItalic() + ";";
        saveString = saveString + "word:" + word + "\n";

        bw.write(saveString);
        bw.flush();
    }

    private void SaveBinaryImageRecord(String word, Font font, BufferedImage image, BufferedWriter bw, OutputStream imageOutputStream) throws IOException {
        WritableRaster raster = image.getRaster();
        DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
        imageOutputStream.write(data.getData());
        imageOutputStream.flush();

        String saveString = "image idx:" + samplesCounter + ";";
        saveString = saveString + "start position:" + imageOutputStreamPosition + ";";
        saveString = saveString + "image height:" + image.getHeight() + ";";
        saveString = saveString + "image width:" + image.getWidth() + ";";
        saveString = saveString + "font name:" + font.getFontName() + ";";
        saveString = saveString + "font size:" + font.getSize() + ";";
        saveString = saveString + "bold:" + font.isBold() + ";";
        saveString = saveString + "italic:" + font.isItalic() + ";";
        saveString = saveString + "word:" + word + "\n";

        bw.write(saveString);
        bw.flush();

        imageOutputStreamPosition += data.getData().length;
    }
} //class Orchestrator
