package com.mpreventos.admin.utils;

import android.content.Context;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.mpreventos.admin.R;

public class imageLoader {

    private static boolean estado;
    private static Context context;


    //constructor para cargar imiagen
    public imageLoader(Context context) {
        this.context = context;
    }

    //cargar imagen con glide
    public static boolean setImgWithGlide(String imgUrl, ImageView imageView) {
        if (imgUrl == null) {
            estado = false;
        } else {
            try {

                Glide.with(context).load(imgUrl).into(imageView);
                estado = true;

            } catch (Exception ex) {
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logompr));
                ex.getStackTrace();
                estado = false;
            }
        }
        return estado;
    }
}
