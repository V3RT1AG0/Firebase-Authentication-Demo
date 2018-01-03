package com.example.v3rt1ag0.smartgridsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.v3rt1ag0.smartgridsystem.DataGraph.MainActivity;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    SharedPreferences pref;
    Button signUp, signIn;
    TextView email, password,plantID;
    private static final String TAG = "Splash";
    private static final String key = "com.smartgrid";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        signUp = findViewById(R.id.custom_signup_button);
        signIn = findViewById(R.id.custom_signin_button);
        email = findViewById(R.id.email_edittext);
        password = findViewById(R.id.password_edittext);
        plantID = findViewById(R.id.plant_edittext);
        mAuth = FirebaseAuth.getInstance();
        pref= getSharedPreferences(key, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();


        if(pref.getBoolean("authenticated",false)){
            startActivity(new Intent(this,MainActivity.class));
        }


        signUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(password.getText().length()<7)
                {
                    Toast.makeText(Splash.this, "Password must have at least 7 characters",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!plantID.getText().toString().equals("xyz"))
                {
                    Log.d(TAG, plantID.getText().toString());
                    Toast.makeText(Splash.this, "Plant ID is incorrect",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(Splash.this, new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful())
                                {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    email.setText("");
                                    password.setText("");
                                    plantID.setText("");
                                    verifyEmail(user);
                                    //updateUI(user);
                                } else
                                {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(Splash.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    email.setText("");
                                    password.setText("");
                                    // updateUI(null);
                                }

                            }
                        });


            }
        });


        signIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(Splash.this, new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful())
                                {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success ");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user.isEmailVerified())
                                    {
                                        editor.putBoolean("authenticated",true);
                                        editor.commit();
                                        startActivity(new Intent(Splash.this, MainActivity.class));
                                    }
                                    else
                                        Toast.makeText(Splash.this, "Please verify your email", Toast.LENGTH_SHORT).show();

                                } else
                                {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(Splash.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    email.setText("");
                                    password.setText("");
                                }
                            }
                        });
            }
        });
    }

    private void verifyEmail(final FirebaseUser user)
    {
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    Toast.makeText(Splash.this,
                            "Verification email sent to " + user.getEmail(),
                            Toast.LENGTH_SHORT).show();
                } else
                {
                    Log.e(TAG, "sendEmailVerification", task.getException());
                    Toast.makeText(Splash.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
}