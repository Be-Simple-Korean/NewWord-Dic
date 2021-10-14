package notyet.newword.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import notyet.newword.model.Word;

@Database(entities = {Word.class},version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract WordDao wordDao();
}
