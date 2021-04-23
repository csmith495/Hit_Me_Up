package com.cas.hitmeup.dictionary;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class DictionaryActivityViewModel extends AndroidViewModel {

    private MutableLiveData<List<Category>> listOfCategories;
    private AppDatabase appDatabase;

    public DictionaryActivityViewModel(Application application) {
        super(application);
        listOfCategories = new MutableLiveData<>();

        appDatabase = AppDatabase.getDBinstance(getApplication().getApplicationContext());
    }

    public MutableLiveData<List<Category>> getCategoryListObserver() {
        return listOfCategories;
    }

    public void getAllCategoryList() {
        List<Category> categoryList = appDatabase.wordListDao().getAllCategoriesList();
        if(categoryList.size() > 0) {
            listOfCategories.postValue(categoryList);
        }
        else {
            listOfCategories.postValue(null);
        }
    }

    public  void insertCategory(String catName) {
        Category category = new Category();
        category.categoryName = catName;
        appDatabase.wordListDao().insertCategories(category);
        getAllCategoryList();
    }

    public void updateCategory(Category category) {
        appDatabase.wordListDao().updateCategory(category);
        getAllCategoryList();
    }

    public void deleteCategory(Category category) {
        appDatabase.wordListDao().deleteCategory(category);
        getAllCategoryList();
    }
}
