package com.cellular.automata.cellularautomata.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.signature.ObjectKey;
import com.cellular.automata.cellularautomata.R;
import com.cellular.automata.cellularautomata.Settings;
import com.cellular.automata.cellularautomata.glide.GlideApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageHelper {


    private static String imageDirectoryName = "images";
    private static String tag = "ImageHelper";

    private boolean clearImageCache = Settings.clearImageCache;

    public interface SaveImageCallback{

        void onImageSaved();

    }

    public void loadImageIntoImageView(Context context, String imageName, ImageView imageView){


        //try to load from saved user images
        File picture = new File(context.getFilesDir() + File.separator + imageName);

        if (picture.exists()) {

            if(clearImageCache){
                GlideApp.with(context).load(picture).signature(new ObjectKey(String.valueOf(System.currentTimeMillis()))).into(imageView);
            }else{
                GlideApp.with(context).load(picture).into(imageView);
            }

            return;

        }

        //try to load from drawable
        int drawableId = context.getResources().getIdentifier(imageName.replaceAll("-", "_"), "drawable", context.getPackageName());
        if (drawableId != 0) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(context.getResources(), drawableId, options);
            int pictureWidth = options.outWidth;
            int pictureHeight = options.outHeight;

            GlideApp.with(context).load(drawableId).override(pictureWidth, pictureHeight).into(imageView);

            return;

        }

        //try to load from downloaded / updated pixio images
        picture = new File(context.getFilesDir() + File.separator + imageName);

        if (picture.exists()) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(context.getFilesDir() + File.separator + imageName, options);

            int pictureWidth = options.outWidth;
            int pictureHeight = options.outHeight;

            if (imageName.contains("-min")){

                Bitmap icon = BitmapFactory.decodeFile(context.getFilesDir() + File.separator + imageName);

                if (icon != null){
                    Bitmap bitmap1 = Bitmap.createScaledBitmap(icon, icon.getWidth()*30, icon.getHeight()*30, false);

                    GlideApp.with(context).load(bitmap1).override(bitmap1.getWidth(), bitmap1.getHeight()).into(imageView);
                }

            }else{

                GlideApp.with(context).load(picture).override(pictureWidth, pictureHeight).into(imageView);

            }

            return;


        }

        //load default image
        GlideApp.with(context).load(R.drawable.input_background).into(imageView);


    }

    public static void saveImage(Bitmap bitmap, String filename, Context context, SaveImageCallback callback){

        boolean del = deleteImage(filename, context);

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(getImagePath(context, filename));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                    if(callback!=null){
                        callback.onImageSaved();
                        Log.d(tag, "saved to: " + getImagePath(context, filename));

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void renameImage(String filename, String newFileName, Context context, SaveImageCallback callback){

        File oldImage = new File(getImagePath(context, filename));
        File newImage = new File(getImagePath(context, newFileName));

        if(oldImage.renameTo(newImage) && callback!= null){
            callback.onImageSaved();
        }

    }

    public static boolean deleteImage(String filename, Context context){

        File file = new File(getImagePath(context, filename));
        if(file.exists()){

            if(file.delete()){
                Log.d(tag, "File deleted: " + file.toString());
            }else{
                Log.d(tag, "File not deleted: " + file.toString());
            }

        }

        context.deleteFile(filename);

        return false;
    }

    private static String getImagePath(Context context, String fileName){



        String fileDir = context.getFilesDir() + File.separator;

        return fileDir + fileName;

    }




}

