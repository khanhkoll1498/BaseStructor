package com.masterlibs.basestructure.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.documentmaster.documentscan.Const;
import com.documentmaster.documentscan.OnActionCallback;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.masterlibs.basestructure.R;
import com.masterlibs.basestructure.model.ItemFile;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

@SuppressLint("StaticFieldLeak")
public class ImageToPdf extends AsyncTask<String, String, String> {
    private static final String ITEM_ALL_FILE = "ITEM_ALL_FILE";
    private final ProgressDialog dialog;
    private final List<String> imagesUri;
    private final Context context;
    private final OnActionCallback callback;
    private String path;
    private String fileName;

    public ImageToPdf(List<String> imagesUri, Context context, OnActionCallback callback) {
        this.callback = callback;
        this.imagesUri = imagesUri;
        this.context = context;
        dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage(context.getString(R.string.converting) + "... 0%");
        dialog.setButton(ProgressDialog.BUTTON_NEGATIVE, context.getString(R.string.cancel), (dialog, which) -> {
            ImageToPdf.this.cancel(true);
            if (path != null)
                new File(path).delete();
            dismissProgressDialog();
        });
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        path = Environment.getExternalStorageDirectory().toString() + "/Documents/" + Arrays.toString(params) + ".pdf";
        long totalSize = getTotalSize();
        long currentConvert = 0;
        Document document = new Document(PageSize.A4, 38, 38, 50, 38);
        Rectangle documentRect = document.getPageSize();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            for (int i = 0; i < imagesUri.size(); i++) {


                Bitmap bmp;
                bmp = BitmapFactory.decodeFile(imagesUri.get(i));
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 80, stream);

                currentConvert += new File(imagesUri.get(i)).length();
                int percent = (int) ((currentConvert * 1.0 / totalSize) * 100);
                publishProgress(percent + "%", imagesUri.get(i));


                Image image = Image.getInstance(stream.toByteArray());
                if (bmp.getWidth() > documentRect.getWidth() || bmp.getHeight() > documentRect.getHeight()) {
                    image.scaleToFit(documentRect.getWidth() - 16, documentRect.getHeight() - 16);
                } else {
                    image.scaleToFit(bmp.getWidth(), bmp.getHeight());
                }
                image.setAbsolutePosition((documentRect.getWidth() - image.getScaledWidth()) / 2,
                        (documentRect.getHeight() - image.getScaledHeight()) / 2);
                image.setBorder(Image.BOX);
                document.add(image);

                document.newPage();
            }
            document.close();
            File file = new File(path);
            int totalPage = getNumberPage(file) - 1;
//            try {
//                categoryList.get(ITEM_ALL_FILE).addFile(0, file, totalPage);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        } catch (Exception e) {
            Log.d("TAG", "doInBackground: " + e.getMessage());
            e.printStackTrace();
        }
        imagesUri.clear();
        return null;
    }

    private int getNumberPage(File file) {
        PdfiumCore pdfiumCore = new PdfiumCore(context);
        try {
            ParcelFileDescriptor fd = context.getContentResolver().openFileDescriptor(Uri.fromFile(file), "r");
            PdfDocument pdfDocument = pdfiumCore.newDocument(fd);
            return pdfiumCore.getPageCount(pdfDocument);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        String percent = values[0];
        //callback.callback(Const.PATH_PDF, path);

        //dialog.setMessage("Creating..." + percent);
    }

    private long getTotalSize() {
        long rs = 0;
        for (int i = 0; i < imagesUri.size(); i++) {
            rs += new File(imagesUri.get(i)).length();
        }
        return rs;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (((Activity) context).isDestroyed())
            return;
        dismissProgressDialog();
        try {
            ItemFile itemFile = new ItemFile(path);
            callback.callback(Const.PATH_PDF, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void dismissProgressDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }
}
