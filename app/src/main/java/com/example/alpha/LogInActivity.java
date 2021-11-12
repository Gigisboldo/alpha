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
import com.example.alpha.FirebaseActions.GetUserNickname;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogInActivity extends AppCompatActivity {
    private enum ChooseLayout {
        ACTIVITY_LOG_IN,
        ACTIVITY_LOG_ON,
    }

    private EditText editTextEmail;
    private TextView emailErrorTextView;
    private EditText editTextPassword;
    private TextView passwordErrorTextView;
    private TextView connectionInfoTextView;
    private TextView editTextConfirmPassword;
    private TextView confirmPasswordErrorTextView;
    private EditText nicknameEditText;
    private Button confirmRegistrationButton;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DbManager db;
    private CheckIfEmailExists checkIfEmailExists;
    private String email;
    private String password;
    private ChooseLayout choosenLayout = ChooseLayout.ACTIVITY_LOG_IN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        try {
            db = new DbManager(this);
            mAuth = FirebaseAuth.getInstance();
        } catch (Exception e) {
            Toast.makeText(LogInActivity.this, e.toString(),
                    Toast.LENGTH_LONG).show();
        }
        if (extras != null && extras.getString("Type").equals("LogInActivity")) {
            setContentView(R.layout.activity_log_in);
            try {

                editTextEmail = (EditText) findViewById(R.id.editTextEmailLogIn);
                emailErrorTextView = (TextView) findViewById(R.id.emailErrorTextViewLogIn);
                editTextPassword = (EditText) findViewById(R.id.editTextPasswordLogIn);
                passwordErrorTextView = (TextView) findViewById(R.id.passwordErrorTextViewLogIn);
                connectionInfoTextView = (TextView) findViewById(R.id.connectionInfoTextViewLogIn);
                confirmRegistrationButton = (Button) findViewById(R.id.confirmRegistrationButtonLogIn);
                connectionInfoTextView = (TextView) findViewById(R.id.connectionInfoTextViewLogIn);
                checkIfEmailExists = new CheckIfEmailExists();


            } catch (Exception e) {
                Toast.makeText(LogInActivity.this, e.toString(),
                        Toast.LENGTH_LONG).show();
            }
        } else {
            choosenLayout = ChooseLayout.ACTIVITY_LOG_ON;
            setContentView(R.layout.activity_log_on);
            try {
                editTextEmail = (EditText) findViewById(R.id.editTextEmailLogOn);
                emailErrorTextView = (TextView) findViewById(R.id.emailErrorTextViewLogOn);
                editTextPassword = (EditText) findViewById(R.id.editTextPasswordLogOn);
                passwordErrorTextView = (TextView) findViewById(R.id.passwordErrorTextViewLogOn);
                connectionInfoTextView = (TextView) findViewById(R.id.connectionInfoLogOn);
                confirmRegistrationButton = (Button) findViewById(R.id.confirmRegistrationButtonLogOn);
                connectionInfoTextView = (TextView) findViewById(R.id.connectionInfoLogOn);
                editTextConfirmPassword = (TextView) findViewById(R.id.editTextPasswordLogOn);
                confirmPasswordErrorTextView = (TextView) findViewById(R.id.confirmPasswordErrorTextViewLogOn);
                nicknameEditText = (EditText) findViewById(R.id.nicknameEditTextLogOn);
            } catch (Exception e) {
                Toast.makeText(LogInActivity.this, e.toString(),
                        Toast.LENGTH_LONG).show();
            }

        }
    }


    @Override
    public void onStart() {
        super.onStart();
        try {


            editTextEmail.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                    emailErrorTextView.setText("");
                    if (isEmailValid(editTextEmail.getText().toString())) {

                        email = editTextEmail.getText().toString();
                        emailErrorTextView.setText("");
                    } else {

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
                    if (isValidPassword(editTextPassword.getText().toString())) {


                        password = editTextPassword.getText().toString();
                        passwordErrorTextView.setText("");

                    } else {

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
                    if (isEmailValid(editTextEmail.getText().toString()) && !editTextEmail.getText().toString().isEmpty()) {

                        if (isValidPassword(editTextPassword.getText().toString()) && !editTextPassword.getText().toString().isEmpty()) {
                            mAuth.signInWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString())
                                    .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                user = mAuth.getCurrentUser();
                                                if (user != null) {
                                                    if (user.isEmailVerified()) {
                                                        GetUserNickname mGetUserNickname = new GetUserNickname();
                                                        mGetUserNickname.getUserNickNameInFirestore(user);
                                                        mGetUserNickname.setListener(new GetUserNickname.GetUserNicknameListener() {
                                                            @Override
                                                            public void onPostExecuteConcluded(String nickNameResult) {

                                                                if (nickNameResult.equals("")) {
                                                                    db.saveNewUser("", user.getUid(), "", editTextEmail.getText().toString(),
                                                                            editTextPassword.getText().toString());
                                                                    //TODO go to nickname activity
                                                                } else {
                                                                    db.saveNewUser("", user.getUid(), nickNameResult, editTextEmail.getText().toString(), editTextPassword.getText().toString());

                                                                }
                                                            }
                                                        });
                                                    } else {
                                                        db.saveNewUser("", user.getUid(), "", editTextEmail.getText().toString(), editTextPassword.getText().toString());
                                                        //TODO start email verify intent
                                                    }

                                                } else {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(LogInActivity.this);
                                                    builder.setTitle(R.string.attention);
                                                    builder.setMessage(R.string.error_log_in);
                                                    builder.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {

                                                        }
                                                    });


                                                    builder.show();

                                                }

                                            } else {

                                                checkIfEmailExists.sendRequest(LogInActivity.this, editTextEmail.getText().toString());
                                                checkIfEmailExists.setListener(new CheckIfEmailExists.CheckIfEmailExitsListener() {
                                                    @Override
                                                    public void onPostExecuteConcluded(boolean result) {
                                                        if (result == false) {
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(LogInActivity.this);
                                                            builder.setTitle(R.string.attention);
                                                            builder.setMessage(R.string.error_email_not_existing);
                                                            builder.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {

                                                                }
                                                            });


                                                            builder.show();

                                                        } else {
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(LogInActivity.this);
                                                            builder.setTitle(R.string.attention);
                                                            builder.setMessage(R.string.error_log_in);
                                                            builder.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {

                                                                }
                                                            });


                                                            builder.show();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                        } else {
                            passwordErrorTextView.setText(R.string.error_password_not_valid);
                        }
                    } else {
                        if (!isValidPassword(editTextPassword.getText().toString()) && editTextPassword.getText().toString().isEmpty()) {
                            passwordErrorTextView.setText(R.string.error_password_not_valid);
                        }
                        emailErrorTextView.setText(R.string.error_email_not_valid);
                    }

                }
            });
        } catch (Exception e) {

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


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


}
