package controller;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mfc.autofin.framework.R;

import java.util.List;

import model.custom_model.ReviewData;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private static final String TAG = ReviewAdapter.class.getSimpleName();
    private Activity activity;
    private List<ReviewData> dataList;
    private String strKODetails="";

    public ReviewAdapter(Activity activity, List<ReviewData> dataList,String strKODetails)
    {
        this.activity=activity;
        this.dataList=dataList;
        this.strKODetails=strKODetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.review_ladapter_row_item, parent, false);
        Log.i(TAG, "onCreateViewHolder: ");
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position)
    {
        ReviewData reviewData=dataList.get(position);
        if(!reviewData.getStrTitleLbl().equals("") && reviewData.getStrTitleLbl()!=null)
        {
            holder.tvReviewTitleLbl.setText(reviewData.getStrTitleLbl());
        }
        /*else if(!reviewData.getStrFieldValue().equals("") && reviewData.getStrFieldValue()!=null)
        {
           */ holder.tvReviewValue.setText(reviewData.getStrFieldValue());
            Log.i(TAG, "onBindViewHolder: "+reviewData.getStrFieldValue());
        //}
    }



    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvReviewTitleLbl,tvReviewValue,tvReviewGivenValEdit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReviewTitleLbl=itemView.findViewById(R.id.tvReviewTitleLbl);
            tvReviewValue=itemView.findViewById(R.id.tvReviewValue);
            tvReviewGivenValEdit=itemView.findViewById(R.id.tvReviewGivenValEdit);
        }
    }
}
