package com.example.mymoviesandseries.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.mymoviesandseries.interfaces.CollectionDAO;
import com.example.mymoviesandseries.localdb.MyCollectionDB;
import com.example.mymoviesandseries.models.MyCollection;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyCollectionRepository {
    private CollectionDAO collectionDAO;
    private LiveData<List<MyCollection>> imagesList;
    //Executors
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public MyCollectionRepository(Application application){
        MyCollectionDB database = MyCollectionDB.getInstance(application);
        collectionDAO = database.imagesDAO();
        imagesList = collectionDAO.getAllCollection();
    }

    public void insert(MyCollection myCollection){
        //A new method to do background process without using a UI thread
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                collectionDAO.insert(myCollection);
            }
        });
    }

    public void delete(MyCollection myCollection){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                collectionDAO.delete(myCollection);
            }
        });
    }

    public void update(MyCollection myCollection){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                collectionDAO.update(myCollection);
            }
        });
    }

    public LiveData<List<MyCollection>> getCollection(){
        return imagesList;
    }

}
