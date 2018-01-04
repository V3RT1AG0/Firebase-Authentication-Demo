package com.example.v3rt1ag0.smartgridsystem;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity
{
    Button reset;
    EditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        reset = findViewById(R.id.reset_password);
        email = findViewById(R.id.email_edittext);
        reset.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(email.getText().length()==0)
                    return;
                FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPasswordActivity.this,"Password reset link has been sent to your email",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ForgotPasswordActivity.this,Splash.class));
                                }
                                else
                                {
                                    Toast.makeText(ForgotPasswordActivity.this,"Invalid email address",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
