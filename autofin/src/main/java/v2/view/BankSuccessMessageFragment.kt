package v2.view

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.mfc.autofin.framework.R
import v2.view.base.BaseFragment

class BankSuccessMessageFragment : BaseFragment() {

    var custId=0
    var name=""
    var caseID=""
    val bankAppIdPrefix="Your bank application ID is "
    val caseIDPrefix="Case ID is "

    private lateinit var textViewCustomerName: TextView
    private lateinit var textViewCustCaseId:TextView
    private lateinit var buttonViewStatus:Button
    private lateinit var fragView:View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.v2_fragment_bank_success_message, container, false)
        fragView=view
        textViewCustomerName=view.findViewById(R.id.textViewCustomerName)
        textViewCustCaseId=view.findViewById(R.id.textViewCustCaseId)
        buttonViewStatus=view.findViewById(R.id.buttonViewStatus)
        textViewCustomerName.text=name
        textViewCustCaseId.text=caseIDPrefix+caseID
        buttonViewStatus.setOnClickListener(View.OnClickListener {
            activity?.finish()
            navigateToLeadDetailsFromFinalScreen(custId)
        })


        return view
    }



}