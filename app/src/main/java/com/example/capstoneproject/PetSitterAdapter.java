package com.example.capstoneproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PetSitterAdapter extends FirestoreRecyclerAdapter<PetSitter, PetSitterAdapter.PetSitterHolder> {
    private OnItemClickListener listener;
    private Context context;

    public PetSitterAdapter(@NonNull FirestoreRecyclerOptions<PetSitter> options) {
        super(options);
    }

    public PetSitterAdapter(@NonNull FirestoreRecyclerOptions<PetSitter> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull PetSitterHolder holder, int position, @NonNull PetSitter model) {
        holder.textViewName.setText(model.getfName() + " " + model.getlName());
        //holder.textViewMiles.setText(model.getEmail());
        //holder.textViewRating.setText(model.getlName());

        Picasso.with(context).load(model.getImageUrl()).into(holder.imageView);
        //holder.imageView.setImageResource(model.);
    }

    @NonNull
    @Override
    public PetSitterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sitter_item,
                parent, false);
        context = parent.getContext();
        return new PetSitterHolder(v);
    }

    class PetSitterHolder extends RecyclerView.ViewHolder{
        TextView textViewName;
        //TextView textViewRating;
        //TextView textViewMiles;
        CircleImageView imageView;


        public PetSitterHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.fullName);
            //textViewRating = itemView.findViewById(R.id.rating);
            //textViewMiles = itemView.findViewById(R.id.milesAway);
            imageView = itemView.findViewById(R.id.profilePic);


            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    //position is valid
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }

                }
            });
        }

    }

    //send data from the adapter to the activity
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
