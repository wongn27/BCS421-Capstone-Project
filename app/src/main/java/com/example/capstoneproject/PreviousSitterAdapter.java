package com.example.capstoneproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class PreviousSitterAdapter extends FirestoreRecyclerAdapter<Review, PreviousSitterAdapter.PreviousSitterHolder> {
    private OnItemClickListener listener;
    private Context context;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    public PreviousSitterAdapter(@NonNull FirestoreRecyclerOptions<Review> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull PreviousSitterHolder holder, int position, @NonNull Review model) {

        holder.textViewName.setText(model.getfName()+ " " + model.getlName());
        //holder.textViewName.setText(model.getFullName());
        //holder.petName.setText(model.getNameOfPet());

    }

    @NonNull
    @Override
    public PreviousSitterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.previous_sitter_item, parent, false  );
        context = parent.getContext();
        return new PreviousSitterHolder(v);
    }


    class PreviousSitterHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        CircleImageView imageView;

        public PreviousSitterHolder(@NonNull View itemView) {
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

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(PreviousSitterAdapter.OnItemClickListener listener){
        this.listener = listener;
    }
}