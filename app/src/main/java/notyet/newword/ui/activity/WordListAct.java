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
import notyet.newword.viewmodel.MainViewModel;
import notyet.newword.database.WordDao;

import java.util.ArrayList;
import java.util.List;

public class WordListAct extends AppCompatActivity {
    ArrayAdapter adapter;
    private ArrayList<String> war;
    private TextView tv_word;
    private TextView tv_mean;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);
        MainViewModel viewModel= ViewModelProviders.of(this).get(MainViewModel.class);
        new WordListAsyncTask(viewModel.db.wordDao()).execute();
        list = findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Dialog dialog=new Dialog(WordListAct.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.word_dlg);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                tv_word = dialog.findViewById(R.id.word_title);
                tv_mean = dialog.findViewById(R.id.word_meaning);
                String word=war.get(i);
                new MeanAsyncTask(viewModel.db.wordDao()).execute(word);
                dialog.show();
            }
        });
    }
    private class MeanAsyncTask extends AsyncTask<String,String,Void>{
        private WordDao wordDao;

        public MeanAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(String... s) {
            String mean=wordDao.getmeaning(s[0]);
            publishProgress(s[0],mean);
            return null;
        }

        @Override
        protected void onProgressUpdate(String... newWord) {
            super.onProgressUpdate(newWord);
            tv_word.setText(newWord[0]);
            tv_mean.setText(newWord[1]);
        }
    }
    private class WordListAsyncTask extends AsyncTask<Void,ArrayList<String>, ArrayList<String>>{
        private WordDao wordDao;

        public WordListAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            List<String> lar=wordDao.getWord();
            publishProgress((ArrayList<String>) lar);
            return (ArrayList<String>) lar;
        }

        @Override
        protected void onProgressUpdate(ArrayList<String>... values) {
            super.onProgressUpdate(values);
            war=values[0];
            adapter=new ArrayAdapter(WordListAct.this, android.R.layout.simple_list_item_1,values[0]);
            list.setAdapter(adapter);
        }
    }
}