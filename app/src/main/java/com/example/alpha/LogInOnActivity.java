package com.example.alpha;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.alpha.DbManager.DatabaseStrings;
import com.example.alpha.DbManager.DbManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogInOnActivity extends AppCompatActivity {


    private enum listType {deleteUser, logInUser}
    private listType _listType;
    private TextView deleteUser;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Button logOnButton;
    private Button logInButton;
    private CursorAdapter adapter;
    private DbManager db;
    private ListView usersListView;
    private Intent mainActivityIntent;
    private Intent LogInActivityIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_on);
        //Set the layout to choose the user for login
        _listType = listType.logInUser;
        try {
            db = new DbManager(this);
            mAuth = FirebaseAuth.getInstance();
            usersListView = (ListView) findViewById(R.id.listViewDemo);
            logInButton = (Button) findViewById(R.id.log_in_button);
            logOnButton = (Button) findViewById(R.id.log_on_button);
            deleteUser = (TextView) findViewById(R.id.textViewDelete);
            mainActivityIntent = new Intent(LogInOnActivity.this, MainActivity.class);
            LogInActivityIntent = new Intent(LogInOnActivity.this, LogInActivity.class);

        } catch (Exception e) {
            Toast.makeText(LogInOnActivity.this, e.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }


    @SuppressLint("Range")
    @Override
    public void onStart() {
        super.onStart();
        //Procedure to log in on activity
        try {
            //set the clicklistener if the user wants to delete a user
            deleteUser.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if (_listType.equals(listType.logInUser)) {
                        usersListView.setBackgroundColor(Color.parseColor("#FFF41616"));
                        _listType = listType.deleteUser;
                    } else {
                        _listType = listType.logInUser;
                        usersListView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                    }
                }
            });
            final Cursor crs = db.query();

            crs.moveToFirst();
            int icount = crs.getCount();
            //Set the clicklistener for the LogInActivity for log in
            logInButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    LogInActivityIntent.putExtra("Type","LogInActivity");
                    LogInActivityIntent.putExtra("Email","empty");
                    startActivity(LogInActivityIntent);
                }
            });
            //Set the clicklistener for the LogInActivity for log on with a new user
            logOnButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    LogInActivityIntent.putExtra("Type","LogOnActivity");
                    LogInActivityIntent.putExtra("Email","empty");
                    startActivity(LogInActivityIntent);
                }
            });

            final List list = new LinkedList();

            if (icount == 0) {


            } else {
                //default option list of the users logged in
                for (int i = 0; i < icount; i++) {
                    crs.moveToPosition(i);
                    list.add(new Users(crs.getString((crs.getColumnIndex(DatabaseStrings.FIELD_NICKNAME))), crs.getString((crs.getColumnIndex(DatabaseStrings.FIELD_MAIL))), crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_USER_IMAGE))));
                }
                CustomAdapter adapter = new CustomAdapter(this, R.layout.users_log_on_custom, list);
                usersListView.setAdapter(adapter);
                usersListView.setClickable(true);
                if(isNetworkConnected()) {
                    usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @SuppressLint("Range")
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {

                            if (_listType.equals(listType.logInUser)) {
                                crs.moveToPosition(position);
                                //Sign in with the user clicked
                                //if the user before log out doesn't want to delete password
                                if (!crs.getString((crs.getColumnIndex(DatabaseStrings.FIELD_PASSWORD))).equals("")) {
                                    mAuth.signInWithEmailAndPassword(crs.getString((crs.getColumnIndex(DatabaseStrings.FIELD_MAIL))), crs.getString((crs.getColumnIndex(DatabaseStrings.FIELD_PASSWORD)))).addOnCompleteListener(LogInOnActivity.this, new OnCompleteListener<AuthResult>() {

                                        @SuppressLint("Range")
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                startActivity(mainActivityIntent);

                                            } else {
                                                // If sign in fails, display a message to the user.

                                                Toast.makeText(LogInOnActivity.this, "Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();

                                                db.deleteUser(crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ID)));
                                                list.clear();
                                                recreate();


                                            }


                                        }
                                    });
                                } else {
                                    //if the user before log out want to delete password will be send to LogInActivity with the mail saved
                                    LogInActivityIntent.putExtra("Type", "LogInActivity");
                                    LogInActivityIntent.putExtra("Email", crs.getString((crs.getColumnIndex(DatabaseStrings.FIELD_MAIL))));
                                    startActivity(LogInActivityIntent);
                                }
                            } else {
                                crs.moveToPosition(position);
                                db.deleteUser(crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ID)));
                                recreate();
                            }
                        }
                    });
                }else{
                    Toast.makeText(LogInOnActivity.this, R.string.no_connection_info,
                            Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            Toast.makeText(LogInOnActivity.this, e.toString(),
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
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}