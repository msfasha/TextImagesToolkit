/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


public class Main {

    public static void main(String[] args) throws Exception {

        //Clear output directory from previously created files
        DirectoryHelper.DeleteDirectoryContent(Config.IMAGES_PATH);
        DirectoryHelper.DeleteDirectoryContent(Config.LABELS_PATH);

        //Use for PredefinedImageResize and PredefinedImageResizeWithStretch operations
        ImageSize fixedSize = new ImageSize();
        fixedSize.Width = Config.FIXED_IMG_SIZE_WIDTH;
        fixedSize.Height = Config.FIXED_IMG_SIZE_HEIGHT;

        DatasetGenerator datasetGenerator = new DatasetGenerator(
                Config.IMAGE_SAVE_METHOD, Config.IMAGE_SIZE_OPERATION, fixedSize, true);

        if (Config.DATASET_GENERATE_MODE == Config.DatasetGenerateModeEnum.RotateThroughFonts)
        {
            //make sure to the SAMPLES_PER_FONT_LIMIT value
            datasetGenerator.RotateThroughFonts();
        } else
        {
            //make sure to the TOTAL_SAMPLES value
            datasetGenerator.RotateThroughSamples();
        }

    }// main function        
}//class
