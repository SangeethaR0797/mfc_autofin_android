package v2.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mfc.autofin.framework.R
import v2.model.response.CustomerDetailsResponse
import v2.view.base.BaseFragment


class SoftOfferFragment : BaseFragment() {
    lateinit var customerDetailsResponse: CustomerDetailsResponse
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.v2_soft_offer_loading_fragment, container, false)
        arguments?.let {
            val safeArgs = SoftOfferFragmentArgs.fromBundle(it)
            customerDetailsResponse = safeArgs.CustomerDetails
            customerId = safeArgs.customerId
        }
        initViews(view)
        return view
    }

    private fun initViews(view: View?) {

    }

}