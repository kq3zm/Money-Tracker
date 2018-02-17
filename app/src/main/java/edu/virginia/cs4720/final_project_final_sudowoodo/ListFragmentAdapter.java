package edu.virginia.cs4720.final_project_final_sudowoodo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.BaseColumns;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class ListFragmentAdapter extends RecyclerView.Adapter <ListFragmentAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView dateTextView;
        public TextView amtTextView;


        public ViewHolder (View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.itemName);
            dateTextView = (TextView) itemView.findViewById(R.id.itemDate);
            amtTextView = (TextView) itemView.findViewById(R.id.itemAmt);

        }
    }

    //store member variable for items
    private ArrayList<ArrayList<String>> spentList;
    //store images for items
    private ArrayList<byte[]> imageList;
    //store context for easy access
    private Context mContext;

    //constructor
    public ListFragmentAdapter (Context context, ArrayList<ArrayList<String>> spentList, ArrayList<byte[]> imageList) {
        this.spentList = spentList;
        this.imageList = imageList;
        mContext = context;
    }

    public Context getContext () {
        return mContext;
    }


    @Override
    public ListFragmentAdapter.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        Context context= parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //inflate custom layout
        View listView = inflater.inflate(R.layout.item_layout, parent, false );

        //return new holder instance
        ViewHolder viewHolder = new ViewHolder(listView);
        return viewHolder;
    }


    //populating data into the item through holder
    @Override
    public void onBindViewHolder (ListFragmentAdapter.ViewHolder viewHolder, final int position) {
        final int pos = position;
        final ListFragmentAdapter.ViewHolder vh = viewHolder;
        //get data model based on position
        try {
            final ArrayList<String> item = spentList.get(position);
            final byte[] imageByte = imageList.get(position);


            //set item views based on views and data model
            TextView nTextView = viewHolder.nameTextView;
            TextView dTextView = viewHolder.dateTextView;
            TextView aTextView = viewHolder.amtTextView;
            nTextView.setText(item.get(0));

//            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String combined = String.valueOf(item.get(4));
//            Date date = new Date();
//            try {
//                date = sdf.parse(combined);
//            } catch (Exception ex) {
//            }

            dTextView.setText(combined);
            aTextView.setText("$"+item.get(2));

            int green = Color.parseColor("#4cd964");
            int orange = Color.parseColor("#ff954f");
            int blue = Color.parseColor("#007aff");
            int red = Color.parseColor("#ff3b30");
            int purple = Color.parseColor("#5856d6");

            dTextView.setText(combined);

            aTextView.setText("$"+item.get(2));

            if (item.get(1).contains("Grocery")){
                nTextView.setTextColor(green);
                aTextView.setTextColor(green);
            }
            else if (item.get(1).contains("Restaurant")){
                nTextView.setTextColor(orange);
                aTextView.setTextColor(orange);
            }
            else if (item.get(1).contains("Clothing")){
                nTextView.setTextColor(blue);
                aTextView.setTextColor(blue);
            }
            else if (item.get(1).contains("Bills")){
                nTextView.setTextColor(red);
                aTextView.setTextColor(red);
            }
            else if (item.get(1).contains("Other")){
                nTextView.setTextColor(purple);
                aTextView.setTextColor(purple);
            }

            viewHolder.nameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getContext(), EditItem.class);
                    intent.putExtra("item", item);
                    intent.putExtra("image", imageByte);
                    mContext.startActivity(intent);
                }
            });

        }
        catch (IndexOutOfBoundsException e){

        }
    }
    //returns total number of items in list
    @Override
    public int getItemCount () {
        return spentList.size();
    }


}
