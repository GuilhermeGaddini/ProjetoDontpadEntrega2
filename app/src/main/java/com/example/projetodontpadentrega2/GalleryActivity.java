package com.example.projetodontpadentrega2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class GalleryActivity extends AppCompatActivity {

    private EditText tagEditText;
    private Button buttonPicture;
    private Button buttonSave;
    private String tagEditTextSave;
    private String mainActivityTextBoxSave;
    private ImageView imageView;
    private Uri imageURI;
    private TagContent tagcontent;
    private Intent intent;

    int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.projetodontpadentrega2.R.layout.activity_gallery);
        tagEditText = findViewById(com.example.projetodontpadentrega2.R.id.tagEditText);
        buttonPicture = findViewById(com.example.projetodontpadentrega2.R.id.galleryButtonPicture);// Open Gallery
        buttonSave = findViewById(com.example.projetodontpadentrega2.R.id.saveButtonText);// Save Button
        imageView = findViewById(com.example.projetodontpadentrega2.R.id.galleryImageView);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buttonPicture.setOnClickListener(v -> openGallery());
        buttonSave.setOnClickListener(v -> {
            Intent intent = new Intent(GalleryActivity.this, ChatActivity.class);
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            tagcontent = new TagContent(mainActivityTextBoxSave,tagEditTextSave,tagcontent.getEmail().replace("@", ""),imageView);
            intent.putExtra("ObjectTagContent", tagcontent);
            startActivity(intent);
            this.finish();
            finish();
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
        startActivityForResult(gallery, Integer.parseInt(getString(com.example.projetodontpadentrega2.R.string.PICK_IMAGE_REQUEST)));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Integer.parseInt(getString(com.example.projetodontpadentrega2.R.string.PICK_IMAGE_REQUEST))){
            if(data.getData() == null){
                Toast.makeText(this, com.example.projetodontpadentrega2.R.string.No_img_on_gallery, Toast.LENGTH_SHORT).show();
            }else{
                imageURI = data.getData();
                imageView.setImageURI(imageURI);
            }

        }else if (resultCode == RESULT_OK && requestCode == Integer.parseInt(getString(com.example.projetodontpadentrega2.R.string.REQ_CODE_CAMERA))){
            Bitmap picture = (Bitmap) data.getExtras().get("data");
        }
    }

    public void takePhoto(View view) {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (camera.resolveActivity(getPackageManager()) != null){
            startActivityForResult(camera,Integer.parseInt(getString(com.example.projetodontpadentrega2.R.string.REQ_CODE_CAMERA)));
        }else{
            Toast.makeText(this,getString(com.example.projetodontpadentrega2.R.string.no_camera),Toast.LENGTH_SHORT).show();
        }

    }

}
