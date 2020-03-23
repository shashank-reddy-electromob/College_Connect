package org.electromob.college_connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class upload extends AppCompatActivity {

    final static int PICK_PDF_CODE = 2342;
    private static final int PICK_IMAGE = 123;
    TextView textViewStatus;
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    ProgressBar progressBar;
    ImageView uploadingimage;
    private Uri imagepath;
    private ProgressDialog progressDialog;
    private StorageTask muploadtask;
    Button upload,imageupload;
    private FirebaseAuth firebaseAuth;
    String Quote;
    StorageReference mStorageReference,mstorageref;
    TextView Uploadingtext;
    FirebaseStorage firebaseStorage;
    Button textupload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        uploadingimage = findViewById(R.id.uploadingimage);
        mstorageref = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        imageupload = findViewById(R.id.imageupload);
        reference = firebaseDatabase.getReference("Quotes").child("" + System.currentTimeMillis());
        Uploadingtext = findViewById(R.id.uploadingtext);
        firebaseAuth = FirebaseAuth.getInstance();
        textupload = findViewById(R.id.textupload);
        textViewStatus = findViewById(R.id.textViewStatus);
        progressBar = findViewById(R.id.progressbar);
        firebaseStorage = FirebaseStorage.getInstance();
        upload = findViewById(R.id.buttonUploadFile);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPDF();
            }
        });


        final StorageReference storageReference = firebaseStorage.getReference("users");
        StorageReference reff = storageReference.child(firebaseAuth.getUid()).child("Property image");


        uploadingimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(getApplicationContext(), "Select from Camera roll: ", Toast.LENGTH_SHORT).show();
                if (muploadtask != null && muploadtask.isInProgress()) {
                    Toast.makeText(getApplicationContext(), "Upload in progress ...", Toast.LENGTH_SHORT).show();

                } else {

                    openfile();

                }
            }
        });

        textupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Quote = Uploadingtext.getText().toString().trim();

                reference.setValue(Quote);
            }
        });


    }

    private void openfile() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE);
        imageupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (imagepath != null ){
                    progressDialog.setMessage("Relax a bit we are uploading your pic ...");
                    progressDialog.show();
                    StorageReference fileReference = mstorageref.child(firebaseAuth.getUid()).child("Property image");

                    muploadtask = fileReference.putFile(imagepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Process success",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Process failed",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(),"IMAGE NOT SELECTED: ",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getPDF() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return;
        }


        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Document"), PICK_PDF_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            if (data.getData() != null) {
                Toast.makeText(this,"Working",Toast.LENGTH_SHORT).show();
                uploadFile(data.getData());
            }else{
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {


            //Toast.makeText(aadhar.this,"test2",Toast.LENGTH_SHORT).show();

            imagepath = data.getData();

            Picasso.get().load(imagepath).into(uploadingimage);

        }
    }



    private void uploadFile(Uri data) {

        progressBar.setVisibility(View.VISIBLE);
        StorageReference sRef = mStorageReference.child("users").child("uploads" + System.currentTimeMillis() + ".pdf");
        sRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        textViewStatus.setText("File Uploaded Successfully");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        textViewStatus.setText((int) progress + "% Uploading...");
                    }
                });

    }

}
