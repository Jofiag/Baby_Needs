package com.jofiagtech.babyneeds;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jofiagtech.babyneeds.adapter.RecyclerViewAdapter;
import com.jofiagtech.babyneeds.data.DataBaseHandler;
import com.jofiagtech.babyneeds.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemListActivity extends AppCompatActivity
{
    private List<Item> mItemList;
    private DataBaseHandler mDataBaseHandler;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private FloatingActionButton mFab;
    private AlertDialog.Builder mBuilder;
    private AlertDialog mDialog;
    private Button saveButton;
    private EditText babyItem;
    private EditText itemQuantity;
    private EditText itemColor;
    private EditText itemSize;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        mItemList = new ArrayList<>();
        mDataBaseHandler = new DataBaseHandler(this);
        mItemList = mDataBaseHandler.getAllItems();

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerViewAdapter = new RecyclerViewAdapter(this, mItemList);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerViewAdapter.notifyDataSetChanged();

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                createPopupDialog();
            }
        });
    }

    private void createPopupDialog()
    {
        mBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);

        babyItem = view.findViewById(R.id.article_name);
        itemQuantity = view.findViewById(R.id.article_quantity);
        itemColor = view.findViewById(R.id.item_color);
        itemSize = view.findViewById(R.id.item_size);
        saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (!babyItem.getText().toString().isEmpty()
                        && !itemColor.getText().toString().isEmpty()
                        && !itemQuantity.getText().toString().isEmpty()
                        && !itemSize.getText().toString().isEmpty()){
                    saveItem(v);
                    mRecyclerViewAdapter.notifyDataSetChanged();
                }
                else {
                    Snackbar.make(v, "Empty Fields not Allowed", Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        });

        mBuilder.setView(view);
        mDialog = mBuilder.create();
        mDialog.show();
    }

    private void saveItem(View view) {
        Item item = new Item();

        String newItem = babyItem.getText().toString().trim();
        String newColor = itemColor.getText().toString().trim();
        int quantity = Integer.parseInt(itemQuantity.getText().toString().trim());
        int size = Integer.parseInt(itemSize.getText().toString().trim());

        item.setItemName(newItem);
        item.setItemColor(newColor);
        item.setItemQuantity(quantity);
        item.setItemSize(size);

        //databaseHandler.deleteAllItem();
        mDataBaseHandler.addItem(item);
        mItemList = mDataBaseHandler.getAllItems();

        Snackbar.make(view, "Item Saved",Snackbar.LENGTH_SHORT)
                .show();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run() {
                mDialog.dismiss();
                mRecyclerViewAdapter = null;
                mRecyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), mItemList);
                mRecyclerView.setAdapter(mRecyclerViewAdapter);
                //mRecyclerViewAdapter.notifyDataSetChanged();

                //finish();
            }
        }, 1200);//1 seconde
    }
}