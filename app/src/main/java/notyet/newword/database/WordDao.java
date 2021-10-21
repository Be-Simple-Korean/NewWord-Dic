package notyet.newword.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import notyet.newword.model.Word;

import java.util.List;

@Dao
public interface WordDao {
    @Query("SELECT * FROM WORD")
    List<Word> getAll();
    @Query("SELECT meaning FROM WORD WHERE word = :word")
    String getmeaning(String word);

    @Query("SELECT word FROM word")
    List<String> getWord();

    @Insert
    void insert(Word word);

    @Update
    void Update(Word word);

    @Delete
    void delete(Word word);

    @Query("DELETE FROM word")
    void clear();
}
