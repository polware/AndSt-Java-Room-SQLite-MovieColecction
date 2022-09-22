package com.example.mymoviesandseries.adapters;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymoviesandseries.R;
import com.example.mymoviesandseries.models.MyCollection;

import java.util.ArrayList;
import java.util.List;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.myCollectionHolder> {

    List<MyCollection> myListCollection = new ArrayList<>();
    private itemClickListener listener;

    public void setMyListCollection(List<MyCollection> myListCollection) {
        this.myListCollection = myListCollection;
        notifyDataSetChanged();
    }

    public class myCollectionHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textViewTitle, textViewDescription, textViewYear, textViewGenre;
        RatingBar ratingBar;

        public myCollectionHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewTitle = itemView.findViewById(R.id.textViewTitleMain);
            textViewYear = itemView.findViewById(R.id.textViewYearMain);
            textViewGenre = itemView.findViewById(R.id.textViewGenreMain);
            textViewDescription = itemView.findViewById(R.id.textViewDescriptionMain);
            ratingBar = itemView.findViewById(R.id.ratingBarMain);

            //Card listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(myListCollection.get(position));
                    }
                }
            });
        }
    }

    //Setter and Interface methods for clickListener on Item
    public void setListener(itemClickListener listener) {
        this.listener = listener;
    }

    public interface itemClickListener {
        void onItemClick(MyCollection myCollection);
    }

    //Method for return object position for delete
    public MyCollection getPosition(int position) {
        return myListCollection.get(position);
    }

    @NonNull
    @Override
    public myCollectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_card, parent, false);
        return new myCollectionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myCollectionHolder holder, int position) {
        MyCollection myCollection = myListCollection.get(position);
        holder.textViewTitle.setText(myCollection.getItemTitle());
        holder.textViewYear.setText(Integer.toString(myCollection.getItemYear()));
        holder.textViewGenre.setText(myCollection.getItemGenre());
        holder.textViewDescription.setText(myCollection.getItemDescription());
        holder.ratingBar.setRating(myCollection.getItemScore());
        holder.ratingBar.setIsIndicator(true);
        holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(myCollection.getItemImage(),
                0, myCollection.getItemImage().length));
    }

    @Override
    public int getItemCount() {
        return myListCollection.size();
    }

}
