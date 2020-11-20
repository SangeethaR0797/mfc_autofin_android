package controller;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mfc.autofin.framework.R;

import java.util.List;

import model.document.DocumentFile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentFileAdapter extends RecyclerView.Adapter<DocumentFileAdapter.ViewHolder> implements Callback<Object> {


    private String TAG = SelectBankAdapter.class.getSimpleName();
    private Activity activity;
    private List<DocumentFile> listOfFiles;

    public DocumentFileAdapter(Activity activity, List<DocumentFile> listOfFiles) {
        this.activity = activity;
        this.listOfFiles = listOfFiles;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.layout_document_row_item, parent, false);
        Log.i(TAG, "onCreateViewHolder: ");
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       if(listOfFiles.get(position).getDisplayLabel()!=null && listOfFiles.get(position).getValue()!=null)
       {
           holder.tvDocFileLbl.setText(listOfFiles.get(position).getDisplayLabel());
       }
        holder.tvDocFileLbl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }


    @Override
    public int getItemCount() {
        return listOfFiles.size();
    }


    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {

    }

    @Override
    public void onFailure(retrofit2.Call<Object> call, Throwable t) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llDocFile;
        TextView tvDocFileLbl,tvDocFileURL;
        public ViewHolder(View itemView) {
            super(itemView);
            llDocFile=itemView.findViewById(R.id.llDocFile);
            tvDocFileLbl=itemView.findViewById(R.id.tvDocFileLbl);
            tvDocFileURL=itemView.findViewById(R.id.tvDocFileURL);
        }

    }
}
