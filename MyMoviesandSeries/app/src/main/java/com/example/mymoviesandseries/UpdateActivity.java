package com.example.mymoviesandseries;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class UpdateActivity extends AppCompatActivity {
    private String title, description, genre;
    private int id, year, genreID;
    private float score;
    private byte[] image;
    private ImageView imageViewImageUpdate;
    private EditText editTextTitleUpdate, editTextDescriptionUpdate, editTextYearUpdate;
    private Spinner spinnerGenre;
    private RatingBar ratingBarUpdate;
    private Button buttonUpdate;
    private Bitmap selectedImage;
    private Bitmap reducedImage;
    ActivityResultLauncher<Intent> activityResultLauncherUpdateData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Update Movie/Serie");
        setContentView(R.layout.activity_update);

        initializeUpdate();
        retrieveData();
        registerActivityForUpdateData();

        ratingBarUpdate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float value, boolean fromUser) {
                score = value;
            }
        });

        imageViewImageUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //startActivityForResult --> before API 30
                //ActivityResultLauncher -> currently
                activityResultLauncherUpdateData.launch(intent);
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });
    }

    private void initializeUpdate() {
        imageViewImageUpdate = findViewById(R.id.imageViewImageUpdate);
        editTextTitleUpdate = findViewById(R.id.editTextTitleUpdate);
        editTextYearUpdate = findViewById(R.id.editTextYearUpdate);
        spinnerGenre = findViewById(R.id.spinnerGenreUpdate);
        editTextDescriptionUpdate = findViewById(R.id.editTextDescriptionUpdate);
        ratingBarUpdate = findViewById(R.id.ratingBarUpdate);
        buttonUpdate = findViewById(R.id.buttonUpdate);
    }

    private void retrieveData() {
        id = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        year = getIntent().getIntExtra("year", -1);
        genreID = getIntent().getIntExtra("genre", -1);
        description = getIntent().getStringExtra("description");
        score = getIntent().getFloatExtra("score", -1);
        image = getIntent().getByteArrayExtra("image");

        editTextTitleUpdate.setText(title);
        editTextYearUpdate.setText(Integer.toString(year));
        //Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,
                ListGenres.getNames());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenre.setAdapter(spinnerAdapter);
        spinnerGenre.setSelection(genreID);
        editTextDescriptionUpdate.setText(description);
        ratingBarUpdate.setRating(score);
        imageViewImageUpdate.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
    }

    private void updateData() {
        if(id == -1){
            Toast.makeText(UpdateActivity.this, "There is a problem!", Toast.LENGTH_SHORT).show();
        }
        else {
            title = editTextTitleUpdate.getText().toString();
            year = Integer.parseInt(editTextYearUpdate.getText().toString());
            genre = spinnerGenre.getSelectedItem().toString();
            description = editTextDescriptionUpdate.getText().toString();
            Intent intent = new Intent();
            intent.putExtra("id", id);
            intent.putExtra("updatetitle", title);
            intent.putExtra("updateyear", year);
            intent.putExtra("updategenre", genre);
            intent.putExtra("updatedescription", description);
            intent.putExtra("updatescore", score);

            if(selectedImage == null){
                intent.putExtra("image", image);
            }
            else {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                //Here is compressed the image selected
                reducedImage = reduceImageSize(selectedImage, 300);
                reducedImage.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
                //Sending data to be saved in Database
                byte[] itemImage = outputStream.toByteArray();
                intent.putExtra("image", itemImage);
            }
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void registerActivityForUpdateData() {
        activityResultLauncherUpdateData = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        Intent data = result.getData();
                        if(resultCode == RESULT_OK && data != null){
                            try {
                                selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                                imageViewImageUpdate.setImageBitmap(selectedImage);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
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