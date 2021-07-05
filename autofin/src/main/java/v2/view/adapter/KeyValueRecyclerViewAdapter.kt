package v2.view.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.mfc.autofin.framework.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import v2.model.dto.KeyValueDTO

import v2.view.callBackInterface.itemClickCallBack

class KeyValueRecyclerViewAdapter(
    var context: Activity,
    var dataListValue: List<KeyValueDTO>?,
    itemClick: itemClickCallBack?
) : RecyclerView.Adapter<KeyValueRecyclerViewAdapter.MyViewHolder>(), Filterable {

    public var dataListFilter: List<KeyValueDTO>?
    private var itemCallBack: itemClickCallBack = itemClick!!
    private var mContext: Activity = context

    init {
        dataListFilter = dataListValue as List<KeyValueDTO>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.v2_key_value_layout, parent, false)

        return MyViewHolder(listItem)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tvKey.text = dataListFilter!![position].key
        if (!TextUtils.isEmpty(dataListFilter!![position].value)) {
            holder.tvValue.text = dataListFilter!![position].value
            if (dataListFilter!![position].key.equals("Case id") || dataListFilter!![position].key.equals(
                    "Mobile No"
                )
            ) {
                holder.tvValue.setTextIsSelectable(true)
            } else {
                holder.tvValue.setTextIsSelectable(false)
            }
        } else {
            holder.tvValue.text = ""
            holder.tvValue.hint = "NA"
        }

        holder.llMainLayout.setOnClickListener(View.OnClickListener {
            itemCallBack.itemClick(dataListFilter?.get(position), position)
        })


    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvKey: TextView
        var tvValue: TextView
        var llMainLayout: LinearLayout


        init {
            tvKey = itemView.findViewById(R.id.tv_key)
            tvValue = itemView.findViewById(R.id.tv_value)
            llMainLayout = itemView.findViewById(R.id.ll_main_layout)

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
                    dataListValue as ArrayList<KeyValueDTO>
                } else {
                    val resultList = ArrayList<KeyValueDTO>()
                    for (row in dataListValue!!) {
                        if (row.key.toString().toLowerCase()
                                .contains(constraint.toString().toLowerCase()) ||
                            row.key.toString().toLowerCase()
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
                dataListFilter = results?.values as ArrayList<KeyValueDTO>
                notifyDataSetChanged()
            }
        }
    }


}