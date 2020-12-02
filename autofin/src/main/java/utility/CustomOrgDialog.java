package utility;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mfc.autofin.framework.R;

import java.util.List;

import static android.R.layout.simple_list_item_1;

public class CustomOrgDialog extends Dialog implements View.OnClickListener {

    private Context context;
    ArrayAdapter<String> adapter = null;
    List<String> listOfItems, temporaryList;
    TextView text1;
    ImageView dialog_cancel_btn, iv_custom_search_icon;
    private EditText etSearchAlertDialog,givenEditText;
    private ListView alertDialogListView;
    Button btnSelect;
    private String strSelectedValue = "";
    private String TAG=CustomOrgDialog.class.getSimpleName();

    public CustomOrgDialog(Context context, List<String> list, EditText editText,String searchString) {
        super(context);

        setContentView(R.layout.custom_org_name_dialog);
        this.context = context;

        dialog_cancel_btn = findViewById(R.id.dialog_cancel_btn);
        iv_custom_search_icon = findViewById(R.id.iv_custom_search_icon);
        etSearchAlertDialog = findViewById(R.id.etSearchAlertDialog);
        alertDialogListView = findViewById(R.id.alertDialogListView);
        btnSelect = findViewById(R.id.btnSelect);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = this.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        adapter = new ArrayAdapter<String>(context, simple_list_item_1, list);
        listOfItems = list;
        temporaryList = listOfItems;
        givenEditText = editText;
        etSearchAlertDialog.addTextChangedListener(filterTextWatcher);
        if(searchString.equals(""))
        {
            etSearchAlertDialog.setHint("Select Organization name");
        }
        else
        {
            etSearchAlertDialog.setText(searchString);
        }
        alertDialogListView.setAdapter(adapter);
        alertDialogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                strSelectedValue = "" + alertDialogListView.getItemAtPosition(position);
                etSearchAlertDialog.setText(strSelectedValue);
                Log.i(TAG, "onItemClick: "+strSelectedValue);
            }
        });
        dialog_cancel_btn.setOnClickListener(this);
        iv_custom_search_icon.setOnClickListener(this);
        btnSelect.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.dialog_cancel_btn) {
            dismiss();
        } else if (v.getId() == R.id.iv_custom_search_icon) {
            adapter.getFilter().filter(etSearchAlertDialog.getText());
            adapter.notifyDataSetChanged();
        } else if (v.getId() == R.id.btnSelect) {
            if (!strSelectedValue.equals("")) {
                givenEditText.setText(strSelectedValue);
                dismiss();
            } else {
                Toast.makeText(context, "Please select any one option", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            adapter.getFilter().filter(s);
            adapter.notifyDataSetChanged();
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            adapter.getFilter().filter(s);
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onStop() {
        etSearchAlertDialog.removeTextChangedListener(filterTextWatcher);
    }
}

