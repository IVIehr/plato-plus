package file;

import java.io.File;

public class DeleteFile {

    public DeleteFile(String fileName){
        File file = new File(fileName+".txt");
        file.delete();
    }
}
