package com.jofiagtech.babyneeds.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.jofiagtech.babyneeds.R;
import com.jofiagtech.babyneeds.data.DataBaseHandler;
import com.jofiagtech.babyneeds.model.Item;

import java.text.MessageFormat;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private Context mContext;
    private List<Item> mItemList;
    private AlertDialog.Builder mBuilder;
    private AlertDialog mDialog;
    private LayoutInflater mInflater;
    //private DataBaseHandler db;

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

        ViewHolder(@NonNull View itemView, Context ctx)
        {
            super(itemView);
            mContext = ctx;
            //db = new DataBaseHandler(mContext);

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

        private void deleteItem(final int id) {
            mBuilder = new AlertDialog.Builder(mContext);
            mInflater = LayoutInflater.from(mContext);

            @SuppressLint("InflateParams") View view = mInflater.inflate(R.layout.delete_confirmation_popup, null);

            Button cancelButton = view.findViewById(R.id.cancel_button);
            Button confirmDeleteButton = view.findViewById(R.id.delete_confirmation_button);

            mBuilder.setView(view);
            mDialog = mBuilder.create();
            mDialog.show();

            cancelButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });

            confirmDeleteButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    DataBaseHandler db = new DataBaseHandler(mContext);
                    db.deleteItem(id);
                    mItemList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    mDialog.dismiss();
                }
            });
        }

        private void editItem(final Item newItem) {

            mBuilder = new AlertDialog.Builder(mContext);
            mInflater = LayoutInflater.from(mContext);

            @SuppressLint("InflateParams") View view = mInflater.inflate(R.layout.popup, null);

            TextView title = view.findViewById(R.id.title);
            final EditText babyItem = view.findViewById(R.id.article_name);
            final EditText itemQuantity = view.findViewById(R.id.article_quantity);
            final EditText itemColor = view.findViewById(R.id.item_color);
            final EditText itemSize = view.findViewById(R.id.item_size);
            Button saveButton = view.findViewById(R.id.save_button);

            title.setText(R.string.update_title);
            babyItem.setText(newItem.getItemName());
            itemQuantity.setText(String.valueOf(newItem.getItemQuantity()));
            itemColor.setText(newItem.getItemColor());
            itemSize.setText(String.valueOf(newItem.getItemSize()));
            saveButton.setText(R.string.update_txt);

            mBuilder.setView(view);
            mDialog = mBuilder.create();
            mDialog.show();

            saveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    DataBaseHandler db = new DataBaseHandler(mContext);

                    newItem.setItemName(babyItem.getText().toString().trim());
                    newItem.setItemQuantity(Integer.parseInt(itemQuantity.getText().toString().trim()));
                    newItem.setItemColor(itemColor.getText().toString().trim());
                    newItem.setItemSize(Integer.parseInt(itemSize.getText().toString().trim()));

                    if (!babyItem.getText().toString().isEmpty()
                            && !itemColor.getText().toString().isEmpty()
                            && !itemQuantity.getText().toString().isEmpty()
                            && !itemSize.getText().toString().isEmpty()) {

                        db.updateItem(newItem);
                        notifyItemChanged(getAdapterPosition(), newItem);
                    }
                    else
                        Snackbar.make(v, "Empty Fields not Allowed", Snackbar.LENGTH_SHORT)
                                .show();

                    mDialog.dismiss();
                }
            });
        }

        @Override
        public void onClick(View v) {

            Item item = mItemList.get(getAdapterPosition());

            switch (v.getId()) {
                case R.id.editButton:
                   editItem(item);
                    break;
                case R.id.deleteButton:
                    deleteItem(item.getId());
                    break;
                default:
                    break;
            }
        }

    }
}