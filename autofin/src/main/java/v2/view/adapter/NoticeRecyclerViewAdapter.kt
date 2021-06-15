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
import v2.model.dto.Notice

import v2.view.callBackInterface.itemClickCallBack

class NoticeRecyclerViewAdapter(var context: Activity, var dataListValue: List<Notice>?, itemClick: itemClickCallBack?) : RecyclerView.Adapter<NoticeRecyclerViewAdapter.MyViewHolder>(), Filterable {

    public var dataListFilter: List<Notice>?
    private var itemCallBack: itemClickCallBack = itemClick!!
    private var mContext: Activity = context

    init {
        dataListFilter = dataListValue as List<Notice>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.v2_notice_layout, parent, false)

        return MyViewHolder(listItem)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tvItem.text = dataListFilter!![position].text
        holder.tvItemSmall.text = dataListFilter?.get(position)?.description

        holder.llMainLayout.setOnClickListener(View.OnClickListener {
            itemCallBack.itemClick(dataListFilter?.get(position), position)
        })



    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvItem: TextView
        var tvItemSmall: TextView
        var llMainLayout: LinearLayout
        var llTextData: LinearLayout
        var ivIcon: ImageView

        init {
            tvItem = itemView.findViewById(R.id.tv_item)
            tvItemSmall = itemView.findViewById(R.id.tv_item_small)
            llMainLayout = itemView.findViewById(R.id.ll_main_layout)
            llTextData = itemView.findViewById(R.id.ll_text_data)
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
                    dataListValue as ArrayList<Notice>
                } else {
                    val resultList = ArrayList<Notice>()
                    for (row in dataListValue!!) {
                        if (row.text.toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
                dataListFilter = results?.values as ArrayList<Notice>
                notifyDataSetChanged()
            }
        }
    }


}