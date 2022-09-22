package com.example.mymoviesandseries.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mymoviesandseries.models.MyCollection;
import com.example.mymoviesandseries.repositories.MyCollectionRepository;

import java.util.List;

public class CollectionViewModel extends AndroidViewModel {
    private MyCollectionRepository repository;
    private LiveData<List<MyCollection>> imagesList;

    public CollectionViewModel(@NonNull Application application) {
        super(application);

        repository = new MyCollectionRepository(application);
        imagesList = repository.getCollection();
    }

    public void insert(MyCollection myCollection){
        repository.insert(myCollection);
    }

    public void delete(MyCollection myCollection){
        repository.delete(myCollection);
    }

    public void update(MyCollection myCollection){
        repository.update(myCollection);
    }

    public LiveData<List<MyCollection>> getCollection(){
        return imagesList;
    }
}
