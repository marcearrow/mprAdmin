package com.mpreventos.admin.utils;

import android.content.Context;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.mpreventos.admin.R;

public class ImageLoader {

    private static boolean estado;
    private Context context;


    //constructor para cargar imiagen
    public ImageLoader(Context context) {
        this.context = context;
    }

    //cargar imagen con glide
    public boolean setImgWithGlide(String imgUrl, ImageView imageView) {
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
