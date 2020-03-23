package org.electromob.college_connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity {

    private Button signup;
    private TextView slogin;
    private EditText signup_email,signup_password,signup_confrim_password;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reff;
    private FirebaseUser firebaseUser;
    String email1,np,ncp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup = (Button)findViewById(R.id.button4);
        slogin = findViewById(R.id.slogin);
        signup_email = findViewById(R.id.signup_email);
        signup_password = findViewById(R.id.signup_password);
        signup_confrim_password = findViewById(R.id.signup_confirm_password);
        firebaseAuth = FirebaseAuth.getInstance();

        slogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),login.class));
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                np = signup_password.getText().toString();
                ncp = signup_confrim_password.getText().toString();
                email1 = signup_email.getText().toString().trim();

                if (np.equals(ncp)) {


                    if (email1.isEmpty() || np.isEmpty() || ncp.isEmpty()) {
                        Toast.makeText(signup.this, "Enter all the feilds....", Toast.LENGTH_SHORT).show();
                    } else {

                        firebaseAuth.createUserWithEmailAndPassword(email1, np).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    sendemail();
                                } else {
                                    Toast.makeText(signup.this, "registration failed....", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                        Toast.makeText(signup.this, "Data taken ....", Toast.LENGTH_LONG).show();

                    }
                }
                else {
                    Toast.makeText(signup.this,"Entered password doesnt match:..",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void sendemail(){
        firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        firebaseDatabase = FirebaseDatabase.getInstance();
                        reff = firebaseDatabase.getReference("users").child(firebaseAuth.getUid());
                        userinfo userinfo;
                        userinfo = new userinfo(email1, np, ncp);
                        reff.setValue(userinfo);
                        Toast.makeText(signup.this,"Successfully registered, verification mail has sent",Toast.LENGTH_LONG).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(signup.this, login.class));
                    }else{
                        Toast.makeText(signup.this,"Verification mail failed to send try after sometime", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

}
