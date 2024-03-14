package com.matainja.bootapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class FileDownloader extends AsyncTask<String, Void, String> {

    private Context context;
    private OnDownloadCompleteListener listener;

    public interface OnDownloadCompleteListener {
        void onDownloadComplete(String filePath);

        void onError(String errorMessage);

        void onDownloadStatus(boolean isDownloading);
    }

    public FileDownloader(Context context, OnDownloadCompleteListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        listener.onDownloadStatus(true);
    }

    @Override
    protected String doInBackground(String... params) {
        String fileUrl = params[0];
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setConnectTimeout(10000); // Connection timeout in milliseconds

            Random random = new Random();
            int randomNumber = random.nextInt(100);

            //File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
            if (directory != null) {
                if (!directory.exists()) {
                    directory.mkdirs(); // Create directories if they don't exist
                }
                File file = new File(directory, "downloaded_video" + randomNumber + ".mp4");
                FileOutputStream outputStream = new FileOutputStream(file);
                InputStream inputStream = connection.getInputStream();

                byte[] buffer = new byte[2048];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();

                return file.getAbsolutePath();
            } else {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            listener.onDownloadComplete(result);
        } else {
            listener.onError("Error downloading file");
        }
        listener.onDownloadStatus(false); // Notify that download is complete
    }

}
