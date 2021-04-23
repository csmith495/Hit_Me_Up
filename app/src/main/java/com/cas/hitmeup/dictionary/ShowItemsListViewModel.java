package com.cas.hitmeup.dictionary;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class ShowItemsListViewModel extends AndroidViewModel {

    private MutableLiveData<List<Items>> listOfItems;
    private AppDatabase appDatabase;

    public ShowItemsListViewModel(Application application) {
        super(application);
        listOfItems = new MutableLiveData<>();

        appDatabase = AppDatabase.getDBinstance(getApplication().getApplicationContext());
    }

    public MutableLiveData<List<Items>> getItemsListObserver() {
        return listOfItems;
    }

    public void getAllItemsList(int categoryID) {
        List<Items> itemsList = appDatabase.wordListDao().getAllItemsList(categoryID);
        if(itemsList.size() > 0) {
            listOfItems.postValue(itemsList);
        }
        else {
            listOfItems.postValue(null);
        }
    }

    public  void insertItems(Items item) {
        appDatabase.wordListDao().insertItems(item);
        getAllItemsList(item.categoryId);
    }

    public void updateItems(Items item) {
        appDatabase.wordListDao().updateItems(item);
        getAllItemsList(item.categoryId);
    }

    public void deleteItems(Items item) {
        appDatabase.wordListDao().deleteItems(item);
        getAllItemsList(item.categoryId);
    }
}
