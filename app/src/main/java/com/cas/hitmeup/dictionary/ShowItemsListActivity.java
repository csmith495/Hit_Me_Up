package com.cas.hitmeup.dictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cas.hitmeup.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.List;

public class ShowItemsListActivity extends AppCompatActivity implements ItemsListAdapter.HandleItemsClick {

    private int category_id;
    private ItemsListAdapter itemsListAdapter;
    private RecyclerView recyclerView;
    private ShowItemsListViewModel viewModel;
    private Items itemToUpdate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_items_list);

        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.KOREAN)
                .build();

        final Translator translator = Translation.getClient(options);

        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();
        translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ShowItemsListActivity.this,
                                "Model Downloaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShowItemsListActivity.this,
                                "Download Failed", Toast.LENGTH_SHORT).show();
                    }
                });

        category_id = getIntent().getIntExtra("category_id", 0);
        String categoryName = getIntent().getStringExtra("category_name");

        getSupportActionBar().setTitle(categoryName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText addNewItemInput = findViewById(R.id.addNewItemInput);
        ImageView saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = addNewItemInput.getText().toString();
                translator.translate(itemName).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        if(TextUtils.isEmpty(s)) {
                            Toast.makeText(ShowItemsListActivity.this,
                                    "Enter Word or Phrase Here", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(itemToUpdate == null) {
                            saveNewItem(itemName + "\n" + s);
                        }
                        else {
                            updateNewItem(itemName + "\n" + s);
                        }
                    }
                });
            }
        });

        initRecyclerView();
        initViewModel();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(ShowItemsListViewModel.class);
        viewModel.getItemsListObserver().observe(this, new Observer<List<Items>>() {
            @Override
            public void onChanged(List<Items> items) {
                if(items == null) {
                    recyclerView.setVisibility(View.GONE);
                    findViewById(R.id.noItemResult).setVisibility(View.VISIBLE);
                }
                else {
                    itemsListAdapter.setCategoryList(items);
                    findViewById(R.id.noItemResult).setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.itemsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemsListAdapter = new ItemsListAdapter(this, this);
        recyclerView.setAdapter(itemsListAdapter);
    }

    private void saveNewItem(String itemName) {
        Items item = new Items();
        item.itemName = itemName;
        item.categoryId = category_id;
        viewModel.insertItems(item);
        ((EditText) findViewById(R.id.addNewItemInput)).setText("");
    }

    private void updateNewItem(String newName) {
        itemToUpdate.itemName = newName;
        viewModel.updateItems(itemToUpdate);
        ((EditText) findViewById(R.id.addNewItemInput)).setText("");
        itemToUpdate = null;
    }

    @Override
    public void itemClick(Items item) {
        if(item.mastered) {
            item.mastered = false;
        }
        else {
            item.mastered = true;
        }
        viewModel.updateItems(item);
    }

    @Override
    public void removeItem(Items item) {
        viewModel.deleteItems(item);
    }

    @Override
    public void editItem(Items item) {
        this.itemToUpdate = item;
        ((EditText) findViewById(R.id.addNewItemInput)).setText(item.itemName);
    }
}