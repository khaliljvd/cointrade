package khalil.cointrader;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by khalil on 1/16/18.
 */

public class FileCache {

    private File cacheDir;

    public FileCache(Context context){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            cacheDir = new File(Environment.getExternalStorageDirectory(), "LazyList");
        } else {
            cacheDir = context.getCacheDir();
        }

        if(!cacheDir.exists()){
            cacheDir.mkdirs();
        }
    }

    public File getFile(String url){
        String filename = String.valueOf(url.hashCode());

        File f = new File(cacheDir, filename);
        return f;
    }

    public void clear(){
        File[] files = cacheDir.listFiles();
        if(files == null)
            return;

        for(File f: files){
            f.delete();
        }
    }
}
