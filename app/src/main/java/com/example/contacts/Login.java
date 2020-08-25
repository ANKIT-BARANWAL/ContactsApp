package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserIdStorageFactory;

public class Login extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    EditText mail , password;
    Button login,register;
    TextView tvreset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);
        mail=findViewById(R.id.mail);
        password=findViewById(R.id.password);
        login=findViewById(R.id.login);
        register=findViewById(R.id.register);
        tvreset=findViewById(R.id.tvreset);
        showProgress(true);
       login.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(mail.getText().toString().isEmpty() || password.getText().toString().isEmpty())
               {
                   Toast.makeText(Login.this, "Please enter all fields!!!", Toast.LENGTH_SHORT).show();
               }
               else
               {
                   String email = mail.getText().toString().trim();
                   String pass = password.getText().toString().trim();
                   showProgress(true);
                   tvLoad.setText("Busy logging you in... please wait...");
                   //LOGGING IN with these datas
                   Backendless.UserService.login(email, pass, new AsyncCallback<BackendlessUser>() {
                       @Override
                       public void handleResponse(BackendlessUser response) {

                           ApplicationClass.user = response;

                           Toast.makeText(Login.this, "LOGGED IN Successfully!!", Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(Login.this,MainActivity.class));
                           Login.this.finish();
                       }

                       @Override
                       public void handleFault(BackendlessFault fault) {
                           Toast.makeText(Login.this, "Error: "+fault.getMessage() , Toast.LENGTH_SHORT).show();
                           showProgress(false);
                       }
                   }, true);

               }


           }
       });
       register.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(Login.this,Register.class));
           }
       });

       tvreset.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(mail.getText().toString().isEmpty())
               {
                   Toast.makeText(Login.this, "Please enter your email address in email field!!", Toast.LENGTH_SHORT).show();
               }
               else
               {
                   String email = mail.getText().toString().trim();
                   showProgress(true);
                   tvLoad.setText("Busy sending reset instructions... please wait...");
                   Backendless.UserService.restorePassword(email, new AsyncCallback<Void>() {
                       @Override
                       public void handleResponse(Void response) {
                           Toast.makeText(Login.this, "Reset instructions sent to your email address, please check!!!", Toast.LENGTH_SHORT).show();
                            showProgress(false);
                       }

                       @Override
                       public void handleFault(BackendlessFault fault) {
                           Toast.makeText(Login.this, "Error: "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                           showProgress(false);
                       }
                   });
               }
           }
       });
        tvLoad.setText("Checking logging credentials... please wait...");
       Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
           @Override
           public void handleResponse(Boolean response) {
               if(response)
               {
                   String objectid = UserIdStorageFactory.instance().getStorage().get();
                  tvLoad.setText("Logging you in... please wait...");
                   Backendless.Data.of(BackendlessUser.class).findById(objectid, new AsyncCallback<BackendlessUser>() {
                       @Override
                       public void handleResponse(BackendlessUser response) {

                           ApplicationClass.user = response;

                            startActivity(new Intent(Login.this,MainActivity.class));
                            Login.this.finish();
                       }

                       @Override
                       public void handleFault(BackendlessFault fault) {
                           Toast.makeText(Login.this, "Error: "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                           showProgress(false);
                       }
                   });
               }
               else
               {
                   showProgress(false);
               }
           }

           @Override
           public void handleFault(BackendlessFault fault) {
               Toast.makeText(Login.this, "Error: "+fault.getMessage(), Toast.LENGTH_SHORT).show();
               showProgress(false);
           }
       });
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


}