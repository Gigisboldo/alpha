package com.example.alpha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.DbManager.DbManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerifyEmail extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Intent userSettingIntent;
    private DbManager db=null;
    private TextView emailSentTextView;
    private TextView emailAddress;
    private Button resendVerificationEmailButton;
    private Button changeEmailAddressButton;
    private Button doneButton;
    private Intent changeEmailAddressIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);
        try{

            emailSentTextView = (TextView) findViewById(R.id.email_sent);
            emailAddress = (TextView) findViewById(R.id.email_address);
            resendVerificationEmailButton = (Button) findViewById(R.id.resend_verification_email_button);
            changeEmailAddressButton = (Button) findViewById(R.id.change_email_address);
            doneButton = (Button) findViewById(R.id.done);
            db=new DbManager(this);
            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
            emailAddress.setText(user.getEmail());
            changeEmailAddressIntent = new Intent(this, ChangeEmailAddress.class);
        }catch(Exception e){
            Toast.makeText(VerifyEmail.this, e.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            resendVerificationEmailButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    user.sendEmailVerification();
                }
            });
            changeEmailAddressButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(changeEmailAddressIntent);
                }
            });
            doneButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mAuth.signInWithEmailAndPassword(user.getEmail(), db.getUserPassword(user.getEmail()))
                            .addOnCompleteListener(VerifyEmail.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        user = mAuth.getCurrentUser();
                                        if (user.isEmailVerified()) {
                                            startActivity(userSettingIntent);
                                        }

                                    }


                                }
                            });

                }
            });
            mAuth.signInWithEmailAndPassword(user.getEmail(), db.getUserPassword(user.getEmail()))
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                user = mAuth.getCurrentUser();
                                if (user.isEmailVerified()) {
                                    startActivity(userSettingIntent);
                                }

                            }

                            // ...
                        }
                    });
        }catch(Exception e){
            Toast.makeText(VerifyEmail.this, e.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        mAuth.signInWithEmailAndPassword(user.getEmail(), db.getUserPassword(user.getEmail()) )
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
                            if(user.isEmailVerified()) {
                                startActivity(userSettingIntent);
                            }

                        }

                        // ...
                    }
                });

    }
}