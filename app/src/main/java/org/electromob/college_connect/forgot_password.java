package org.electromob.college_connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgot_password extends AppCompatActivity {

    private EditText emailreset;
    private TextView login,reset;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        login = (TextView) findViewById(R.id.login5);
        reset = (TextView) findViewById(R.id.passwordreset);
        emailreset = (EditText)findViewById(R.id.resetemail);
        firebaseAuth = FirebaseAuth.getInstance();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(forgot_password.this, login.class));
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String useremail = emailreset.getText().toString().trim();

                if (useremail.isEmpty()){
                    Toast.makeText(forgot_password.this,"Enter the email to recieve an reset link ...",Toast.LENGTH_LONG).show();

                }else{
                    firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(forgot_password.this,"Password reset email sent..",Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(forgot_password.this, login.class));
                            }else{
                                Toast.makeText(forgot_password.this,"Error in sending password reset email...",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
