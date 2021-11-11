package com.example.alpha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.DbManager.DbManager;
import com.example.alpha.FirebaseActions.CheckIfEmailExists;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
    private CheckIfEmailExists checkIfEmailExists;
    private String email;
    private String password;

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
        }catch (Exception e){
            Toast.makeText(LogInActivity.this, e.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();


        editTextEmail.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                emailErrorTextView.setText("");
                if (isEmailValid(editTextEmail.getText().toString())){

                    email = editTextEmail.getText().toString();
                    emailErrorTextView.setText("");
                }else{

                    emailErrorTextView.setText(R.string.error_email_not_valid);
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {


                ;

            }
        });


        editTextPassword.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                passwordErrorTextView.setText("");
                if (isValidPassword(editTextPassword.getText().toString())){


                    password = editTextPassword.getText().toString();
                    passwordErrorTextView.setText("");

                }else{

                    passwordErrorTextView.setText(R.string.error_password_not_valid);
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {




            }
        });

        confirmRegistrationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {



            }
        });

    }

    public boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
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



    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }



}
