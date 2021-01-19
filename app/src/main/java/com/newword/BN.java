package com.newword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BN extends AppCompatActivity {
    private BottomNavigationView bn;
    private FragmentManager fa;
    private FragmentTransaction ft;
    private SearchFragment s;
    private BoardFragment b;
    private SettingFragment q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_n);
        bn=findViewById(R.id.bottomNavi);
        bn.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.b_search:
                        setFrag(0);
                        break;
                    case R.id.b_board:
                        setFrag(1);
                        break;
                    case R.id.b_quiz:
                        setFrag(2);
                        break;
                }
                return true;
            }
        });
        s=new SearchFragment();
        b=new BoardFragment();
        q=new SettingFragment();
        setFrag(0);
    }
    private void setFrag(int n){
        fa=getSupportFragmentManager();
        ft=fa.beginTransaction();
        switch (n){
            case 0:
                ft.replace(R.id.mainFrame,s);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.mainFrame,b);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.mainFrame,q);
                ft.commit();
                break;
        }
    }
}