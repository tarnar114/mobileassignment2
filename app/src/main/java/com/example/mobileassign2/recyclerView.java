package com.example.mobileassign2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import objects.Location;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;
//recycler for app to display locations linearly as cards
public  class recyclerView  extends RecyclerView.Adapter<recyclerView.myViewHolder> implements Filterable {
    private Context context;
    private ArrayList<Location> displayLocations;
    private ArrayList<Location> allLocations;
    private CharSequence previousSearch;


    //constructor for each card
    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView address,longitude,latitude;
        CardView cardView;
        ImageButton edit,del;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            address=itemView.findViewById(R.id.address);
            longitude=itemView.findViewById(R.id.longitude);
            latitude=itemView.findViewById(R.id.latitude);
            cardView=itemView.findViewById(R.id.recycler_row_container);
            edit=itemView.findViewById(R.id.edit);
            del=itemView.findViewById(R.id.del);

        }
    }
    public recyclerView(Context context,ArrayList<Location> availableLocations){
        this.context=context;
        this.displayLocations=availableLocations;
        this.allLocations=new ArrayList<>(displayLocations);
    }
    //create views
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.recycler_row,parent,false);
        return new myViewHolder(view);
    }
    //binds views to data and sets onclicks
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.address.setText(displayLocations.get(position).getAddress());
        holder.longitude.setText(String.valueOf(displayLocations.get(position).getLongitude()));
        holder.latitude.setText(String.valueOf(displayLocations.get(position).getLatitude()));
        Intent intent=new Intent(holder.itemView.getContext(),UpdateLocation.class);
        intent.putExtra("longitude",String.valueOf(displayLocations.get(position).getLongitude()));
        intent.putExtra("latitude",String.valueOf(displayLocations.get(position).getLatitude()));
        intent.putExtra("address",displayLocations.get(position).getAddress());
        intent.putExtra("id",displayLocations.get(position).getId());
        holder.edit.setOnClickListener(view->holder.itemView.getContext().startActivity(intent));

        holder.del.setOnClickListener(view->delLocation(holder.address.getText().toString(),displayLocations.get(position)));
    }
    //delete location event
    public void delLocation(String address,Location deletedLOC){
        SQLHelper db=new SQLHelper(context);
        displayLocations.remove(deletedLOC);
        db.deleteLocation(address);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return displayLocations.size();
    }
    //filter for cards when searching (wasnt able to fully implement this)
    @Override
    public Filter getFilter(){return LocationFIlter;}

    private Filter LocationFIlter=new Filter(){


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Location> filterList=new ArrayList<>();
            if (constraint==null||constraint.length()==0){

            }
            else{
                String filterPattern=constraint.toString().toLowerCase().trim();
                for (int i=0;i<allLocations.size();i++){
                    if (allLocations.get(i).getAddress().toLowerCase().contains(filterPattern)){
                        filterList.add(allLocations.get(i));
                    }
                }
            }
            FilterResults res=new FilterResults();
            res.values=filterList;
            return res;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values!=null) {
                previousSearch=constraint;
                displayLocations.clear();
                displayLocations.addAll((ArrayList<Location>)results.values);
                notifyDataSetChanged();
            }else if(!constraint.equals(previousSearch)) {
                displayLocations.clear();
                notifyDataSetChanged();
            }
        }
    };
}
