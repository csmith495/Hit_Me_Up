package com.cas.hitmeup.dictionary;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Items {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "itemName")
    public String itemName;

    @ColumnInfo(name = "categoryId")
    public int categoryId;

    @ColumnInfo(name = "mastered")
    public boolean mastered;
}
