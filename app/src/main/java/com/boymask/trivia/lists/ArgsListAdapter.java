package com.boymask.trivia.lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.boymask.trivia.MainActivity;
import com.boymask.trivia.R;
import com.boymask.trivia.network.Category;

import java.util.ArrayList;
import java.util.List;

public class ArgsListAdapter  extends
        RecyclerView.Adapter<ArgsListAdapter.ViewHolder> {

    private  MainActivity main=null;
    private List<Category> mContacts;

    // Pass in the contact array into the constructor
    public ArgsListAdapter(MainActivity main,List<Category> contacts) {
        mContacts = contacts;
        this.main=main;
    }
    public ArgsListAdapter(MainActivity main) {
        mContacts = new ArrayList<>();
        this.main=main;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.args_list_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category contact = mContacts.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.nameTextView;
        textView.setText(contact.getName());
//        Button button = holder.messageButton;
//        button.setText(contact.isOnline() ? "Message" : "Offline");
//        button.setEnabled(contact.isOnline());
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        private AdapterView.OnItemClickListener listener;
      //  public Button messageButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.argName);
        //    messageButton = (Button) itemView.findViewById(R.id.message_button);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                Category cat = mContacts.get(position);
                // We can access the data within the views
                System.out.println( cat.getName());
                main.setTriviaCat(cat);

            }
        }
    }
}