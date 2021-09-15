package v2.view.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mfc.autofin.framework.R
import v2.model.dto.MenuDTO
import v2.model.enum_class.MenuEnum
import v2.view.callBackInterface.itemClickCallBack

class MenuForDashboardAdapter(
    var context: Activity,
    var dataListValue: List<MenuDTO>?,
    itemClick: itemClickCallBack?
) : RecyclerView.Adapter<MenuForDashboardAdapter.MyViewHolder>(), Filterable {

    public var dataListFilter: List<MenuDTO>?
    private var itemCallBack: itemClickCallBack = itemClick!!
    private var mContext: Activity = context

    init {
        dataListFilter = dataListValue as List<MenuDTO>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.v2_menu_layout, parent, false)

        return MyViewHolder(listItem)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (dataListFilter!![position].menuCode.equals(MenuEnum.Add_New.value)) {
            holder.tvCount.text = " ";
        } else {
            if (dataListFilter!![position].total < 10) {
                holder.tvCount.text = "0" + dataListFilter!![position].total.toString()
            } else {
                holder.tvCount.text = dataListFilter!![position].total.toString()
            }
        }

        holder.tvMenuName.text = dataListFilter?.get(position)?.menuName

        if (dataListFilter?.get(position)?.amount != null) {
            holder.tvAmount.visibility = View.VISIBLE
            holder.tvAmount.text = "₹" + dataListFilter?.get(position)?.amount
        } else {
            holder.tvAmount.visibility = View.INVISIBLE
        }
        if (dataListFilter?.get(position)?.menuIcon!! > 0) {
            holder.ivIcon.scaleType = ImageView.ScaleType.FIT_XY
            holder.ivIcon.setBackgroundResource(dataListFilter?.get(position)?.menuIcon!!)
        }

        holder.llMainLayout.setOnClickListener(View.OnClickListener {
            itemCallBack.itemClick(dataListFilter?.get(position), position)
        })

        holder.llMainLayout.setBackgroundResource(dataListFilter!![position].backgroundResource)
        holder.llMainLayout.invalidate()

    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var llMainLayout: ConstraintLayout
        var tvCount: TextView
        var tvMenuName: TextView
        var tvAmount: TextView
        var ivIcon: ImageView

        init {
            llMainLayout = itemView.findViewById(R.id.ll_main_layout)

            tvCount = itemView.findViewById(R.id.tv_count)
            tvMenuName = itemView.findViewById(R.id.tv_menu_name)

            tvAmount = itemView.findViewById(R.id.tv_amount)
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
                    dataListValue as ArrayList<MenuDTO>
                } else {
                    val resultList = ArrayList<MenuDTO>()
                    for (row in dataListValue!!) {
                        if (row.menuName.toString().toLowerCase()
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
                dataListFilter = results?.values as ArrayList<MenuDTO>
                notifyDataSetChanged()
            }
        }
    }


}