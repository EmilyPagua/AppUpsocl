package com.upsocl.upsoclapp.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by emily.pagua on 12-07-16.
 */
public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;
    Resources resources;

    public DownloadImage(ImageView imageView, Resources resources) {
        this.imageView = imageView;
        this.resources = resources;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;

        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);

        } catch (IOException e) {
            Log.e("ERROR DownloadImage", e.getMessage());
            e.printStackTrace();
        }

        return mIcon11;
    }

    protected void onPostExecute(Bitmap resutl){
        if (resutl!=null){
            //creamos el drawable redondeado
            RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(resources, resutl);
            //asignamos el CornerRadius
            roundedDrawable.setCornerRadius(resutl.getHeight());
            imageView.setImageDrawable(roundedDrawable);
        }

    }
}
