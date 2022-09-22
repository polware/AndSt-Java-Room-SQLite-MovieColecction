package com.example.mymoviesandseries;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mymoviesandseries.adapters.CollectionAdapter;
import com.example.mymoviesandseries.models.ListGenres;
import com.example.mymoviesandseries.models.MyCollection;
import com.example.mymoviesandseries.viewmodel.CollectionViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/*
Ordem de diseño del proyecto:
-> Modelo, Interface, local DB, Repository, ViewModel, MainActivity, AddItem, Update, Adapter, other Activities
 */

public class MainActivity extends AppCompatActivity {
    private CollectionViewModel collectionViewModel;
    private ActivityResultLauncher<Intent> activityResultLauncherForNewItem;
    private ActivityResultLauncher<Intent> activityResultLauncherForUpdate;
    private RecyclerView recyclerView;
    private TextView textViewTitle, textViewDescription, textViewYear, textViewGenre;
    private FloatingActionButton floatingActionButton;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Favorite Movies & TV series");
        setContentView(R.layout.activity_main);

        registerActivityForNewItem();
        registerActivityForUpdate();
        recyclerView = findViewById(R.id.recyclerView);
        floatingActionButton = findViewById(R.id.fab_add);
        textViewTitle = findViewById(R.id.textViewTitleMain);
        textViewYear = findViewById(R.id.textViewYearMain);
        textViewGenre = findViewById(R.id.textViewGenreMain);
        textViewDescription = findViewById(R.id.textViewDescriptionMain);
        ratingBar = findViewById(R.id.ratingBarMain);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CollectionAdapter adapter = new CollectionAdapter();
        recyclerView.setAdapter(adapter);

        collectionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(CollectionViewModel.class);
        collectionViewModel.getCollection().observe(MainActivity.this, new Observer<List<MyCollection>>() {
            @Override
            public void onChanged(List<MyCollection> myCollections) {
                adapter.setMyListCollection(myCollections);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                //ActivityResultLauncher
                activityResultLauncherForNewItem.launch(intent);
            }
        });

        //Method for delete item from database
        //First parameter is for Drag and Drop
        //Secod parameter is of Swipe movement
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                collectionViewModel.delete(adapter.getPosition(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        //Define listener for update item selected
        adapter.setListener(new CollectionAdapter.itemClickListener() {
            @Override
            public void onItemClick(MyCollection myCollection) {
                int genreID = ListGenres.getIndex(myCollection.getItemGenre());
                Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                intent.putExtra("id", myCollection.getCollectionID());
                intent.putExtra("title", myCollection.getItemTitle());
                intent.putExtra("year", myCollection.getItemYear());
                intent.putExtra("genre", genreID);
                intent.putExtra("description", myCollection.getItemDescription());
                intent.putExtra("score", myCollection.getItemScore());
                intent.putExtra("image", myCollection.getItemImage());
                //ActivityResultLauncher
                activityResultLauncherForUpdate.launch(intent);
            }
        });

    }

    //Capture the data from AddItem Activity
    public void registerActivityForNewItem(){
        activityResultLauncherForNewItem = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        //Receive data to saving in Database
                        int resultcode = result.getResultCode();
                        Intent data = result.getData();
                        if(resultcode == RESULT_OK && data != null){
                            String title = data.getStringExtra("title");
                            int year = data.getIntExtra("year", -1);
                            String genre = data.getStringExtra("genre");
                            String description = data.getStringExtra("description");
                            float score = data.getFloatExtra("score", -1);
                            byte[] itemImage = data.getByteArrayExtra("image");
                            MyCollection myCollection = new MyCollection(title, year, genre, description, score, itemImage);
                            collectionViewModel.insert(myCollection);
                        }
                    }
        });
    }

    //Capture the data from Update Activity
    public void registerActivityForUpdate() {
        activityResultLauncherForUpdate = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultcode = result.getResultCode();
                        Intent data = result.getData();
                        if(resultcode == RESULT_OK && data != null){
                            int id = data.getIntExtra("id", -1);
                            String title = data.getStringExtra("updatetitle");
                            int year = data.getIntExtra("updateyear", -1);
                            String genre = data.getStringExtra("updategenre");
                            String description = data.getStringExtra("updatedescription");
                            float score = data.getFloatExtra("updatescore", -1);
                            byte[] itemImage = data.getByteArrayExtra("image");
                            MyCollection myCollection = new MyCollection(title, year, genre, description, score, itemImage);
                            myCollection.setCollectionID(id);
                            collectionViewModel.update(myCollection);
                        }
                    }
        });
    }

}