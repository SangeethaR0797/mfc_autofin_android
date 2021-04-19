package v2.view.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mfc.autofin.framework.R
import controller.ReviewAdapter
import model.custom_model.ReviewData
import v2.view.callBackInterface.itemClickCallBack

class StringDataRecyclerViewAdapter(dataListValue: List<String>?, itemClick: itemClickCallBack?) : RecyclerView.Adapter<StringDataRecyclerViewAdapter.MyViewHolder>() {

    private var dataList: List<String>? = dataListValue
    private var itemCallBack: itemClickCallBack = itemClick!!


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.v2_string_item_layout, parent, false)

        return MyViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvItem.text = dataList!!.get(position).toString()
        holder.tvItem.setOnClickListener(View.OnClickListener {
            itemCallBack.itemClick(dataList!!.get(position), position)
        })
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvItem: TextView

        init {
            tvItem = itemView.findViewById(R.id.tv_item)
        }
    }

    override fun getItemCount(): Int {
        return dataList!!.size
    }


}