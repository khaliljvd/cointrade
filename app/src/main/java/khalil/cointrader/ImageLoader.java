package khalil.cointrader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Output;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by khalil on 1/16/18.
 */

public class ImageLoader {
    MemoryCache memoryCache = new MemoryCache();
    FileCache fileCache;

    private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());

    ExecutorService executorService;
    android.os.Handler handler = new android.os.Handler();

    public ImageLoader(Context context){
        fileCache = new FileCache(context);
        executorService = Executors.newFixedThreadPool(5);
    }

    final int stub_int = R.drawable.loading;

    public void displayImage(String url, ImageView imageView){
        imageViews.put(imageView, url);
        Bitmap bitmap = memoryCache.get(url);

        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
        } else {
            queuePhoto(url, imageView);
            imageView.setImageResource(stub_int);
        }
    }

    private void queuePhoto(String url, ImageView imageView){
        PhotoToLoad p = new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }

    private class PhotoToLoad{
        public String url;
        public ImageView imageView;
        public PhotoToLoad(String u, ImageView i){
            url = u;
            imageView = i;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;

        public PhotosLoader(PhotoToLoad ptl){
            this.photoToLoad = ptl;
        }


        @Override
        public void run() {
            try {
                if(imageViewReused(photoToLoad))
                    return;

                Bitmap bmp = getBitmap(photoToLoad.url);
                memoryCache.put(photoToLoad.url, bmp);

                if(imageViewReused(photoToLoad))
                    return;

                BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
                handler.post(bd);
            } catch(Throwable e){
                e.printStackTrace();
            }
        }
    }

    private Bitmap getBitmap(String url){
        File f = fileCache.getFile(url);

        Bitmap b = decodeFile(f);

        if(b != null)
            return b;

        try {
            Bitmap bitmap = null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(f);

            Utils.copyStream(is, os);
            os.close();
            conn.disconnect();
            bitmap = decodeFile(f);
            return bitmap;

        } catch (Throwable e) {
            e.printStackTrace();
            if(e instanceof OutOfMemoryError)
                memoryCache.clear();

            return null;
        }
    }

    private Bitmap decodeFile(File f){
        try{
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1 = new FileInputStream(f);
            BitmapFactory.decodeStream(stream1, null, o);
            stream1.close();

            final int REQUIRED_SIZE = 85;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;

            while(true){
                if(width_tmp/2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE){
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            FileInputStream stream2 = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();

            return bitmap;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    boolean imageViewReused(PhotoToLoad ptl){
        String tag = imageViews.get(ptl.imageView);

        if(tag == null || !tag.equals(ptl.url))
            return true;

        return false;
    }

    class BitmapDisplayer implements Runnable {

        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        public BitmapDisplayer(Bitmap b, PhotoToLoad p){
            bitmap = b;
            photoToLoad = p;
        }

        @Override
        public void run() {
            if(imageViewReused(photoToLoad))
                return;

            if(bitmap != null){
                photoToLoad.imageView.setVisibility(View.GONE);

                Animation a = new AlphaAnimation(0.00f, 1.00f);
                a.setDuration(500);
                a.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        photoToLoad.imageView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                photoToLoad.imageView.setImageBitmap(bitmap);
                photoToLoad.imageView.startAnimation(a);

            } else {
                photoToLoad.imageView.setImageResource(stub_int);
            }
        }
    }

    public void clearCache(){
        memoryCache.clear();
        fileCache.clear();
    }
}
