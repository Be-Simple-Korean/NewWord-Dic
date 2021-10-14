package notyet.newword.ui.adapter;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import notyet.newword.R;
import notyet.newword.model.BoardData;
import notyet.newword.viewmodel.MainViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.CustomViewHolder> {
    ArrayList<BoardData> bar;
    private View v;
    TextView word,mean,writer,like,recom,delete;
    private String selectKey="";
    private ArrayList<String> kar;

    public BoardAdapter(ArrayList<BoardData> bar) {
        this.bar = bar;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_item,parent,false);
        CustomViewHolder holder=new CustomViewHolder(v);
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        MainViewModel viewModel= ViewModelProviders.of((FragmentActivity) holder.itemView.getContext()).get(MainViewModel.class);
        String uid=viewModel.getUid();
        holder.word.setText(bar.get(position).getWord());
        holder.writer.setText(bar.get(position).getWirter());
        String result=getResult(bar.get(position).getTime());
        holder.time.setText(result);
        holder.n_like.setText(bar.get(position).getN_like()+"");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //User userInfo=new User();
                kar = new ArrayList<>();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("board");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot d:snapshot.getChildren()){
                            if(d.getValue(BoardData.class).getWord().equals(bar.get(position).getWord())||d.getValue(BoardData.class).getTime().equals(bar.get(position).getTime())){
                                selectKey=d.getRef().getKey();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                DatabaseReference lef = database.getReference("like");
                //해당 단어 클릭시 단어를 좋아한 사람 목록을 가져오기
                lef.child(selectKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        kar.clear();
                        for(DataSnapshot d:snapshot.getChildren()){
                            if(d.getValue().getClass().getName().equals("java.util.ArrayList")){
                                kar= (ArrayList<String>) d.getValue();
                            }else if(d.getValue().getClass().getName().equals("java.lang.String")){
                                kar.add(d.getValue(String.class));
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Dialog dialog=new Dialog(v.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.board_word_dlg);
                
                delete=dialog.findViewById(R.id.tv_delete);
                word=dialog.findViewById(R.id.b_w_title);
                mean=dialog.findViewById(R.id.b_w_meaning);
                writer=dialog.findViewById(R.id.b_w_write);
                like=dialog.findViewById(R.id.b_w_like);
                recom=dialog.findViewById(R.id.b_w_recom);
                
                BoardData boardData = bar.get(position);
                if(uid.equals(bar.get(position).getUid())){
                    recom.setVisibility(View.INVISIBLE);
                    delete.setVisibility(View.VISIBLE);
                } //본인 추천 비허용 & 삭제 텍스트뷰 보이기
                
                word.setText(boardData.getWord());
                mean.setText(boardData.getMean());
                like.setText(boardData.getN_like()+"");
                writer.setText(boardData.getWirter());

                recom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       if(uid.equals("")){
                           Toast.makeText(view.getContext(),   "로그인 후에 이용해주세요.", Toast.LENGTH_SHORT).show();
                           return;
                       }else{
                           if (kar.size() > 0) {
                               for (int i = 0; i < kar.size(); i++) {
                                   if (kar.get(i).equals(uid)) {
                                       int value = bar.get(position).getN_like();
                                       value -= 1;
                                       bar.get(position).setN_like(value);
                                       like.setText(bar.get(position).getN_like() + "");
                                       myRef.child(selectKey).setValue(new BoardData(bar.get(position).getWord(), bar.get(position).getMean(), bar.get(position).getWirter(), bar.get(position).getTime(),
                                               value, bar.get(position).getUid()));
                                       kar.remove(uid);
                                       lef.child(selectKey).setValue(kar);
                                       return;
                                   }else{
                                       if(i+1==kar.size()){
                                           int value = bar.get(position).getN_like();
                                           value += 1;
                                           bar.get(position).setN_like(value);
                                           like.setText(bar.get(position).getN_like() + "");
                                           myRef.child(selectKey).setValue(new BoardData(bar.get(position).getWord(), bar.get(position).getMean(), bar.get(position).getWirter(), bar.get(position).getTime(),
                                                   value,bar.get(position).getUid()));
                                           kar.add(uid);
                                           lef.child(selectKey).setValue(kar);
                                           return;
                                       }
                                   }
                               }
                           }else{
                               int value = bar.get(position).getN_like();
                               value += 1;
                               bar.get(position).setN_like(value);
                               like.setText(bar.get(position).getN_like() + "");
                               myRef.child(selectKey).setValue(new BoardData(bar.get(position).getWord(), bar.get(position).getMean(), bar.get(position).getWirter(), bar.get(position).getTime(),
                                       value, bar.get(position).getUid()));
                               kar.add(uid);
                               lef.child(selectKey).setValue(kar);
                           }
                       }
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bar.remove(position);
                        dialog.dismiss();
                        Toast.makeText(view.getContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        //myRef.child(selectKey).setValue(null);
                        myRef.child(selectKey).removeValue();
                        kar.clear();
                        lef.child(selectKey).removeValue();
                    }
                });
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
            }
        });
    }

    private String getResult(String time) {
        String rTime="";
        Calendar cal=Calendar.getInstance();
        String cur=new SimpleDateFormat("yyyy.MM.dd").format(cal.getTime());
        String wt=time;
        String w_ymd=wt.substring(0,10);
        if(cur.equals(w_ymd)){
            String c_h=new SimpleDateFormat("HH").format(cal.getTime());
            String w_h=wt.substring(11,13);
            if(c_h.equals(w_h)){
                String c_m=new SimpleDateFormat("mm").format(cal.getTime());
                String w_m=wt.substring(14,16);
                if(c_m.equals(w_m)){
                    rTime="방금";
                }else{
                    int result=Integer.parseInt(c_m)-Integer.parseInt(w_m);
                    rTime=result+"분 전";
                }
            }else{
                int result=Integer.parseInt(c_h)-Integer.parseInt(w_h);
                rTime=result+"시간 전";
            }
        }else{
            rTime=w_ymd;
        }
        return rTime;
    } //시간처리

    @Override
    public int getItemCount() {
        return (null!=bar)?bar.size():0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView word;
        protected TextView writer;
        protected TextView time;
        protected TextView n_like;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            word=itemView.findViewById(R.id.r_tv_word);
            writer=itemView.findViewById(R.id.r_tv_writer);
            time=itemView.findViewById(R.id.r_tv_time);
            n_like=itemView.findViewById(R.id.r_tv_like);
        }
    }
}
