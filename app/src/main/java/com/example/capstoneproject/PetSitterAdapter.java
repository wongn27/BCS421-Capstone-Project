package com.example.capstoneproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PetSitterAdapter extends RecyclerView.Adapter<PetSitterAdapter.PetSitterHolder> {
    //private OnItemClickListener listener;
    //private Context context;
    List<PetSitter> petSitterList;
    List<CardView> cardViewList;
    OnPetSitterListener onPetSitterListener;
    //LocalBroadcastManager localBroadcastManager;


    public PetSitterAdapter(List<PetSitter> petSitterList, OnPetSitterListener onPetSitterListener) {
        //this.context = context;
        this.petSitterList = petSitterList;
        cardViewList = new ArrayList<>();
        this.onPetSitterListener = onPetSitterListener;
        //localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public PetSitterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sitter_item,
                parent, false);
        //context = parent.getContext();
        return new PetSitterHolder(v, onPetSitterListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final PetSitterHolder holder, int i) {
        holder.textViewName.setText(petSitterList.get(i).getfName() + " " + petSitterList.get(i).getlName());
        //holder.textViewMiles.setText(model.getEmail());
        //holder.textViewRating.setText(model.getlName());

        if(!cardViewList.contains((holder.cardView))){
            cardViewList.add(holder.cardView);
        }

        //Picasso.with(context).load(model.getImageUrl()).into(holder.imageView);
        //holder.imageView.setImageResource(model.);

    }

    @Override
    public int getItemCount() {
        return petSitterList.size();
    }


    public class PetSitterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewName;
        //TextView textViewRating;
        //TextView textViewMiles;
        CircleImageView imageView;
        CardView cardView;
        OnPetSitterListener onPetSitterListener;

        public PetSitterHolder(View itemView, OnPetSitterListener onPetSitterListener) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            textViewName = itemView.findViewById(R.id.fullName);
            //textViewRating = itemView.findViewById(R.id.rating);
            //textViewMiles = itemView.findViewById(R.id.milesAway);
            imageView = itemView.findViewById(R.id.profilePic);
            this.onPetSitterListener = onPetSitterListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onPetSitterListener.onPetSitterClick(getAdapterPosition());
        }
    }

    public interface OnPetSitterListener{
        void onPetSitterClick(int position);
    }


    /*//send data from the adapter to the activity
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }*/
}
