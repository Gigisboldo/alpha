package com.example.alpha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.example.alpha.DbManager.DbManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    //Choose the first layout of Mainactivity, if it's before the log in choose ACTIVITY_MAIN else choose ACTIVITY_FIRST
    private enum ChooseLayout {
        ACTIVITY_FIRST,
        ACTIVITY_MAIN,
    }

    private ChooseLayout choosenLayout;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private DbManager db = null;
    private Intent logInOnIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        choosenLayout = ChooseLayout.ACTIVITY_MAIN;
        setContentView(R.layout.activity_main);


        try {
            //Try to get Firebase user and access to Database
            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
            db = new DbManager(MainActivity.this);
            logInOnIntent = new Intent(this, LogInOnActivity.class);

        } catch (Exception e) {
            Toast.makeText(MainActivity.this, e.toString(),
                    Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            //Check if there is a user saved in the database
            if (db.query().getCount() != 0) {
                //Try to log in with the current user
                mAuth.signInWithEmailAndPassword(user.getEmail(), db.getUserPassword(user.getEmail()))
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    user = mAuth.getCurrentUser();
                                    //Check if the user has completed the log on process
                                    if (user != null && user.isEmailVerified()) {
                                        //if the user has completed the log on process Mainactivity will change the layout to first layout
                                        choosenLayout = ChooseLayout.ACTIVITY_FIRST;
                                        setContentView(R.layout.activity_first);
                                        //TODO try log in first

                                    } else {
                                        //if the user has not completed the log on process will be redirected to the log in on activity
                                        startActivity(logInOnIntent);
                                    }


                                } else {
                                    Toast.makeText(MainActivity.this, "Error log in",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            } else {
                //if there is no user saved in the database will be redirected to log in on activity
                startActivity(logInOnIntent);
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, e.toString(),
                    Toast.LENGTH_LONG).show();
        }

    }
}