package v2.view.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.marginRight
import androidx.recyclerview.widget.RecyclerView
import com.mfc.autofin.framework.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

import v2.model.response.RuleEngineBankData
import v2.view.callBackInterface.itemClickCallBack

class PartnerBankRecyclerViewAdapter(
    var context: Activity,
    var dataListValue: List<RuleEngineBankData>?,
    itemClick: itemClickCallBack?
) : RecyclerView.Adapter<PartnerBankRecyclerViewAdapter.MyViewHolder>(), Filterable {

    public var dataListFilter: List<RuleEngineBankData>?
    private var itemCallBack: itemClickCallBack = itemClick!!
    private var mContext: Activity = context

    init {
        dataListFilter = dataListValue as List<RuleEngineBankData>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.v2_banking_partner_layout, parent, false)

        return MyViewHolder(listItem)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        Picasso.get()
            .load(dataListFilter!![position].logoUrl)
            .into(holder.ivIcon, object : Callback {
                override fun onSuccess() {

                }

                override fun onError(ex: Exception) {

                }
            })


        holder.tvRateOfIntrest.text = dataListFilter!![position].roi.toString()

        holder.llMainLayout.setOnClickListener(View.OnClickListener {
            itemCallBack.itemClick(dataListFilter?.get(position), position)
        })
        if (dataListFilter!!.size - 1 == position) {

            val params = holder.rlRoot.layoutParams as ViewGroup.MarginLayoutParams
            params.rightMargin = holder.llMainLayout.paddingLeft
            holder.rlRoot.layoutParams = params
        }
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvRateOfIntrest: TextView
        var tvKnowMore: TextView
        var llMainLayout: LinearLayout
        var rlRoot: RelativeLayout

        var ivIcon: ImageView

        init {
            rlRoot = itemView.findViewById(R.id.rl_root)
            tvRateOfIntrest = itemView.findViewById(R.id.tv_rate_of_intrest)
            tvKnowMore = itemView.findViewById(R.id.tv_know_more)
            llMainLayout = itemView.findViewById(R.id.ll_main_layout)

            ivIcon = itemView.findViewById(R.id.iv_icon)
        }
    }

    override fun getItemCount(): Int {
        return dataListFilter!!.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                dataListFilter = if (charSearch.isEmpty()) {
                    dataListValue as ArrayList<RuleEngineBankData>
                } else {
                    val resultList = ArrayList<RuleEngineBankData>()
                    for (row in dataListValue!!) {
                        if (row.bankName.toString().toLowerCase()
                                .contains(constraint.toString().toLowerCase())
                        ) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = dataListFilter
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                dataListFilter = results?.values as ArrayList<RuleEngineBankData>
                notifyDataSetChanged()
            }
        }
    }


}