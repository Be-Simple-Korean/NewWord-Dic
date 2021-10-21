package notyet.newword.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

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

import notyet.newword.R;
import notyet.newword.ui.activity.RegisterActivity;


public class LoginDialog extends Dialog {
    public interface SetOnClickListener {
        void connectLogin(String nick, String uid);

        void connectRegister();
    }


    private FirebaseAuth mAuth;

    SetOnClickListener onClickListener;

    public void setOnClickListener(SetOnClickListener setOnClickListener) {
        this.onClickListener = setOnClickListener;
    }

    public LoginDialog(@NonNull Context context) {
        super(context, R.style.common_dialog_style);
    }

    Activity activity;

    public LoginDialog(@NonNull Context context, Activity activity) {
        super(context, R.style.common_dialog_style);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_dlg);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mAuth = FirebaseAuth.getInstance();
        TextView login = findViewById(R.id.login);
        TextView tv_email = findViewById(R.id.ed_id);
        TextView tv_pwd = findViewById(R.id.ed_pwd);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("sFile", getContext().MODE_PRIVATE);
                Toast.makeText(getContext(), "로그인을 시도중입니다...", Toast.LENGTH_SHORT).show();
                String email = tv_email.getText().toString();
                if (email.equals("") || email == null) {
                    Toast.makeText(view.getContext(), "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                String pwd = tv_pwd.getText().toString();
                if (pwd.equals("") || pwd == null) {
                    Toast.makeText(view.getContext(), "패스워드를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference("user").child(user.getUid());
                                    myRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String nick = dataSnapshot.getValue(String.class);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("uid", user.getUid());
                                            editor.putString("nick", nick); // key, value를 이용하여 저장하는 형태
                                            editor.commit();
                                            dismiss();
                                            onClickListener.connectLogin(nick, user.getUid());
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError error) {
                                            Log.w("Get Nick", "Failed to read value.", error.toException());
                                        }
                                    });
                                } else {
                                    if (task.getException().getMessage().equals("There is no user record corresponding to this identifier. The user may have been deleted.")) {
                                        Toast.makeText(getContext(), "이메일을 확인해주세요.", Toast.LENGTH_SHORT).show();
                                    } else if (task.getException().getMessage().equals("The password is invalid or the user does not have a password.")) {
                                        Toast.makeText(getContext(), "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "로그인 정보를 확인해주세요.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });
        TextView regi = findViewById(R.id.regi);
        regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.connectRegister();
            }
        });
    }
}
