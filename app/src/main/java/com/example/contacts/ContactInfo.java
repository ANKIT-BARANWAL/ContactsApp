package com.example.contacts;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class ContactInfo extends AppCompatActivity {
TextView tvchar,tvname;
ImageView call,mail,edit,delete;
EditText name,email,number;
Button button;
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);
        tvchar=findViewById(R.id.tvchar);
        tvname=findViewById(R.id.tvname);
        call=findViewById(R.id.call);
        mail=findViewById(R.id.mail);
        edit=findViewById(R.id.edit);
        delete=findViewById(R.id.delete);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);
        number=findViewById(R.id.number);
        button=findViewById(R.id.button);

        name.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        number.setVisibility(View.GONE);
        button.setVisibility(View.GONE);

        final int index = getIntent().getIntExtra("index",0);
        name.setText(ApplicationClass.contacts.get(index).getName());
        email.setText(ApplicationClass.contacts.get(index).getEmail());
        number.setText(ApplicationClass.contacts.get(index).getNumber());
        tvchar.setText(ApplicationClass.contacts.get(index).getName().toUpperCase().charAt(0)+"");
        tvname.setText(ApplicationClass.contacts.get(index).getName());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty() || number.getText().toString().isEmpty() || email.getText().toString().isEmpty())
                {
                    Toast.makeText(ContactInfo.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ApplicationClass.contacts.get(index).setName(name.getText().toString().trim());
                    ApplicationClass.contacts.get(index).setName(number.getText().toString().trim());
                    ApplicationClass.contacts.get(index).setName(email.getText().toString().trim());
                    showProgress(true);
                    tvLoad.setText("UPDATING CONTACTS... PLEASE WAIT...");
                    Backendless.Persistence.save(ApplicationClass.contacts.get(index), new AsyncCallback<Contact>() {
                        @Override
                        public void handleResponse(Contact response) {
                            tvchar.setText(ApplicationClass.contacts.get(index).getName().toUpperCase().charAt(0)+"");
                            tvname.setText(ApplicationClass.contacts.get(index).getName());
                            Toast.makeText(ContactInfo.this, "Contact successfully updated!!!", Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(ContactInfo.this, "Error: "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }
                    });
                }
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + ApplicationClass.contacts.get(index).getNumber();
                Intent intent  = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);

            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL,""+ApplicationClass.contacts.get(index).getEmail());
                startActivity(Intent.createChooser(intent,"SEND MAIL TO " + ApplicationClass.contacts.get(index).getName()));

            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag  = !flag;
                if(flag)
                {
                    name.setVisibility(View.VISIBLE);
                    email.setVisibility(View.VISIBLE);
                    number.setVisibility(View.VISIBLE);
                    button.setVisibility(View.VISIBLE);
                }
                else
                {
                    name.setVisibility(View.GONE);
                    email.setVisibility(View.GONE);
                    number.setVisibility(View.GONE);
                    button.setVisibility(View.GONE);
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(ContactInfo.this);
                dialog.setMessage("Are you sure you want to delete the contact!!!");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showProgress(true);
                        tvLoad.setText("Deleting contact... please wait...");
                        Backendless.Persistence.of(Contact.class).remove(ApplicationClass.contacts.get(index), new AsyncCallback<Long>() {
                            @Override
                            public void handleResponse(Long response) {
                                ApplicationClass.contacts.remove(index);
                                Toast.makeText(ContactInfo.this, "Contact successfully removed!!", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                ContactInfo.this.finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(ContactInfo.this, "Error: "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                                showProgress(false);
                            }
                        });
                    }
                });
                dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog=dialog.create();
                alertDialog.show();

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