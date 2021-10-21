package notyet.newword.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import notyet.newword.R;
import notyet.newword.model.Question;

/**
 * 문의사항 얼럿
 */
public class QuestionDialog extends Dialog {

    String uid, nick, time;

    public QuestionDialog(@NonNull Context context) {
        super(context, R.style.common_dialog_style);
    }

    public QuestionDialog(@NonNull Context context, String uid, String nick, String time) {
        super(context, R.style.common_dialog_style);
        this.uid = uid;
        this.nick = nick;
        this.time = time;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_dlg);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        String title = ((TextView) findViewById(R.id.ques_title)).getText().toString();
        String contents = ((TextView) findViewById(R.id.ques_contents)).getText().toString();
        Button apply = findViewById(R.id.ques_apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uid.equals("")) {
                    Toast.makeText(view.getContext(), "로그인 후에 이용해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("question");
                    myRef.push().setValue(new Question(nick, uid, title, contents, time));
                    myRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            dismiss();
                            Toast.makeText(getContext(), "문의사항이 등록되었습니다.", Toast.LENGTH_SHORT).show();
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
    }
}
