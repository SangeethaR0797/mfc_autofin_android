package com.mfc.autofin.framework.Activity.review_activites;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mfc.autofin.framework.R;

import java.util.Arrays;
import java.util.List;

public class BSDListItemFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private Activity activity;
    TextView tvSelectionTitle;
    ImageView dialog_cancel_btn;
    ListView lvGender;
    Button btnSelectGender;
    TextView textView;
    private List<String> itemList;
    private String strGender="",title ;
    public BSDListItemFragment(Activity activity, TextView textView, List<String> itemList,String title) {
        this.activity = activity;
        this.textView=textView;
        this.itemList=itemList;
        this.title=title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gender, container, false);
        initView(view);
        return view;
    }

    private void initView(View view)
    {
        tvSelectionTitle=view.findViewById(R.id.tvSelectionTitle);
        dialog_cancel_btn=view.findViewById(R.id.dialog_cancel_btn);
        lvGender=view.findViewById(R.id.lvGender);
        btnSelectGender=view.findViewById(R.id.btnSelectGender);
        tvSelectionTitle.setText(title);
        ArrayAdapter adapter = new ArrayAdapter<String>(activity, R.layout.layout_single_list_view, itemList);
        lvGender.setAdapter(adapter);
        lvGender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                strGender= ""+ lvGender.getItemAtPosition(position);
            }
        });

        dialog_cancel_btn.setOnClickListener(this);
        btnSelectGender.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.dialog_cancel_btn)
        {
            dismiss();
        }
        else if(v.getId()==R.id.btnSelectGender)
        {
            textView.setText(strGender);
            dismiss();
        }
    }

}
