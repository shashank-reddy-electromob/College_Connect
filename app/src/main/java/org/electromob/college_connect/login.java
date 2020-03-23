package org.electromob.college_connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.widget.Toast.LENGTH_SHORT;

public class login extends AppCompatActivity {

    private Button login;
    private TextView forgot,lsignup,message;
    EditText login_email,login_password;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button)findViewById(R.id.button);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        lsignup = findViewById(R.id.lsignup);
        forgot = findViewById(R.id.forgot);
        message = findViewById(R.id.message);
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null){
            finish();
            startActivity(new Intent(login.this, home.class));
        }

        lsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), signup.class));
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),forgot_password.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name =  login_email.getText().toString().trim();
                String password =  login_password.getText().toString().trim();

                if (name.isEmpty() && password.isEmpty()){
                    Toast.makeText(login.this, "Entered username or password are blank or incorrect", LENGTH_SHORT).show();
                    login.setEnabled(true);
                }
                else {
                    progressDialog.setMessage("Relax a bit we are loging in ...");
                    progressDialog.show();

                    mAuth.signInWithEmailAndPassword(name,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(login.this,"Login successfull...", LENGTH_SHORT).show();
                                checkemailverification();
                            }
                            else
                            {
                                //signInWithPhoneAuthCredential(phoneAuthCredential);
                                message.setText("Login failed.... Try Verifying your email ");
                                //Toast.makeText(login.this,"Login failed... Try verifying your email", LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        }
                    });
                }

            }
        });

    }

    private void checkemailverification(){
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();

        if (emailflag){
            finish();
            startActivity(new Intent(login.this, home.class));
        }else{
            Toast.makeText(login.this,"Verify your email..", LENGTH_SHORT).show();
            mAuth.signOut();
        }
    }

}
