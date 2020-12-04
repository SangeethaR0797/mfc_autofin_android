package controller;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mfc.autofin.framework.Activity.PersonalDetails.UserDOBActivity;
import com.mfc.autofin.framework.Activity.ResidentialActivity.ResidentialCity;
import com.mfc.autofin.framework.Activity.VehicleDetailsActivities.VehicleCategory;
import com.mfc.autofin.framework.Activity.review_activites.ReviewDetailsActivity;
import com.mfc.autofin.framework.R;

import java.util.List;

import model.basic_details.BasicDetails;
import model.custom_model.ReviewData;
import utility.CommonMethods;
import utility.CommonStrings;

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
        holder.tvReviewGivenValEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(strKODetails.equalsIgnoreCase(activity.getResources().getString(R.string.vehicle_details_title)))
                {
                    activity.startActivity(new Intent(activity, VehicleCategory.class));
                    CommonStrings.IS_OLD_LEAD=true;
                }
                else if(strKODetails.equalsIgnoreCase(activity.getResources().getString(R.string.title_basic_details)))
                {
                    CommonMethods.showToast(activity, "Sorry! you cannot edit Basic details");
                    CommonStrings.IS_OLD_LEAD=true;

                }
                else if(strKODetails.equalsIgnoreCase(activity.getResources().getString(R.string.title_residential_details)))
                {
                    activity.startActivity(new Intent(activity, ResidentialCity.class));
                    CommonStrings.IS_OLD_LEAD=true;

                }else if(strKODetails.equalsIgnoreCase(activity.getResources().getString(R.string.title_personal_details)))
                {
                    activity.startActivity(new Intent(activity, UserDOBActivity.class));
                    CommonStrings.IS_OLD_LEAD=true;

                }
            }
        });
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
