package com.rishi.sharepic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener {

    Boolean signUpModeActive = false;

    @BindView(R.id.signupButton)
    Button signupButton;

    @BindView(R.id.changeSignupModeTextView)
    TextView changeSignupModeTextView;

    @BindView(R.id.usernameEditText)
    EditText usernameEditText;

    @BindView(R.id.passwordEditText)
    EditText passwordEditText;

    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;

    @BindView(R.id.imageView)
    ImageView imageView;


    @OnClick(R.id.changeSignupModeTextView)
    public void toggleMode(){
        if (signUpModeActive) {

            signUpModeActive = false;
            signupButton.setText("Login");
            changeSignupModeTextView.setText("Or, Signup");

        } else {

            signUpModeActive = true;
            signupButton.setText("Signup");
            changeSignupModeTextView.setText("Or, Login");

        }
    }

    @OnClick({R.id.relativeLayout, R.id.imageView})
    public void hideKey(){
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
            submit();
        }

        return false;
    }


    @OnClick(R.id.signupButton)
    public void submit(){

        if(signUpModeActive){

                ParseUser user = new ParseUser();

                user.setUsername(usernameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {

                            showUserList();
                            Log.i("Signup", "Successful");

                        } else {

                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }

            else {

                ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {

                        if (user != null) {

                            showUserList();
                            Log.i("Signup", "Login successful");

                        } else {

                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }


                    }
                });


            }

    }


    private void showUserList(){
        Intent intent=new Intent(MainActivity.this , UserListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        passwordEditText.setOnKeyListener(this);

       // if(ParseUser.getCurrentUser().get("objectId") != null){

        if(ParseUser.getCurrentUser() != null){

            Log.e("Current User", ParseUser.getCurrentUser()+" ");
            showUserList();
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());


    }


}
