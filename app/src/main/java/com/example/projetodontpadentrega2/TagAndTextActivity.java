package com.example.projetodontpadentrega2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class TagAndTextActivity extends AppCompatActivity {

    private EditText tagEditText;
    private EditText mainActivityTextBox;
    private Button buttonPicture;
    private String tagEditTextSave;
    private String mainActivityTextBoxSave;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.projetodontpadentrega2.R.layout.activity_tag_and_text);
        tagEditText = findViewById(com.example.projetodontpadentrega2.R.id.tagEditText);
        mainActivityTextBox = findViewById(com.example.projetodontpadentrega2.R.id.activityMainTextBox);
        buttonPicture = findViewById(com.example.projetodontpadentrega2.R.id.activityMainButtonPicture);
        intent = getIntent();
        tagEditText.setText(intent.getStringExtra("tag_save"));
        mainActivityTextBox.setText(intent.getStringExtra("text_save"));
        buttonPicture.setOnClickListener(view -> {
            tagEditTextSave = tagEditText.getText().toString();
            mainActivityTextBoxSave = mainActivityTextBox.getText().toString();
            intent = new Intent(TagAndTextActivity.this, GalleryActivity.class);
            intent.putExtra("tag_save",tagEditTextSave);
            intent.putExtra("text_save",mainActivityTextBoxSave);
            startActivity(intent);
        });
    }
}
