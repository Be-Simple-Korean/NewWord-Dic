package notyet.newword.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import notyet.newword.BuildConfig;
import notyet.newword.R;
import notyet.newword.ui.activity.RegisterActivity;
import notyet.newword.ui.dialog.LoginDialog;
import notyet.newword.ui.dialog.QuestionDialog;
import notyet.newword.viewmodel.MainViewModel;

public class SettingFragment extends Fragment {
    private View view;
    TextView version, tv_ui;
    RelativeLayout question;
    Button btn_ui;
    private FirebaseAuth mAuth;

    private String getCurTime() {
        Calendar cal = Calendar.getInstance();
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int d = cal.get(Calendar.DATE);
        int h = cal.get(Calendar.HOUR_OF_DAY);
        int mm = cal.get(Calendar.MINUTE);
        int s = cal.get(Calendar.SECOND);
        cal.set(y, m, d, h, mm, s);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
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
                QuestionDialog questionDialog = new QuestionDialog(getContext(),
                        viewModel.getUid(),
                        viewModel.getNickname(),
                        getCurTime());
                questionDialog.show();
            }
        });
        btn_ui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginDialog dialog = new LoginDialog(getContext(),requireActivity());
                dialog.setOnClickListener(new LoginDialog.SetOnClickListener() {
                    @Override
                    public void connectLogin(String nick, String uid) {
                        tv_ui.setText(nick);
                        viewModel.setNickname(nick);
                        viewModel.setUid(uid);
                        btn_ui.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void connectRegister() {
                        Intent i = new Intent(requireActivity(), RegisterActivity.class);
                        startActivity(i);
                    }

                });
                dialog.show();
            }
        });
        return view;
    }
}
