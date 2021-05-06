package v2.view

import android.graphics.Color
import android.graphics.drawable.VectorDrawable
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.mfc.autofin.framework.R
import kotlinx.android.synthetic.main.soft_offer_action_bar.*
import kotlinx.android.synthetic.main.v2_soft_offer_loading_fragment.*
import v2.view.base.BaseFragment
import kotlin.concurrent.fixedRateTimer


class SoftOfferFragment : BaseFragment() {

    lateinit var tvLoanAmountVal: TextView
    lateinit var tvLoanTenureVal: TextView
    lateinit var skLoanAmount: SeekBar
    lateinit var skTenure: SeekBar
    lateinit var llBankOfferParent: LinearLayout
    lateinit var linearLayoutCalculation: LinearLayout
    lateinit var llSoftOfferDialog: LinearLayout
    lateinit var ivBackToRedDetails: ImageView
    lateinit var buttonLoanDetailsSubmit: Button
    var loanAmountMaximum: Int = 1000000
    var loanAmountMinimum: Int = 50000

    var loanTenureMaximum: Int = 15
    var loanTenureMinimum: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.v2_soft_offer_loading_fragment, container, false)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            initViews(view)
        }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initViews(view: View?) {
        if (view != null) {
            ivBackToRedDetails = view.findViewById(R.id.ivBackToRedDetails)

            tvLoanAmountVal = view.findViewById(R.id.tvLoanAmountValV2)
            tvLoanTenureVal = view.findViewById(R.id.tvLoanTenureValV2)
            skLoanAmount = view.findViewById(R.id.skLoanAmount)
            skTenure = view.findViewById(R.id.skTenure)

            llBankOfferParent = view.findViewById(R.id.llBankOfferParent)
            llSoftOfferDialog = view.findViewById(R.id.llSoftOfferDialog)
            linearLayoutCalculation = view.findViewById(R.id.linearLayoutCalculation)

            buttonLoanDetailsSubmit=view.findViewById(R.id.buttonLoanDetailsSubmit)

            skLoanAmount.min = loanAmountMinimum
            skLoanAmount.max = loanAmountMaximum


            skLoanAmount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                    val loanAmountVal = formatAmount(progress.toString())
                    tvLoanAmountVal.text = resources.getString(R.string.rupees_symbol) + loanAmountVal

                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })
        }

        skTenure.min = loanTenureMinimum
        skTenure.max = loanTenureMaximum

        ivBackToRedDetails.setOnClickListener(View.OnClickListener { activity?.onBackPressed() })

        skTenure.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                val loanTenureVal = progress.toString() + " Years"
                tvLoanTenureVal.text = loanTenureVal

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        buttonLoanDetailsSubmit.setOnClickListener(View.OnClickListener {
            showToast("Development in progress")
            activity?.onBackPressed()
        })
        enableSoftOfferDialog()

    }

    private fun enableSoftOfferDialog() {
        llSoftOfferDialog.visibility = View.VISIBLE
        val fixedRateTimer = fixedRateTimer(name = "soft-offer",
                initialDelay = 100, period = 100) {
            println("loading!")
        }
        try {
            Thread.sleep(3000)
        } finally {
            fixedRateTimer.cancel();
            enableCalculatorLayout()
        }

    }

    private fun enableCalculatorLayout() {
        llBankOfferParent.setBackgroundResource(R.color.vtwo_pale_grey)
        llSoftOfferDialog.visibility = View.VISIBLE
        linearLayoutCalculation.visibility = View.GONE

    }
}