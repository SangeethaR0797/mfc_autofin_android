package v2.view.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mfc.autofin.framework.R
import org.w3c.dom.Text
import v2.model.response.bank_offers.BankOffersData
import v2.view.callBackInterface.itemClickCallBack

public class SoftOfferAdapter(var context:Activity, var list: List<BankOffersData>, itemClick: itemClickCallBack?) : RecyclerView.Adapter<SoftOfferAdapter.BankViewHolder>() {

    private var itemCallBack: itemClickCallBack = itemClick!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.v2_bank_item_layout, parent, false)

        return BankViewHolder(view)
    }

    override fun onBindViewHolder(holder: BankViewHolder, position: Int)
    {
        val bankData=list[position]

        if(bankData.isRecommended==true)
        holder.tvBankStatusV2.text="Recommended"
        else
            holder.tvBankStatusV2.text=""

        holder.tvBankName.text=bankData.bankName
        holder.tvLoanAmountValV2.text=context.resources.getString(R.string.rupees_symbol)+" "+bankData.loanAmount
        holder.tvROIValV2.text=bankData.roi+" %"
        holder.tvTenureValV2.text=bankData.tenure+" MONTHS"
        holder.tvApplyNow.setOnClickListener(View.OnClickListener
        {
            itemCallBack.itemClick(bankData.bankId, position)

        })
        holder.tvEMIValV2.text=context.resources.getString(R.string.rupees_symbol)+" "+bankData.emi

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class BankViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivBankName:ImageView=itemView.findViewById(R.id.ivBankName)
        val tvBankName:TextView=itemView.findViewById(R.id.tvBankName)
        val tvBankStatusV2: TextView = itemView.findViewById(R.id.tvBankStatusV2)
        val tvLoanAmountValV2: TextView = itemView.findViewById(R.id.tvLoanAmountValV2)
        val tvROIValV2: TextView = itemView.findViewById(R.id.tvROIValV2)
        val tvTenureValV2: TextView = itemView.findViewById(R.id.tvTenureValV2)
        val tvApplyNow: TextView = itemView.findViewById(R.id.tvApplyNow)
        val tvEMIValV2:TextView=itemView.findViewById(R.id.tvEMIValV2)

    }

}