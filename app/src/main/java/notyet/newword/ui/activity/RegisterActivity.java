package notyet.newword.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import notyet.newword.R;

public class RegisterActivity extends AppCompatActivity {
    EditText et_email,et_pwd,et_nick;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ArrayList<String> tar=new ArrayList<>();
        et_email=findViewById(R.id.reg_email);
        et_pwd=findViewById(R.id.reg_pwd);
        et_nick=findViewById(R.id.reg_nick);
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("user");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tar.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String d = dataSnapshot.getValue().toString();
                    tar.add(d);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        findViewById(R.id.reg_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nick = et_nick.getText().toString();
                String email = et_email.getText().toString();
                if(email.equals("")){
                    Toast.makeText(RegisterActivity.this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!email.contains("@")){
                    Toast.makeText(RegisterActivity.this, "이메일을 정확히 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                String pwd = et_pwd.getText().toString();
                if(pwd.equals("")){
                    Toast.makeText(RegisterActivity.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pwd.length()<6){
                    Toast.makeText(RegisterActivity.this, "비밀번호는 6자리 이상으로 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(nick.equals("")){
                    Toast.makeText(RegisterActivity.this, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(tar.size()>0){
                    for(int i =0;i<tar.size();i++){
                        if(tar.get(i).equals(nick)){
                            Toast.makeText(RegisterActivity.this, "중복된 닉네임입니다. 수정해주세요", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(i+1==tar.size()){
                            conFireBase(email,pwd,nick);
                        }
                    }
                }else{
                    conFireBase(email,pwd,nick);
                }
            }
        });
    }
    private void conFireBase(String email,String pwd,String nick){
        mAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Register", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            myRef.child(user.getUid()).setValue(nick);
                            Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Register", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "회원가입 실패",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}