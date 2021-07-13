package v2.view.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Typeface
import android.os.Build
import android.text.Layout
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

    var dataListFilter: List<NotificationItemData>?
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

        holder.tvItem.text = dataListFilter!![position].message!!.replace("  ", " ").toString()
        var desc = dataListFilter?.get(position)?.description!!.replace("  ", " ").toString()
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
            dataListFilter?.get(position)!!.showMore = dataListFilter?.get(position)!!.showMore == false || dataListFilter?.get(position)!!.showMore == null

            itemCallBack.itemClick(dataListFilter?.get(position), position)
        })
        holder.tvShowMore.setOnClickListener(View.OnClickListener {
            dataListFilter?.get(position)!!.showMore = dataListFilter?.get(position)!!.showMore == false || dataListFilter?.get(position)!!.showMore == null

            itemCallBack.itemClick(dataListFilter?.get(position), position)
        })

        holder.tvShowMore.post(Runnable {
            if (dataListFilter?.get(position)!!.visibilityOfShowMore == null) {
                val l: Layout = holder.tvItemSmall.layout
                if (l != null) {
                    val lines: Int = l.lineCount
                    if (lines > 0) {
                        if (l.getEllipsisCount(lines - 1) > 0) {
                            dataListFilter?.get(position)!!.visibilityOfShowMore = true
                        } else {
                            dataListFilter?.get(position)!!.visibilityOfShowMore
                        }
                    }
                }
            }

            if (dataListFilter?.get(position)!!.visibilityOfShowMore == true) {
                holder.tvShowMore.visibility = View.VISIBLE
            } else {
                holder.tvShowMore.visibility = View.GONE
            }
        })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (dataListFilter?.get(position)!!.isNew == true) {
                holder.tvItem.setTextAppearance(R.style.RobotoBold)
            } else {
                holder.tvItem.setTextAppearance(R.style.RobotoRegular)
            }
        }


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