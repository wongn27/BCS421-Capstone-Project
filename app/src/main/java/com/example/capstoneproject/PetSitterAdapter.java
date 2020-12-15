package com.example.capstoneproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.lang.String.format;

public class PetSitterAdapter extends RecyclerView.Adapter<PetSitterAdapter.PetSitterHolder> implements Filterable {
    //private OnItemClickListener listener;
    //private Context context;
    List<PetSitter> petSitterList;
    List<PetSitter> petSitterListFull;


    List<CardView> cardViewList;
    OnPetSitterListener onPetSitterListener;


    public PetSitterAdapter(List<PetSitter> petSitterList, OnPetSitterListener onPetSitterListener) {
        //this.context = context;
        this.petSitterList = petSitterList;
        this.petSitterListFull = new ArrayList<>(petSitterList);
        cardViewList = new ArrayList<>();
        this.onPetSitterListener = onPetSitterListener;
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
        PetSitter petSitter = petSitterList.get(i);
        holder.textViewName.setText(petSitter.getfName() + " " + petSitter.getlName());

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
        TextView textViewMiles;
        CircleImageView imageView;
        CardView cardView;
        OnPetSitterListener onPetSitterListener;



        public PetSitterHolder(View itemView, OnPetSitterListener onPetSitterListener) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            textViewName = itemView.findViewById(R.id.fullName);
            //textViewRating = itemView.findViewById(R.id.rating);
            textViewMiles = itemView.findViewById(R.id.milesAway);
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

    @Override
    public Filter getFilter() {
        return petSitterFilter;
    }

    private Filter petSitterFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<PetSitter> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(petSitterListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (PetSitter item: petSitterListFull){
                    if (item.getfName().toLowerCase().contains(filterPattern));{
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            petSitterList.clear();
            petSitterList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
