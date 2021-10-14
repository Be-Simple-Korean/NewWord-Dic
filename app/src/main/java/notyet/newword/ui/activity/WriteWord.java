package notyet.newword.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import notyet.newword.R;
import notyet.newword.model.BoardData;
import notyet.newword.viewmodel.MainViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WriteWord extends AppCompatActivity {
    EditText word,mean;
    String s_word="",s_mean="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_word);
        word=findViewById(R.id.write_word);
        mean=findViewById(R.id.write_mean);
        MainViewModel viewModel= ViewModelProviders.of(this).get(MainViewModel.class);
        findViewById(R.id.btn_write).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s_word=word.getText().toString();
                if(s_word.equals("")||s_word==null){
                    Toast.makeText(WriteWord.this, "단어를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                s_mean=mean.getText().toString();
                if(s_mean.equals("")||s_mean==null){
                    Toast.makeText(WriteWord.this, "뜻을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                String time=getCurTime();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("board");
                myRef.push().setValue(new BoardData(s_word,s_mean,viewModel.getNickname(),time,0,viewModel.getUid()));
                myRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        finish();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private String getCurTime() {
        Calendar cal=Calendar.getInstance();
        int y=cal.get(Calendar.YEAR);
        int m =cal.get(Calendar.MONTH);
        int d = cal.get(Calendar.DATE);
        int h=cal.get(Calendar.HOUR_OF_DAY);
        int mm=cal.get(Calendar.MINUTE);
        int s=cal.get(Calendar.SECOND);
        cal.set(y,m,d,h,mm,s);
        SimpleDateFormat sd=new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        return sd.format(cal.getTime());
    }
}