package notyet.newword.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import notyet.newword.R;
import notyet.newword.ui.fragment.SearchFragment;
import notyet.newword.ui.fragment.SettingFragment;

public class BottomNavigationActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private SearchFragment searchFragment;
    //    private BoardFragment b;
    private SettingFragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_n);
        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.b_search:
                    setFrag(0);
                    break;
                default:
                    setFrag(2);
                    break;
            }
            return true;
        });
        bottomNavigationView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
        searchFragment = new SearchFragment();
//        b=new BoardFragment();
        settingFragment = new SettingFragment();
        setFrag(0);
    }

    /**
     * 프래그먼트 세팅
     * @param n
     */
    private void setFrag(int n) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if (n == 0) {
            fragmentTransaction.replace(R.id.mainFrame, searchFragment);
        } else {
            fragmentTransaction.replace(R.id.mainFrame, settingFragment);
        }
        fragmentTransaction.commit();
    }
}