package ted.com.freezeapp.helper;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by fy1 on 15/06/02.
 */
public class Ls {
    public static ArrayList<String> ls(String path, String end){
        ArrayList<String> files = new ArrayList<>();
        File d = new File(path);
        if(d.exists() && d.isDirectory()){
            for (File f :d.listFiles()){
                if(f.isFile() && f.getName().endsWith(end)){
                    files.add(f.getAbsolutePath());
                }
            }
        }
        return files;
    }
}
