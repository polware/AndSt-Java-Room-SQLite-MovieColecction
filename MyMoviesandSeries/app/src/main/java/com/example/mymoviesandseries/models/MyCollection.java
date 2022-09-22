package com.example.mymoviesandseries.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "my_collection")
public class MyCollection {
    @PrimaryKey(autoGenerate = true)
    public int collectionID;
    public String itemTitle;
    public int itemYear;
    public String itemGenre;
    public String itemDescription;
    public float itemScore;
    public byte[] itemImage;

    public MyCollection(String itemTitle, int itemYear, String itemGenre, String itemDescription, float itemScore, byte[] itemImage) {
        this.itemTitle = itemTitle;
        this.itemYear = itemYear;
        this.itemGenre = itemGenre;
        this.itemDescription = itemDescription;
        this.itemScore = itemScore;
        this.itemImage = itemImage;
    }

    public int getCollectionID() {
        return collectionID;
    }

    public void setCollectionID(int collectionID) {
        this.collectionID = collectionID;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public int getItemYear() {
        return itemYear;
    }

    public String getItemGenre() { return itemGenre; }

    public String getItemDescription() {
        return itemDescription;
    }

    public float getItemScore() {
        return itemScore;
    }

    public byte[] getItemImage() {
        return itemImage;
    }

}
