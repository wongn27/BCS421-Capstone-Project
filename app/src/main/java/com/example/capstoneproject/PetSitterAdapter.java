package com.example.capstoneproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class PetSitterAdapter extends FirestoreRecyclerAdapter<PetSitter, PetSitterAdapter.PetSitterHolder> {
    private OnItemClickListener listener;

    public PetSitterAdapter(@NonNull FirestoreRecyclerOptions<PetSitter> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PetSitterHolder holder, int position, @NonNull PetSitter model) {
        holder.textViewTitle.setText(model.getfName());
        holder.textViewDescription.setText(model.getlName());
        holder.textViewPriority.setText(model.getEmail());
    }

    @NonNull
    @Override
    public PetSitterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sitter_item,
                parent, false);
        return new PetSitterHolder(v);
    }

    class PetSitterHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewPriority;

        public PetSitterHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.title);
            textViewDescription = itemView.findViewById(R.id.description);
            textViewPriority = itemView.findViewById(R.id.priority);

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
