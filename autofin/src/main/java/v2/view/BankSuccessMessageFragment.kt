package v2.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.mfc.autofin.framework.R
import org.w3c.dom.Text
import v2.view.base.BaseFragment
import v2.view.utility_view.SelectedBankOfferStatusFragmentArgs

class BankSuccessMessageFragment : BaseFragment() {

    var name=""
    var bankApplicationID=""
    var caseID=""
    val bankAppIdPrefix="Your bank application ID is "
    val caseIDPrefix="Case ID is "

    private lateinit var textViewCustomerName: TextView
    private lateinit var textViewBankId:TextView
    private lateinit var textViewCustCaseId:TextView
    private lateinit var buttonViewStatus:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val safeArgs = BankSuccessMessageFragmentArgs.fromBundle(it)
            name=safeArgs.LoanProcessData.customerName
            bankApplicationID=safeArgs.LoanProcessData.bankApplicationID
            caseID=safeArgs.LoanProcessData.caseID

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.v2_fragment_bank_success_message, container, false)
        textViewCustomerName=view.findViewById(R.id.textViewCustomerName)
        textViewBankId=view.findViewById(R.id.textViewBankId)
        textViewCustCaseId=view.findViewById(R.id.textViewCustCaseId)
        buttonViewStatus=view.findViewById(R.id.buttonViewStatus)
        textViewCustomerName.text=name
        textViewBankId.text=bankAppIdPrefix+bankApplicationID
        textViewCustCaseId.text=caseIDPrefix+caseID
        buttonViewStatus.setOnClickListener(View.OnClickListener {
            showToast("Development Under Progress")
        })


        return view
    }


}