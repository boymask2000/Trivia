package com.boymask.triviaforall;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

class ImageDownloader extends AsyncTask<String, String, Bitmap> {

    private final ImageView imageView;
    private final MainActivity main;
    private String turl;

    public ImageDownloader(String url, ImageView imageView, MainActivity mainActivity) {
        this.turl = url;
        this.imageView = imageView;
        this.main = mainActivity;
    }

    private Bitmap prev = null;

    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            URL url = new URL(turl);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            if (prev != null) prev.recycle();
            prev = bmp;
            return bmp;
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }



    @Override
    protected void onPostExecute(Bitmap result) {
        // execution of result of Long time consuming operation

        main.setImg(result);
    }


    @Override
    protected void onPreExecute() {

    }


    @Override
    protected void onProgressUpdate(String... text) {


    }

    private String readUrl(String url) throws Exception {
        String out = "";
        URL yahoo = new URL(url);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        yahoo.openStream()));

        String inputLine;

        while ((inputLine = in.readLine()) != null)
            out += inputLine;

        in.close();
        return out;
    }
}