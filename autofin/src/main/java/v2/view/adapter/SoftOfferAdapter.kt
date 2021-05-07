package v2.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mfc.autofin.framework.R
import v2.model.response.bank_api_response.RecommendedBankData

public class SoftOfferAdapter(var context: Context, var list: List<RecommendedBankData>) : RecyclerView.Adapter<SoftOfferAdapter.BankViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.v2_bank_item_layout, parent, false)

        return BankViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: BankViewHolder, position: Int)
    {
        val bankData = list[position]

        holder.tvLoanAmountValV2.text=bankData.loanAmount
        holder.tvROIValV2.text=bankData.roi
        holder.tvTenureValV2.text=bankData.tenure
        holder.tvApplyNow.setOnClickListener(View.OnClickListener {  })
        holder.tvEMIValV2.text=bankData.emi

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class BankViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvBankStatusV2: TextView = itemView.findViewById(R.id.tvBankStatusV2)
        val tvLoanAmountValV2: TextView = itemView.findViewById(R.id.tvLoanAmountValV2)
        val tvROIValV2: TextView = itemView.findViewById(R.id.tvROIValV2)
        val tvTenureValV2: TextView = itemView.findViewById(R.id.tvTenureValV2)
        val tvApplyNow: TextView = itemView.findViewById(R.id.tvApplyNow)
        val tvEMIValV2:TextView=itemView.findViewById(R.id.tvEMIValV2)

    }

}