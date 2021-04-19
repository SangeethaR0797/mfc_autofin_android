package v2.view.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mfc.autofin.framework.R
import controller.ReviewAdapter
import model.custom_model.ReviewData
import v2.view.callBackInterface.itemClickCallBack

class StringDataRecyclerViewAdapter(var dataListValue: List<String>?, itemClick: itemClickCallBack?) : RecyclerView.Adapter<StringDataRecyclerViewAdapter.MyViewHolder>(), Filterable {

    public var dataListFilter: List<String>?
    private var itemCallBack: itemClickCallBack = itemClick!!
    init {
        dataListFilter = dataListValue as List<String>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.v2_string_item_layout, parent, false)

        return MyViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvItem.text = dataListFilter!!.get(position).toString()
        holder.tvItem.setOnClickListener(View.OnClickListener {
            itemCallBack.itemClick(dataListFilter!!.get(position), position)
        })
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvItem: TextView

        init {
            tvItem = itemView.findViewById(R.id.tv_item)
        }
    }

    override fun getItemCount(): Int {
        return dataListFilter!!.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    dataListFilter = dataListValue as ArrayList<String>
                } else {
                    val resultList = ArrayList<String>()
                    for (row in dataListValue!!) {
                        if (row.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            resultList.add(row)
                        }
                    }
                    dataListFilter = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = dataListFilter
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                dataListFilter = results?.values as ArrayList<String>
                notifyDataSetChanged()
            }
        }
    }


}