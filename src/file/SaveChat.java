package file;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class SaveChat {
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public SaveChat(String fileName){
        this.fileName = fileName;
    }

    public void saveInfo(String text) throws FileNotFoundException {
        try (
                // Create a file
                java.io.PrintWriter output = new java.io.PrintWriter(new FileOutputStream(fileName+".txt",true));
        ) {
            //write texts in file
            output.print(text);
        }
    }
}
