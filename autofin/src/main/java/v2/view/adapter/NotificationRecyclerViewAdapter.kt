package v2.view.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.mfc.autofin.framework.R


import v2.model.response.NotificationItemData

import v2.view.callBackInterface.itemClickCallBack

class NotificationRecyclerViewAdapter(
    var context: Activity,
    var dataListValue: List<NotificationItemData>?,
    itemClick: itemClickCallBack?
) : RecyclerView.Adapter<NotificationRecyclerViewAdapter.MyViewHolder>(), Filterable {

    public var dataListFilter: List<NotificationItemData>?
    private var itemCallBack: itemClickCallBack = itemClick!!
    private var mContext: Activity = context

    init {
        dataListFilter = dataListValue as List<NotificationItemData>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.v2_notice_layout, parent, false)

        return MyViewHolder(listItem)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tvItem.text = dataListFilter!![position].message!!.replace("  ", " ")!!.toString()
        var desc = dataListFilter?.get(position)?.description!!.replace("  ", " ")!!.toString()
        holder.tvItemSmall.text = desc


        if (dataListFilter?.get(position)!!.showMore == true) {
            holder.tvItemSmall.maxLines = 500
            holder.tvShowMore.text = "Less"
            holder.tvItem.maxLines = 5
        } else {
            holder.tvItemSmall.maxLines = 2
            holder.tvShowMore.text = "More"
            holder.tvItem.maxLines = 2
        }
        /*holder.tvShowMore.setOnClickListener(View.OnClickListener {
            itemCallBack.itemClick(dataListFilter?.get(position), position)
        })*/
        holder.llMainLayout.setOnClickListener(View.OnClickListener {
            if (dataListFilter?.get(position)!!.showMore == false || dataListFilter?.get(position)!!.showMore == null) {
                dataListFilter?.get(position)!!.showMore = true
            } else {
                dataListFilter?.get(position)!!.showMore = false
            }

            itemCallBack.itemClick(dataListFilter?.get(position), position)
        })
        holder.tvShowMore.setOnClickListener(View.OnClickListener {
            if (dataListFilter?.get(position)!!.showMore == false || dataListFilter?.get(position)!!.showMore == null) {
                dataListFilter?.get(position)!!.showMore = true
            } else {
                dataListFilter?.get(position)!!.showMore = false
            }

            itemCallBack.itemClick(dataListFilter?.get(position), position)
        })


    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvItem: TextView
        var tvShowMore: TextView
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
            tvShowMore = itemView.findViewById(R.id.tv_show_more)
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
                    dataListValue as ArrayList<NotificationItemData>
                } else {
                    val resultList = ArrayList<NotificationItemData>()
                    for (row in dataListValue!!) {
                        if (row.message.toString().toLowerCase()
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
                dataListFilter = results?.values as ArrayList<NotificationItemData>
                notifyDataSetChanged()
            }
        }
    }


}