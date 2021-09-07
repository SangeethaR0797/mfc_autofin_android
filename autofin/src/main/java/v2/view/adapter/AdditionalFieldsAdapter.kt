package v2.view.adapter

import android.telecom.Call
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mfc.autofin.framework.R
import v2.model.response.master.Details
import v2.view.callBackInterface.AdditionalFieldsDetailsInterface
import v2.view.callBackInterface.itemClickCallBack
import java.util.*
import kotlin.collections.ArrayList

class AdditionalFieldsAdapter(var apiURL: String, var dataListValue: List<Details>, additionalFieldsCallBack: AdditionalFieldsDetailsInterface) : RecyclerView.Adapter<AdditionalFieldsAdapter.MyViewHolder>(), Filterable {

    public var dataListFilter: List<Details>
    private var detailsCallBack: AdditionalFieldsDetailsInterface = additionalFieldsCallBack

    init {
        dataListFilter = dataListValue as List<Details>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.v2_string_item_layout, parent, false)

        return MyViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvItem.text = dataListFilter[position].displayLabel
        holder.tvItem.setOnClickListener(View.OnClickListener {
            detailsCallBack.returnDetails(dataListFilter[position])
        })
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvItem: TextView = itemView.findViewById(R.id.tv_item)

    }

    override fun getItemCount(): Int {
        return dataListFilter.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?)
            : FilterResults {
                val charSearch = constraint.toString()
                dataListFilter = if (charSearch.isEmpty()) {
                    dataListValue as ArrayList<Details>
                } else {

                    val resultList = ArrayList<Details>()
                    for (row in dataListValue) {
                        if (row.displayLabel.toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))) {
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
                dataListFilter = results?.values as ArrayList<Details>
                notifyDataSetChanged()
            }
        }
    }

    public fun updateList(filteredList: List<Details>) {
        dataListFilter = filteredList
        notifyDataSetChanged()
    }
}