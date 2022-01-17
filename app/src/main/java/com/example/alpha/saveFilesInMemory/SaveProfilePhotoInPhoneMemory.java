package com.example.alpha.saveFilesInMemory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.io.File;
import java.io.FileOutputStream;


public class SaveProfilePhotoInPhoneMemory {

    public Bitmap getResizedBitmap(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        int newHeight=1224;
        int newWidth=1224;
        if(width>height){
            newHeight=1224;
            newWidth=1632;
        }else if(width<height){
            newHeight=1632;
            newWidth=1224;
        }
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // Create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // Resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);


        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;

    }

    public void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName, String userUid, Context context) {


        File direct = new File(context.getExternalFilesDir(userUid + "/Pictures/profileImage").getAbsolutePath());

        if (!direct.exists()) {
            direct.mkdirs();
        }else{}

        File file = new File(context.getExternalFilesDir(userUid + "/Pictures/profileImage").getAbsolutePath() , "profileImage" + userUid + ".jpg");
        if (file.exists()) {
            file.delete();
        }else{

        }
        try {


            FileOutputStream out = new FileOutputStream(file);
            getResizedBitmap(imageToSave).compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
