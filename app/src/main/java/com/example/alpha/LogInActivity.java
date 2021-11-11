package com.example.alpha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.DbManager.DbManager;
import com.example.alpha.FirebaseActions.CheckIfEmailExists;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogInActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private TextView emailErrorTextView;
    private EditText editTextPassword;
    private TextView passwordErrorTextView;
    private TextView connectionInfoTextView;
    private Button confirmRegistrationButton;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DbManager db;
    private boolean connection = false;
    private Handler mHandler;
    private CheckIfEmailExists checkIfEmailExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        try{
            db = new DbManager(this);
            mAuth = FirebaseAuth.getInstance();
            editTextEmail = (EditText) findViewById(R.id.editTextEmail);
            emailErrorTextView = (TextView) findViewById(R.id.emailErrorTextView);
            editTextPassword = (EditText)findViewById(R.id.editTextPassword);
            passwordErrorTextView= (TextView)findViewById(R.id.passwordErrorTextView);
            connectionInfoTextView = (TextView) findViewById(R.id.connectionInfoTextView);
            confirmRegistrationButton = (Button) findViewById(R.id.confirmRegistrationButton);
            checkIfEmailExists=new CheckIfEmailExists();
            mHandler = new Handler();
            startRepeatingTask();
        }catch (Exception e){
            Toast.makeText(LogInActivity.this, e.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }


    public boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }

    public boolean isValidPassword(String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[.!#*()?,])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                if (isNetworkConnected()) {
                    connection = true;
                    connectionInfoTextView.setText("");
                    confirmRegistrationButton.setClickable(true);

                } else {
                    connection = false;
                    connectionInfoTextView.setText(R.string.no_connection_info);
                    confirmRegistrationButton.setClickable(false);


                }
                //this function can change value of mInterval.
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }


}

