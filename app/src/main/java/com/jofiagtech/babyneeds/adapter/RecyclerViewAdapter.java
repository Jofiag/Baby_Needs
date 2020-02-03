package com.jofiagtech.babyneeds.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jofiagtech.babyneeds.R;
import com.jofiagtech.babyneeds.model.Item;

import java.text.MessageFormat;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private Context mContext;
    private List<Item> mItemList;

    public RecyclerViewAdapter(Context context, List<Item> itemList)
    {
        mContext = context;
        mItemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row, parent, false);

        return new ViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, int position) {
        Item item = mItemList.get(position);

        viewHolder.itemName.setText(MessageFormat.format("Article: {0}", item.getItemName()));
        viewHolder.itemColor.setText(MessageFormat.format("Couleur: {0}", item.getItemColor()));
        viewHolder.itemQuantity.setText(MessageFormat.format("Quantité: {0}", String.valueOf(item.getItemQuantity())));
        viewHolder.itemSize.setText(MessageFormat.format("Taille: {0}", String.valueOf(item.getItemSize())));
        viewHolder.dateAdded.setText(MessageFormat.format("Ajouté le: {0}", item.getDateItemAdded()));

    }

    @Override
    public int getItemCount()
    {
        return mItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView itemName;
        TextView itemQuantity;
        TextView itemColor;
        TextView itemSize;
        TextView dateAdded;
        Button editButton;
        Button deleteButton;

        private int id;

        ViewHolder(@NonNull View itemView, Context ctx)
        {
            super(itemView);
            mContext = ctx;

            itemName = itemView.findViewById(R.id.itr_name);
            itemQuantity = itemView.findViewById(R.id.itr_quantity);
            itemColor = itemView.findViewById(R.id.itr_color);
            itemSize = itemView.findViewById(R.id.itr_size);
            dateAdded = itemView.findViewById(R.id.itr_date);

            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int position = getAdapterPosition();

            switch (v.getId())
            {
                case R.id.editButton:
                    break;
                case R.id.deleteButton:
                    break;
                default:
                    break;
            }
        }
    }
}

/*public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Item> itemList;

    public RecyclerViewAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list_row, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Item item = itemList.get(position); // each item object inside of our list


    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView itemName;
        public TextView itemQuantity;
        public TextView itemColor;
        public TextView itemSize;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.itr_name);
            itemQuantity = itemView.findViewById(R.id.itr_quantity);
            itemColor = itemView.findViewById(R.id.item_color);
            itemSize = itemView.findViewById(R.id.item_size);
            dateAdded = itemView.findViewById(R.id.itr_date);

            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            switch (v.getId()){
                case R.id.editButton:
                    break;
                case R.id.deleteButton:
                    break;
                    default:
                        break;
            }
        }
    }
}*/
