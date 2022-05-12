package com.masterlibs.basestructure.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.room.util.FileUtil;

import com.itextpdf.text.pdf.PdfDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CommonUtils {

    private static final String POLICY_URL = "https://firebasestorage.googleapis.com/v0/b/compass-app-df4f4.appspot.com/o/Privacy_Policy_QRCodeReader.html?alt=media&token=eace85bf-ef92-4c2d-b7ec-ba1f0d4e41c7";
    private static final String SUBJECT = "Feedback for App Image to PDF";

    private static CommonUtils instance;

    private CommonUtils() {
    }


    public static CommonUtils getInstance() {
        if (instance == null) {
            instance = new CommonUtils();
        }
        return instance;
    }

    public void shareApp(Context context) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody =
                "https://play.google.com/store/apps/details?id=" + context.getPackageName();
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(sharingIntent, "Share to"));
    }

    public void showPolicy(Context context) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                    POLICY_URL)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getDocumentDirPath(String FolderName) {
        File dir = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" + FolderName);
        } else {
            dir = new File(Environment.getExternalStorageDirectory() + "/" + FolderName);
        }

        // Make sure the path directory exists.
        if (!dir.exists()) {
            // Make it, if it doesn't exit
            boolean success = dir.mkdirs();
            if (!success) {
                dir = null;
            }
        }
        return dir;
    }

    public void saveFile(InputStream fin, String savePath, String nameFile) {
        File file = new File(savePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(new File(savePath + "/" + nameFile));
            int lenght = 0;
            byte buff[] = new byte[1024];
            lenght = fin.read(buff);
            while (lenght > 0) {
                fout.write(buff, 0, lenght);
                lenght = fin.read(buff);
            }
            fin.close();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
