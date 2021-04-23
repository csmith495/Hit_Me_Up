package com.cas.hitmeup.dictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cas.hitmeup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.List;

public class DictionaryActivity extends AppCompatActivity implements CategoryListAdapter.HandleCategoryClick {

    private DictionaryActivityViewModel viewModel;
    private TextView noResultTextView;
    private RecyclerView recyclerView;
    private CategoryListAdapter categoryListAdapter;
    private Category categoryForEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.KOREAN)
                .build();

        final Translator translator = Translation.getClient(options);

        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();
        translator.downloadModelIfNeeded(conditions)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(DictionaryActivity.this,
                                "Model Downloaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        getSupportActionBar().setTitle("Dictionary");

        noResultTextView = findViewById(R.id.categoryNoResult);
        recyclerView = findViewById(R.id.dictRecyclerView);
        ImageView addNew = findViewById(R.id.addNewCategoryImageView);
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCategoryDialog(false);
            }
        });

        initViewModel();
        initRecyclerView();
        viewModel.getAllCategoryList();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryListAdapter = new CategoryListAdapter(this, this);
        recyclerView.setAdapter(categoryListAdapter);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(DictionaryActivityViewModel.class);
        viewModel.getCategoryListObserver().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                if(categories == null) {
                    noResultTextView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                else {
                    categoryListAdapter.setCategoryList(categories);
                    recyclerView.setVisibility(View.VISIBLE);
                    noResultTextView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void showAddCategoryDialog(boolean isForEdit) {
        AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        View dialogView = getLayoutInflater().inflate(R.layout.add_category_layout, null);
        EditText enterCategoryInput = dialogView.findViewById(R.id.enterCategoryInput);
        TextView createButton = dialogView.findViewById(R.id.createButton);
        TextView cancelButton = dialogView.findViewById(R.id.cancelCategoryButton);

        if(isForEdit) {
            createButton.setText("Update");
            enterCategoryInput.setText(categoryForEdit.categoryName);
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = enterCategoryInput.getText().toString();
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(DictionaryActivity.this, "Please enter a category name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(isForEdit) {
                    categoryForEdit.categoryName = name;
                    viewModel.updateCategory(categoryForEdit);
                }
                else {
                    viewModel.insertCategory(name);
                }

                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    @Override
    public void itemClick(Category category) {
        
    }

    @Override
    public void removeItem(Category category) {
        viewModel.deleteCategory(category);
    }

    @Override
    public void editItem(Category category) {
        this.categoryForEdit = category;
        showAddCategoryDialog(true);
    }
}