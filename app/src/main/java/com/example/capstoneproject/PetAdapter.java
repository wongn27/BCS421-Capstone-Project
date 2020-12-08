package com.example.capstoneproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class PetAdapter extends FirestoreRecyclerAdapter<Pet, PetAdapter.PetHolder> {

    public PetAdapter(@NonNull FirestoreRecyclerOptions<Pet> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PetHolder petHolder, int i, @NonNull Pet pet) {
        petHolder.textViewPetName.setText(pet.getName());
        petHolder.textViewSex.setText(pet.getSex());
        petHolder.textViewType.setText(pet.getType());
        petHolder.textViewAge.setText(pet.getAge());
        petHolder.textViewWeight.setText(pet.getWeight());
        petHolder.textViewSpecialCareNeeds.setText(pet.getSpecialCareNeeds());
    }

    @NonNull
    @Override
    public PetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_item, parent, false);
        return new PetHolder(v);
    }

    class PetHolder extends RecyclerView.ViewHolder {
        TextView textViewPetName;
        TextView textViewSex;
        TextView textViewType;
        TextView textViewAge;
        TextView textViewWeight;
        TextView textViewSpecialCareNeeds;

        public PetHolder(@NonNull View itemView) {
            super(itemView);
            textViewPetName = itemView.findViewById(R.id.textViewPetName);
            textViewSex = itemView.findViewById(R.id.textViewSex);
            textViewType = itemView.findViewById(R.id.textViewType);
            textViewAge = itemView.findViewById(R.id.textViewAge);
            textViewWeight = itemView.findViewById(R.id.textViewWeight);
            textViewSpecialCareNeeds = itemView.findViewById(R.id.textViewSpecialCareNeeds);
        }
    }
}
