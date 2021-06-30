package v2.view.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.mfc.autofin.framework.R

import v2.model.response.NoticeData

import v2.view.callBackInterface.itemClickCallBack

class NoticeRecyclerViewAdapter(
    var context: Activity,
    var dataListValue: List<NoticeData>?,
    itemClick: itemClickCallBack?
) : RecyclerView.Adapter<NoticeRecyclerViewAdapter.MyViewHolder>(), Filterable {

    public var dataListFilter: List<NoticeData>?
    private var itemCallBack: itemClickCallBack = itemClick!!
    private var mContext: Activity = context

    init {
        dataListFilter = dataListValue as List<NoticeData>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.v2_notice_layout, parent, false)

        return MyViewHolder(listItem)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tvItem.text = dataListFilter!![position].text!!.replace("  ", " ")!!.toString()
        var desc =
            dataListFilter?.get(position)?.description!!.replace("  ", " ")!!.toString()
        holder.tvItemSmall.text = desc
        if (dataListFilter?.get(position)!!.showMore == true) {
            holder.tvItem.maxLines = 5
            holder.tvItemSmall.maxLines = 500
            holder.tvShowMore.text = "Less"
        } else {
            holder.tvItem.maxLines = 2
            holder.tvItemSmall.maxLines = 2
            holder.tvShowMore.text = "More"
        }
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


        holder.tvShowMore.post(Runnable {
            if (dataListFilter?.get(position)!!.visibilityOfShowMore == null) {
                val l: Layout = holder.tvItemSmall.getLayout()
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
                    dataListValue as ArrayList<NoticeData>
                } else {
                    val resultList = ArrayList<NoticeData>()
                    for (row in dataListValue!!) {
                        if (row.text.toString().toLowerCase()
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
                dataListFilter = results?.values as ArrayList<NoticeData>
                notifyDataSetChanged()
            }
        }
    }


}