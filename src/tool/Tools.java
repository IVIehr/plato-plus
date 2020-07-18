package tool;

import jdbc.SelectApp;

public class Tools {
    SelectApp selectApp = new SelectApp();

    public Tools(){
        selectApp.selectAll();
    }
    public int findIndex(java.util.ArrayList dest,String target){
        for(int i=0;i<dest.size();i++){
            if(dest.get(i).equals(target))
                return i;
        }
        return 0;
    }
}
