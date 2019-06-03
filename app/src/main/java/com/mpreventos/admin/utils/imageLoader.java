package com.mpreventos.admin.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class imageLoader {

    private Context context;
    private boolean estado;

    //constructor para cargar imiagen
    public imageLoader(Context context) {
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

                ex.getStackTrace();
                estado = false;
            }
        }
        return estado;
    }
}
