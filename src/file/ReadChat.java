package file;

import javafx.scene.control.TextArea;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadChat {

    public void readInfo(TextArea textArea , String fileName) throws FileNotFoundException {
        java.io.File file = new java.io.File(fileName+".txt");

        if(file.exists()) {
            try (
                    // Create a Scanner for the file
                    Scanner input = new Scanner(file);
            ) {
                // Read texts from a file
                while (input.hasNext()) {
                    String text = input.nextLine();
                    //display texts in chat page
                    textArea.appendText(text + "\n");
                }
            }
        }
    }
}
