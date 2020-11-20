package controller;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mfc.autofin.framework.R;

import java.util.List;

import kyc.DocumentUploadActivity;
import model.document.DOCObject;
import model.document.DocumentFile;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentTypeAdapter extends RecyclerView.Adapter<DocumentTypeAdapter.ViewHolder> implements Callback<Object> {


    private String TAG = SelectBankAdapter.class.getSimpleName();
    private Activity activity;
    private List<DOCObject> docObjectList;
    private List<DocumentFile> listOfFiles;

    public DocumentTypeAdapter(Activity activity, List<DOCObject> docObjectList) {
        this.activity = activity;
        this.docObjectList = docObjectList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.layout_doc_type_block, parent, false);
        Log.i(TAG, "onCreateViewHolder: ");
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(docObjectList.get(position).getKey()!=null)
        {
            holder.tvDocKeyTypeTitle.setText(docObjectList.get(position).getKey());
        }
        else if(docObjectList.get(position).getFiles()!=null)
        {
            listOfFiles=docObjectList.get(position).getFiles();
            DocumentFileAdapter selectBankAdapter = new DocumentFileAdapter(activity, listOfFiles);
            LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
            holder.rvDoc.setLayoutManager(layoutManager);
            holder.rvDoc.setAdapter(selectBankAdapter);
        }

    }


    @Override
    public int getItemCount() {
        return docObjectList.size();
    }


    @Override
    public void onResponse(retrofit2.Call<Object> call, Response<Object> response) {

    }

    @Override
    public void onFailure(retrofit2.Call<Object> call, Throwable t) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDocKeyTypeTitle, tvDocTypeSkip, tvSelectDocLbl;
        RecyclerView rvDoc;
        public ViewHolder(View itemView) {
            super(itemView);
            tvDocKeyTypeTitle=itemView.findViewById(R.id.tvDocKeyTypeTitle);
            tvDocTypeSkip=itemView.findViewById(R.id.tvDocTypeSkip);
            tvSelectDocLbl=itemView.findViewById(R.id.tvSelectDocLbl);
            rvDoc=itemView.findViewById(R.id.rvDoc);
        }

    }
}
