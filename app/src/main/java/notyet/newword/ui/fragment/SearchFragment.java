package notyet.newword.ui.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;

import notyet.newword.R;
import notyet.newword.database.AppDataBase;
import notyet.newword.database.WordDao;
import notyet.newword.ui.activity.WordListAct;
import notyet.newword.viewmodel.MainViewModel;

public class SearchFragment extends Fragment {
    private View view;
    EditText ed_word;
    ImageView iv_find;
    TextView tv_title, tv_mean;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        AppDataBase db = Room.databaseBuilder(view.getContext(),
                AppDataBase.class, "word")
                .build(); //데이터베이스 객체 생성
        SharedPreferences sf = this.getActivity().getSharedPreferences("sFile", MODE_PRIVATE);
        boolean isF = sf.getBoolean("isF", false);
        if (!isF) {
            viewModel.insert();
            isF = true;
            SharedPreferences.Editor editor = sf.edit();
            editor.putBoolean("isF", isF);
            editor.commit();
        }

        ed_word = view.findViewById(R.id.ed_word);
        iv_find = view.findViewById(R.id.iv_find);
        tv_title = view.findViewById(R.id.word_title);
        tv_mean = view.findViewById(R.id.word_meaning);

        ed_word.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String word = ed_word.getText().toString();
                    if (word == null || word.equals("")) {
                        Toast.makeText(view.getContext(), "단어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        new SelectAsyncTask(db.wordDao()).execute(word);
                    }
                }
                return false;
            }
        });
        iv_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String word = ed_word.getText().toString();
                if (word == null || word.equals("")) {
                    Toast.makeText(view.getContext(), "단어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    new SelectAsyncTask(db.wordDao()).execute(word);
                }
            }
        });
        view.findViewById(R.id.iv_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), WordListAct.class);
                startActivity(i);
            }
        });
        return view;
    }

    private class SelectAsyncTask extends AsyncTask<String, String, Void> {
        private WordDao wordDao;

        public SelectAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(String... s) {
            String mean = wordDao.getmeaning(s[0]);
            publishProgress(s[0], mean);
            return null;
        }

        @Override
        protected void onProgressUpdate(String... newWord) {
            String word = newWord[0];
            String mean = newWord[1];
            try {
                if (!mean.equals("")) {
                    tv_title.setVisibility(View.VISIBLE);
                    tv_mean.setVisibility(View.VISIBLE);
                    tv_title.setText(word);
                    tv_mean.setText(mean);
                }
            } catch (Exception e) {
                Toast.makeText(view.getContext(), "단어를 찾을수없습니다. 다시 검색해주세요", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
