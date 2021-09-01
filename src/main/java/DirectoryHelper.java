/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.io.IOException;

/**
 *
 * @author me
 */
public class DirectoryHelper {

    public static void DeleteDirectoryContent(String pathStr) throws IOException {
        System.out.println("Deleteing directories content...");
        
        File file = new File(pathStr);
        String[] myFiles;
        if (file.isDirectory()) {
            myFiles = file.list();
            for (String myFile1 : myFiles) {
                File myFile = new File(file, myFile1);
                myFile.delete();
            }
        }
    }
}
