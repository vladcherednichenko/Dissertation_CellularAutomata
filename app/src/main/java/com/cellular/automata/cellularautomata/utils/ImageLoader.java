package com.cellular.automata.cellularautomata.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bumptech.glide.signature.ObjectKey;
import com.cellular.automata.cellularautomata.glide.GlideApp;

import java.io.File;


public class ImageLoader {

    private static ImageLoader instance;

    private ImageLoader(){}

    private boolean clearImageCache = true;

    public static ImageLoader getInstance() {


        if(instance == null){
            instance = new ImageLoader();
        }
        return instance;
    }

    public void setClearImageCache(boolean b){
        clearImageCache = b;
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
        GlideApp.with(context).load(R.drawable.image_white_rectangle).into(imageView);


    }


}
