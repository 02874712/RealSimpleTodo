package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> items;

    Button btadd;
    EditText etitem;
    RecyclerView rvitems;
    ItemsAdpater itemsAdpater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btadd = findViewById(R.id.btadd);
        etitem = findViewById(R.id.etitem);
        rvitems = findViewById(R.id.rvitems);

        loadItems();

        ItemsAdpater.OnLongClickListener onLongClickListener = new ItemsAdpater.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                //Delete item from the model
                items.remove(position);
                //notify the adapter
                itemsAdpater.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(),"Item was removed",Toast.LENGTH_SHORT ).show();
                saveItems();

            }
        };
        itemsAdpater = new ItemsAdpater(items, onLongClickListener);
        rvitems.setAdapter(itemsAdpater);
        rvitems.setLayoutManager(new LinearLayoutManager(this));

        btadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String todoItem = etitem.getText().toString();
               // add item to model
                items.add(todoItem);
                //notify adapter thatan item is inserted
                itemsAdpater.notifyItemInserted(items.size() - 1);
                etitem.setText("");
                Toast.makeText(getApplicationContext(),"Item was added",Toast.LENGTH_SHORT ).show();
                saveItems();
            }
        });
     }

     private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
     }

     //this function will loaditems by readingevery line ofthe data file
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    // This funciton savesitems by writingthmem into the file
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }

    }
}