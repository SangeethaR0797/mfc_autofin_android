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
import v2.model.dto.DataSelectionDTO
import v2.view.callBackInterface.itemClickCallBack

class DataRecyclerViewAdapter(var context: Activity, var dataListValue: List<DataSelectionDTO>?, itemClick: itemClickCallBack?) : RecyclerView.Adapter<DataRecyclerViewAdapter.MyViewHolder>(), Filterable {

    public var dataListFilter: List<DataSelectionDTO>?
    private var itemCallBack: itemClickCallBack = itemClick!!
    private var mContext: Activity = context

    init {
        dataListFilter = dataListValue as List<DataSelectionDTO>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.v2_data_item_layout, parent, false)

        return MyViewHolder(listItem)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tvItem.text = dataListFilter!![position].displayValue
        if (TextUtils.isEmpty(dataListFilter!![position].displayValuePostFix)) {
            holder.tvItemSmall.visibility = View.GONE
        } else {
            holder.tvItemSmall.visibility = View.VISIBLE
            holder.tvItemSmall.text = dataListFilter?.get(position)?.displayValuePostFix
        }
        holder.llMainLayout.setOnClickListener(View.OnClickListener {
            itemCallBack.itemClick(dataListFilter?.get(position), position)
        })

        holder.llMainLayout.setBackgroundResource(R.drawable.vtwo_input_bg)
        holder.tvItem.setTextAppearance(mContext, R.style.RobotoRegular)
        holder.tvItemSmall.setTextAppearance(mContext, R.style.RobotoRegular)
        holder.tvItem.setTextColor(mContext.resources.getColor(R.color.vtwo_light_grey))
        holder.tvItemSmall.setTextColor(mContext.resources.getColor(R.color.vtwo_light_grey))

        if (dataListFilter!!.get(position).selected) {
            holder.llMainLayout.setBackgroundResource(R.drawable.vtwo_input_yellow)
            holder.tvItem.setTextAppearance(mContext, R.style.RobotoMedium)
            holder.tvItemSmall.setTextAppearance(mContext, R.style.RobotoMedium)

            holder.tvItem.setTextColor(mContext.resources.getColor(R.color.vtwo_black))
            holder.tvItemSmall.setTextColor(mContext.resources.getColor(R.color.vtwo_black))
        }
        if (dataListFilter!![position].imageUrl == null) {
            holder.llTextData.visibility = View.VISIBLE
            holder.ivIcon.visibility = View.GONE
        } else {
           // holder.llTextData.visibility = View.GONE
            holder.ivIcon.visibility = View.GONE
            Picasso.get()
                    .load(dataListFilter!![position].imageUrl)
                    .into(holder.ivIcon, object : Callback {
                        override fun onSuccess() {
                            holder.ivIcon.visibility = View.VISIBLE
                            holder.llTextData.visibility = View.GONE
                        }

                        override fun onError(ex: Exception) {
                            holder.ivIcon.visibility = View.GONE
                            holder.llTextData.visibility = View.VISIBLE
                        }
                    })


        }
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
                    dataListValue as ArrayList<DataSelectionDTO>
                } else {
                    val resultList = ArrayList<DataSelectionDTO>()
                    for (row in dataListValue!!) {
                        if (row.displayValue.toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
                dataListFilter = results?.values as ArrayList<DataSelectionDTO>
                notifyDataSetChanged()
            }
        }
    }


}