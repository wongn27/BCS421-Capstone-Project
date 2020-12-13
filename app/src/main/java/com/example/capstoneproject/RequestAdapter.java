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

public class RequestAdapter extends FirestoreRecyclerAdapter<Request, RequestAdapter.RequestHolder> {
    private OnItemClickListener listener;
    private Context context;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = fStore.collection("users").document(FirebaseAuth.getInstance().getUid()).collection("requests");

    public RequestAdapter(@NonNull FirestoreRecyclerOptions<Request> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull RequestHolder holder, int position, @NonNull Request model) {

        holder.textViewName.setText(model.getFirstNameOfUserThatRequested()+ " " + model.getLastNameOfUserThatRequested());
        //holder.textViewName.setText(model.getFullName());
        holder.petName.setText(model.getNameOfPet());

    }

    @NonNull
    @Override
    public RequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item, parent, false  );
        context = parent.getContext();
        return new RequestHolder(v);
    }


    class RequestHolder extends RecyclerView.ViewHolder {
        TextView textViewName, petName;
        CircleImageView imageView;

        public RequestHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.fullName);
            petName = itemView.findViewById(R.id.petName);

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

    public void setOnItemClickListener(RequestAdapter.OnItemClickListener listener){
        this.listener = listener;
    }
}
