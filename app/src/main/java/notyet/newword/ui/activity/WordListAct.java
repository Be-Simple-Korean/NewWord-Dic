package notyet.newword.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import notyet.newword.R;
import notyet.newword.ui.dialog.ShowWordDialog;
import notyet.newword.viewmodel.MainViewModel;
import notyet.newword.database.WordDao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class WordListAct extends AppCompatActivity {
    ArrayAdapter adapter;
    private ArrayList<String> war;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        new WordListAsyncTask(viewModel.db.wordDao()).execute();
        list = findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String word = war.get(i);
                String mean = "";
                try {
                    mean = new MeanAsyncTask(viewModel.db.wordDao()).execute(word).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ShowWordDialog wordDialog = new ShowWordDialog(WordListAct.this, war.get(i), mean);
                wordDialog.show();
            }
        });
    }

    private class MeanAsyncTask extends AsyncTask<String, String, String> {
        private WordDao wordDao;

        public MeanAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected String doInBackground(String... s) {
            String mean = wordDao.getmeaning(s[0]);
            publishProgress(s[0], mean);
            return mean;
        }
    }

    private class WordListAsyncTask extends AsyncTask<Void, ArrayList<String>, ArrayList<String>> {
        private WordDao wordDao;

        public WordListAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            List<String> lar = wordDao.getWord();
            publishProgress((ArrayList<String>) lar);
            return (ArrayList<String>) lar;
        }

        @Override
        protected void onProgressUpdate(ArrayList<String>... values) {
            super.onProgressUpdate(values);
            war = values[0];
            adapter = new ArrayAdapter(WordListAct.this, android.R.layout.simple_list_item_1, values[0]);
            list.setAdapter(adapter);
        }
    }
}