package kyc;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mfc.autofin.framework.R;

public class DocumentUploadActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout llpancard;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.document_upload_activity);
        initViews();
    }

    private void initViews(){
        llpancard= findViewById(R.id.llpancard);
        llpancard.setOnClickListener(DocumentUploadActivity.this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.llpancard){

        }
    }
}
