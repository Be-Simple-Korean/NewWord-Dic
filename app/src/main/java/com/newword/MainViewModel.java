package com.newword;

import android.app.Application;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.room.Room;

import com.firebase.ui.auth.data.model.Resource;

import java.util.ArrayList;

public class MainViewModel extends AndroidViewModel {
    AppDataBase db;
    Application application;
    private static String nickname="";
    private static String uid="";
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public static String getUid() {
        return uid;
    }

    public static void setUid(String uid) {
        MainViewModel.uid = uid;
    }

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
        db= Room.databaseBuilder(application,
                AppDataBase.class, "word")
                .build();

    }
    public void insert(){
        new InsertAsyncTask(db.wordDao(),application).execute();
    }
    private static class InsertAsyncTask extends AsyncTask<Void,Void,Void> {
        private WordDao wordDao;
        private Application application;
        public InsertAsyncTask(WordDao wordDao,Application application) {
            this.wordDao = wordDao;
            this.application=application;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            Resources r=application.getResources();
            String[] word=r.getStringArray(R.array.word);
            String[] meaning=r.getStringArray(R.array.meaning);
            for(int i=0;i<word.length;i++){
                wordDao.insert(new Word(word[i],meaning[i]));
            }
            return null;
        }
    }
}
