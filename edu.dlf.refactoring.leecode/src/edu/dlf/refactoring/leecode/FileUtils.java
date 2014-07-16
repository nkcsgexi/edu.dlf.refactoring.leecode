package edu.dlf.refactoring.leecode;

import java.io.File;

public class FileUtils {
	
	public static void main(String[] args) {
        String directory = "C:\\vsworkspace\\navigationtool\\CodeNavigation\\Resources\\feedIcons";
		File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                File f = new File(directory +"\\"+ listOfFiles[i].getName()); 
                f.renameTo(new File(directory + "\\animal" +i+".ico"));
            }
        }

	}
}
