package com.example.alpha.FirebaseActions;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import com.example.alpha.saveFilesInMemory.SaveProfilePhotoInPhoneMemory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import java.text.SimpleDateFormat;


public class ProfileImages {


public void DownloadProfilePhoto (String user, Context ctx){

    try {
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();


        final StorageReference profileImageRef = storageRef.child("users/" + user + "/images/profileImage/userImage" + user + ".jpg");


        profileImageRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {


                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");


                final long ONE_MEGABYTE = 1024 * 1024;
                profileImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {

                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        SaveProfilePhotoInPhoneMemory mSavePhoto = new SaveProfilePhotoInPhoneMemory();
                        mSavePhoto.createDirectoryAndSaveFile(bmp, "profileImage" + user, user, ctx);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });
    }catch (Exception e){

    }

}

}
