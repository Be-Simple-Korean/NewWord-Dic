package notyet.newword.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import notyet.newword.R;
import notyet.newword.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sf = getSharedPreferences("sFile", MODE_PRIVATE);
        String nick = sf.getString("nick", "");
        String uid = sf.getString("uid", "");
        if (!nick.equals("") && !uid.equals("")) {
            MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
            viewModel.setNickname(nick);
            viewModel.setUid(uid);
        }

        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                Intent i = new Intent(MainActivity.this, BottomNavigationActivity.class);
                startActivity(i);
                finish();
            }
        }).start();
    }

}