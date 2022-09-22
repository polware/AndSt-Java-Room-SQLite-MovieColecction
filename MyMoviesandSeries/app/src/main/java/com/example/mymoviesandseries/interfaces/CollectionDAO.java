package com.example.mymoviesandseries.interfaces;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mymoviesandseries.models.MyCollection;

import java.util.List;

@Dao
public interface CollectionDAO {

    @Insert
    void insert(MyCollection myCollection);

    @Delete
    void delete(MyCollection myCollection);

    @Update
    void update(MyCollection myCollection);

    @Query("SELECT * FROM my_collection ORDER BY collectionID ASC")
    LiveData<List<MyCollection>>getAllCollection();

}
