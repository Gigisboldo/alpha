package com.example.alpha.FirebaseActions;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class GetUserNickname {

        public interface GetUserNicknameListener{
            void onPostExecuteConcluded(String result);
        }

        private GetUserNicknameListener mListener;

        public void setListener (GetUserNicknameListener listner){
            this.mListener=listner;
        }
        public void getUserNickNameInFirestore (FirebaseUser user){
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        dbFirestore.collection("users").document(user.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                            if(document.get("nickName")!=null){
                                mListener.onPostExecuteConcluded(document.getString("nickName"));
                            }else{
                                mListener.onPostExecuteConcluded("");
                            }
                    } else {

                    }
                } else {

                }
            }
        });
    }

}