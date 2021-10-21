package notyet.newword.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import notyet.newword.R;

public class ShowWordDialog extends Dialog {
    String word, mean;

    public ShowWordDialog(@NonNull Context context) {
        super(context, R.style.common_dialog_style);
    }

    public ShowWordDialog(@NonNull Context context, String word, String mean) {
        super(context, R.style.common_dialog_style);
        this.word = word;
        this.mean = mean;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_dlg);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ((TextView)findViewById(R.id.word_title)).setText(word);
        ((TextView)findViewById(R.id.word_meaning)).setText(mean);
    }
}
