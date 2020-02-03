package com.jofiagtech.babyneeds;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jofiagtech.babyneeds.data.DataBaseHandler;
import com.jofiagtech.babyneeds.model.Item;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Button saveButton;
    private EditText babyItem;
    private EditText itemQuantity;
    private EditText itemColor;
    private EditText itemSize;
    private DataBaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHandler = new DataBaseHandler(this);

        //databaseHandler.deleteAllItem();

        //check if item was saved
        List<Item> items = databaseHandler.getAllItems();
        for (Item item : items) {
            Log.d("Main", "onCreate: " + item.getItemName());
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createPopupDialog();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
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
        databaseHandler.addItem(item);

        Snackbar.make(view, "Item Saved",Snackbar.LENGTH_SHORT)
                .show();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this, ItemListActivity.class));
            }
        }, 1200);//1 seconde
    }

    private void createPopupDialog() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);

        babyItem = view.findViewById(R.id.article_name);
        itemQuantity = view.findViewById(R.id.article_quantity);
        itemColor = view.findViewById(R.id.item_color);
        itemSize = view.findViewById(R.id.item_size);
        saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!babyItem.getText().toString().isEmpty()
                        && !itemColor.getText().toString().isEmpty()
                        && !itemQuantity.getText().toString().isEmpty()
                        && !itemSize.getText().toString().isEmpty()) {
                    saveItem(v);
                }else {
                    Snackbar.make(v, "Empty Fields not Allowed", Snackbar.LENGTH_SHORT)
                            .show();
                }

            }
        });

        builder.setView(view);
        dialog = builder.create();// creating our dialog object
        dialog.show();// important step!
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


/*public class MainActivity extends AppCompatActivity
{
    private AlertDialog.Builder mBuilder;
    private AlertDialog mDialog;
    private EditText articleName;
    private EditText articleQuantity;
    private EditText articleColor;
    private EditText articleSize;
    private Button saveButton;
    private DataBaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DataBaseHandler(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();
                showPopup();
            }
        });
    }

    private void showPopup(){
        mBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);

        articleName = view.findViewById(R.id.article_name);
        articleColor = view.findViewById(R.id.item_color);
        articleQuantity = view.findViewById(R.id.article_quantity);
        articleSize = view.findViewById(R.id.item_size);
        saveButton = view.findViewById(R.id.save_button);

        mBuilder.setView(view);
        mDialog = mBuilder.create();

        mDialog.show();

        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                saveItemInDataBase();
            }
        });
    }

    private void saveItemInDataBase(){
        Item item = new Item();

        String name = articleName.getText().toString().trim();
        int quantity = Integer.parseInt(articleQuantity.getText().toString());
        String color = articleColor.getText().toString();
        int size = Integer.parseInt(articleSize.getText().toString());

        item.setItemName(name);
        item.setItemQuantity(quantity);
        item.setItemColor(color);
        item.setItemSize(size);

        db.addItem(item);
        Log.d("DB", "onClick: " + db.getItem(item.getId()).getItemName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}*/
