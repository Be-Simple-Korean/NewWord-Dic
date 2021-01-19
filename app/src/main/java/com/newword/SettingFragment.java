package com.newword;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.Executor;

public class SettingFragment extends Fragment {
    private View view;
    TextView version, tv_ui;
    EditText title,contents;
    RelativeLayout question;
    Button btn_ui,apply;
    private FirebaseAuth mAuth;
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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_set, container, false);
        MainViewModel viewModel = ViewModelProviders.of((FragmentActivity) view.getContext()).get(MainViewModel.class);
        tv_ui = view.findViewById(R.id.tv_ui);
        btn_ui = view.findViewById(R.id.btn_ui);
        String nick = viewModel.getNickname();
        if (!nick.equals("")) {
            tv_ui.setText(nick);
            btn_ui.setVisibility(View.INVISIBLE);
        }
        mAuth = FirebaseAuth.getInstance();
        version = view.findViewById(R.id.s_v_v);
        version.setText(BuildConfig.VERSION_NAME);
        question = view.findViewById(R.id.s_q);
        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //문의
                Dialog dialog = new Dialog(view.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.question_dlg);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                title=dialog.findViewById(R.id.ques_title);
                contents=dialog.findViewById(R.id.ques_contents);
                apply=dialog.findViewById(R.id.ques_apply);
                apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String uid=viewModel.getUid();
                        if(uid.equals("")){
                            Toast.makeText(view.getContext(),   "로그인 후에 이용해주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            String nick=viewModel.getNickname();
                            String t=title.getText().toString();
                            String time=getCurTime();
                            String c=contents.getText().toString();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("question");
                            myRef.push().setValue(new Question(nick,uid,t,c,time));
                            myRef.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    dialog.dismiss();
                                    Toast.makeText(view.getContext(), "문의사항이 등록되었습니다.", Toast.LENGTH_SHORT).show();
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

                    }
                });
                dialog.show();
            }
        });
        btn_ui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(view.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.login_dlg);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                TextView login = dialog.findViewById(R.id.login);
                TextView tv_email = dialog.findViewById(R.id.ed_id);
                TextView tv_pwd = dialog.findViewById(R.id.ed_pwd);
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
                                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
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
                                                    String nick= dataSnapshot.getValue(String.class);
                                                    dialog.dismiss();
                                                    tv_ui.setText(nick);
                                                    viewModel.setNickname(nick);
                                                    viewModel.setUid(user.getUid());
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putString("uid", user.getUid());
                                                    editor.putString("nick", nick); // key, value를 이용하여 저장하는 형태
                                                    editor.commit();
                                                    btn_ui.setVisibility(View.INVISIBLE);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError error) {
                                                    Log.w("Get Nick", "Failed to read value.", error.toException());
                                                }
                                            });
                                        } else {
                                            if(task.getException().getMessage().equals("There is no user record corresponding to this identifier. The user may have been deleted.")){
                                                Toast.makeText(dialog.getContext(), "이메일을 확인해주세요.", Toast.LENGTH_SHORT).show();
                                            }else if(task.getException().getMessage().equals("The password is invalid or the user does not have a password.")){
                                                Toast.makeText(dialog.getContext(), "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(dialog.getContext(), "로그인 정보를 확인해주세요.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });
                    }
                });
                TextView regi = dialog.findViewById(R.id.regi);
                regi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(view.getContext(), Register.class);
                        startActivity(i);
                    }
                });
                dialog.show();
            }
        });
        return view;
    }
}
