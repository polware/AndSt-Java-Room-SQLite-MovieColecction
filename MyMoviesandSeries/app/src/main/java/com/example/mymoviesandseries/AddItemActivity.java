package com.example.mymoviesandseries;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mymoviesandseries.models.ListGenres;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddItemActivity extends AppCompatActivity {
    private ImageView imageViewAddImage;
    private EditText editTextTitle, editTextDescription, editTextYear;
    private Spinner spinnerGenre;
    private RatingBar ratingBar;
    private Button buttonSave;
    private Bitmap selectedImage;
    private Bitmap reducedImage;
    private float score;
    ActivityResultLauncher<Intent> activityResultLauncherImageSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("New Movie/Serie");
        setContentView(R.layout.activity_add_item);

        Initialize();
        registerActivityForImageSelected();

        imageViewAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageAddListener();
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float value, boolean fromUser) {
                score = value;
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSaveListener();
            }
        });
    }

    private void Initialize() {
        imageViewAddImage = findViewById(R.id.imageViewAddImage);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextYear = findViewById(R.id.editTextYear);
        editTextDescription = findViewById(R.id.editTextDescription);
        ratingBar = findViewById(R.id.ratingBarNew);
        buttonSave = findViewById(R.id.buttonSaveImage);

        spinnerGenre = findViewById(R.id.spinnerGenre);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,
                ListGenres.getNames());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenre.setAdapter(spinnerAdapter);
    }

    public void registerActivityForImageSelected(){
        activityResultLauncherImageSelected = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        Intent data = result.getData();
                        if(resultCode == RESULT_OK && data != null){
                            try {
                                selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                                imageViewAddImage.setImageBitmap(selectedImage);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
        });
    }

    private void imageAddListener() {
        //Check permission to access Media Storage
        if(ContextCompat.checkSelfPermission(AddItemActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AddItemActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //startActivityForResult --> before API 30
            //ActivityResultLauncher -> currently
            activityResultLauncherImageSelected.launch(intent);
        }
    }

    private void buttonSaveListener() {
        if(selectedImage == null){
            Toast.makeText(AddItemActivity.this, "Please, select a image", Toast.LENGTH_SHORT).show();
        }
        else {
            String title = editTextTitle.getText().toString();
            int year = Integer.parseInt(editTextYear.getText().toString());
            String genre = spinnerGenre.getSelectedItem().toString();
            String description = editTextDescription.getText().toString();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            //Here is compressed the image selected
            reducedImage = reduceImageSize(selectedImage, 300);
            reducedImage.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
            //Sending data to be saved in Database
            byte[] itemImage = outputStream.toByteArray();
            Intent intent = new Intent();
            intent.putExtra("title", title);
            intent.putExtra("year", year);
            intent.putExtra("genre", genre);
            intent.putExtra("description", description);
            intent.putExtra("score", score);
            intent.putExtra("image", itemImage);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    //Method for accessing to Media files
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncherImageSelected.launch(intent);
        }
    }

    //Method that returns scaled Image
    public Bitmap reduceImageSize(Bitmap image, int maxSize){
        int width = image.getWidth();
        int height = image.getHeight();
        float ratio = (float) width / (float) height;
        if(ratio > 1){
            width = maxSize;
            height = (int) (width / ratio);
        }
        else {
            height = maxSize;
            width = (int) (height * ratio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}