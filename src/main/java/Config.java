/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author me
 */
public class Config {

    
    //******************************************************************************************
    //Run Mode
    //RotateThroughFonts iterates over all fonts and for each font generate samples using all the words in the base file
    //RotateThroughSamples iterates over number of words defined in TOTAL_SAMPLES below and for each word sample select the next font from fonts iterator    
    public static DatasetGenerateModeEnum DATASET_GENERATE_MODE = DatasetGenerateModeEnum.RotateThroughFonts;
    
    //The generated sampels can be a single PNG file for each word sample, or a singl binary file containing all samples
    //or both png files and single binary file
    public static ImageSaveMethodEnum IMAGE_SAVE_METHOD = ImageSaveMethodEnum.SaveAsSingleBinaryFile;
    
    //Different settings for sizing the generated images
    public static ImageSizeOperationEnum IMAGE_SIZE_OPERATION = ImageSizeOperationEnum.SetToOriginalImageSize;
    
    
    //used to limit the total number of generated samples     
    //specify a number of set to 0 or -1 to process all samples in base file
    //use in DatasetGenerator.RotateThroughSamples()
    public static int TOTAL_SAMPLES = 120000;

    //used to limit the number of generated samples per font    
    //specify a number of set to 0 or -1 to process all samples in base file
    //use in DatasetGenerator.RotateThroughFonts()
    public static int SAMPLES_PER_FONT_LIMIT = 0;
    //******************************************************************************************

    public static int FIXED_IMG_SIZE_HEIGHT = 32;
    public static int FIXED_IMG_SIZE_WIDTH = 128;

    public static int FONT_SIZE = 26;
    public static boolean FONT_BOLD = true;
    public static boolean FONT_ITALIC = false;
    public static boolean RANDOMIZE_FONT_STYLE = true; //for ResizeToFitFixedBoundary only
    
    public static String BASE_PATH = "D://Mywork//HTR//Data//JavaOutput/";
    public static String LABELS_PATH = BASE_PATH + "labels//";
    public static String IMAGES_PATH = BASE_PATH + "images//";

    public static String BASE_WORDS_FILE = BASE_PATH + "words_with_duplicates.txt";
    public static String LABELS_BINARY_FILE = LABELS_PATH + "labels_binary.txt";
    public static String LABELS_PNG_FILE = LABELS_PATH + "labels_png.txt";
    public static String BINARY_IMAGES_FILE = IMAGES_PATH + "images.bin";

    public enum ImageSizeOperationEnum {
        SetToOriginalImageSize,
        SetToLargestImageSize,
        ResizeNoStrectch,
        ResizeWithStretchAndAspectRatio,
        ResizeWithStretchWithoutAspectRatio,
        ResizeToFitFixedBoundary
    }

    public enum ImageSaveMethodEnum {
        SaveAsPNGFIles,
        SaveAsSingleBinaryFile,
        SaveBothPNGandBinary
    }
    
       public enum DatasetGenerateModeEnum {
        RotateThroughSamples,
        RotateThroughFonts        
    }
}
