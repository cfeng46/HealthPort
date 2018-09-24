package com.example.cfeng.healthport;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cfeng.healthport.Model.Contact;

import java.util.ArrayList;
import java.util.Map;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private static ArrayList<Contact> contacts;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public MyViewHolder(TextView v) {
            super(v);
            mTextView = v;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contact.setContactName((String) mTextView.getText());
                    for (Contact c : contacts) {
                        if (c.getName().equals(mTextView.getText())) {
                            contact.setContactNumber(c.getNumber());
                        }
                    }

                    Context context = v.getContext();
                    Intent intent = new Intent(context, contact.class);
                    context.startActivity(intent);
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Contact> dataset) {
        contacts = dataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTextView.setText(contacts.get(position).getName());
        if (position % 2 == 0) {
            holder.mTextView.setBackgroundResource(R.drawable.dark_background);
        } else {
            holder.mTextView.setBackgroundResource(R.drawable.light_background);
        }

        /*holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(contacts_home.this, home.class));
                finish();
            }
        });*/
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return contacts.size();
    }
}