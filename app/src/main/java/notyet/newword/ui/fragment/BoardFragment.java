package notyet.newword.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import notyet.newword.R;
import notyet.newword.ui.adapter.BoardAdapter;
import notyet.newword.model.BoardData;
import notyet.newword.viewmodel.MainViewModel;
import notyet.newword.util.NetworkStatus;
import notyet.newword.ui.activity.WriteWord;

import java.util.ArrayList;

public class BoardFragment extends Fragment {
    private View view;
    RecyclerView recyclerView;
    ArrayList<BoardData> bdar;
    LinearLayoutManager layoutManager;
    BoardAdapter boardAdapter;
    TextView tv_date,tv_recom;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.frag_board,container,false);
        int status= NetworkStatus.getConnectivityStatus(view.getContext());
        if(status==NetworkStatus.TYPE_NOT_CONNECTED){
            Toast.makeText(view.getContext(), "네트워크를 연결해주세요.", Toast.LENGTH_SHORT).show();
        }
        MainViewModel viewModel= ViewModelProviders.of((FragmentActivity) view.getContext()).get(MainViewModel.class);
        bdar=new ArrayList<>();
        recyclerView=view.findViewById(R.id.rView);
        layoutManager=new LinearLayoutManager(view.getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        boardAdapter=new BoardAdapter(bdar);
        recyclerView.setAdapter(boardAdapter);
        recyclerView.setLayoutManager(layoutManager);
        view.findViewById(R.id.iv_write).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid=viewModel.getUid();
                if(uid.equals("")){
                    Toast.makeText(view.getContext(), "로그인후에 이용해주세요", Toast.LENGTH_SHORT).show();
                }else{
                    Intent i =new Intent(view.getContext(), WriteWord.class);
                    startActivity(i);
                }
            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("board");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bdar.clear();
                for(DataSnapshot d:snapshot.getChildren()){
                    bdar.add(d.getValue(BoardData.class));
                }
                boardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        tv_date=view.findViewById(R.id.tv_date);
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_recom.setTextColor(Color.GRAY);
                tv_date.setTextColor(ContextCompat.getColor(view.getContext(),R.color.blue));
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        bdar.clear();
                        for(DataSnapshot d:snapshot.getChildren()){
                            bdar.add(d.getValue(BoardData.class));
                        }
                        boardAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        tv_recom=view.findViewById(R.id.tv_recom);
        tv_recom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_date.setTextColor(Color.GRAY);
                tv_recom.setTextColor(ContextCompat.getColor(view.getContext(),R.color.blue));
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        bdar.clear();
                        for(DataSnapshot d:snapshot.getChildren()){
                            bdar.add(d.getValue(BoardData.class));
                        }
                        //버블 정렬
                        for(int i=bdar.size()-1; i>0; i--){
                            for(int j=0; j<i; j++){ //첫번쩨 j~(size-1) -> i가 거꾸로 시작하는 이유
                                if(bdar.get(j).getN_like()>bdar.get(j+1).getN_like()){
                                    BoardData temp = bdar.get(j);
                                    bdar.set(j,bdar.get(j+1));
                                    bdar.set(j+1,temp);
                                }
                            }
                        }
                        boardAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        return view;
    }
}
