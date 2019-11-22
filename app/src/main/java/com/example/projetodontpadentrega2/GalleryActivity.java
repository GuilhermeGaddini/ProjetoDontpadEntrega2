package com.example.projetodontpadentrega2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class GalleryActivity extends AppCompatActivity {

    private EditText tagEditText;
    private Button buttonPicture;
    private Button buttonSave;
    private ImageView imageView;
    private Uri imageURI;
    private CollectionReference mMsgsReference;
    private StorageReference storageReference;
    private FirebaseUser fireUser;
    private String tagEditTextSave;
    private String mainActivityTextBoxSave;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fireUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("Images");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        tagEditText = findViewById(R.id.tagEditText);
        buttonPicture = findViewById(R.id.galleryButtonPicture);// Open Gallery
        buttonSave = findViewById(R.id.saveButtonText);// Save Button
        imageView = findViewById(R.id.galleryImageView);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buttonPicture.setOnClickListener(v -> openGallery());
        buttonSave.setOnClickListener(v -> {
            uploadFile();
            Intent intent = new Intent(GalleryActivity.this, com.example.projetodontpadentrega2.ChatActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        intent = getIntent();
        tagEditTextSave = intent.getStringExtra("tag_save");
        mainActivityTextBoxSave = intent.getStringExtra("text_save");
        tagEditText.setText(intent.getStringExtra("tag_save"));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            tagEditTextSave = tagEditText.getText().toString();
            Intent intent = new Intent(GalleryActivity.this, TagAndTextActivity.class);
            intent.putExtra("tag_save",tagEditTextSave);
            intent.putExtra("text_save", mainActivityTextBoxSave);
            startActivity(intent);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, Integer.parseInt(getString(R.string.PICK_IMAGE_REQUEST)));
    }

    public void takePhoto(View view) {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (camera.resolveActivity(getPackageManager()) != null){
            startActivityForResult(camera,Integer.parseInt(getString(R.string.REQ_CODE_CAMERA)));
        }else{
            Toast.makeText(this,getString(R.string.no_camera),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Integer.parseInt(getString(R.string.PICK_IMAGE_REQUEST))
            && data != null && data.getData() != null ){
            imageURI = data.getData();
            imageView.setImageURI(imageURI);
        }else if (resultCode == RESULT_OK && requestCode == Integer.parseInt(getString(R.string.REQ_CODE_CAMERA)) ){
            Bitmap picture = (Bitmap) data.getExtras().get("data");
            Toast.makeText(this, R.string.gallery, Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (imageURI != null && mainActivityTextBoxSave.trim() != "" && tagEditTextSave.trim() != "") {
            mMsgsReference = FirebaseFirestore.getInstance().collection("mensagens");
            FirebaseUser fireUser = FirebaseAuth.getInstance().getCurrentUser();
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageURI));
            fileReference.putFile(imageURI).addOnSuccessListener(taskSnapshot -> {
                TagContent tagcontent = new TagContent(mainActivityTextBoxSave,tagEditTextSave,fireUser.getEmail(),taskSnapshot.getMetadata().getReference()
                        .getDownloadUrl().toString());
                        mMsgsReference.add(tagcontent);
                        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, R.string.missingField, Toast.LENGTH_LONG).show();
        }
    }
}