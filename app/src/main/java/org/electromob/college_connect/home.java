package org.electromob.college_connect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class home extends AppCompatActivity {

    ImageView pdf,comments,posts,nav,logo;
    private FirebaseAuth firebaseAuth;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        pdf = findViewById(R.id.pdf);
        comments = findViewById(R.id.comments);
        posts = findViewById(R.id.posts);
        nav = findViewById(R.id.navigation);
        logo = findViewById(R.id.navlogo);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");


        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(home.this, MainActivity.class));
            }
        });

        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // myRef.setValue("Hello, World!");
                //Toast.makeText(getApplicationContext(),"Sucess",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), org.electromob.college_connect.pdf.class));

            }
        });

        posts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), org.electromob.college_connect.posts.class));
            }
        });

        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), org.electromob.college_connect.comments.class));
            }
        });

        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),upload.class));
            }
        });

    }
}
