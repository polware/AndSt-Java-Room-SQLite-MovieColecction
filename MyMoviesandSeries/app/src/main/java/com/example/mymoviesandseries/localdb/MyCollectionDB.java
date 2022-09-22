package com.example.mymoviesandseries.localdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mymoviesandseries.interfaces.CollectionDAO;
import com.example.mymoviesandseries.models.MyCollection;

@Database(entities = MyCollection.class, version = 1)

public abstract class MyCollectionDB extends RoomDatabase {
    private static MyCollectionDB instance;

    public abstract CollectionDAO imagesDAO();

    public static synchronized MyCollectionDB getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MyCollectionDB.class, "my_collection_db")
                    .fallbackToDestructiveMigration()
                    //.allowMainThreadQueries() --> no habilitarlo
                    .build();
        }
        return instance;
    }

}
