package controller;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mfc.autofin.framework.Activity.VehicleDetailsActivities.VehRegistrationYear;
import com.mfc.autofin.framework.Activity.VehicleDetailsActivities.VehicleMakeActivity;
import com.mfc.autofin.framework.R;

import java.util.List;

import utility.CommonMethods;

public class VehicleDetailsAdapter extends ArrayAdapter<String> {

    private int resourceLayout;
    private Activity activity;
    TextView textView;
    ListView lvVehListView;

    public VehicleDetailsAdapter(Activity activity, int resource, List<String> items, TextView textview, ListView lvVehListView)
    {
        super(activity, resource, items);
        this.resourceLayout = resource;
        this.activity = activity;
        this.textView=textview;
        this.lvVehListView=lvVehListView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(activity);
            v = vi.inflate(resourceLayout, null);
        }

        String p = getItem(position);
        TextView tvCustomLV=v.findViewById(R.id.tvCustomLV);
        if (p != null) {
            tvCustomLV.setText(p); }

        tvCustomLV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvCustomLV.setBackground(activity.getResources().getDrawable(R.drawable.navy_blue_outline));
                tvCustomLV.setTextColor(activity.getResources().getColor(R.color.navy_blue));
                /*textView.setText(tvCustomLV.getText().toString());
                lvVehListView.setVisibility(View.GONE);*/
            }
        });

        return v;
    }
}
