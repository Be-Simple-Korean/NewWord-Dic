package com.newword;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
@Database(entities = {Word.class},version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract WordDao wordDao();
}
