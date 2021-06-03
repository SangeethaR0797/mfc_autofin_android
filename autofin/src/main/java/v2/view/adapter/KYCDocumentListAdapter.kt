package v2.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.view.get
import com.mfc.autofin.framework.R
import v2.model.response.master.Docs
import v2.view.callBackInterface.itemClickCallBack


class KYCDocumentListAdapter(val activity: Context, val docList: List<Docs>, itemClickCallBack: itemClickCallBack) : BaseAdapter() {

    val itemClick = itemClickCallBack
    var selectedPosition=-1
    override fun getCount(): Int {
        return docList.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: ViewHolder
        var rowView = convertView
        if (rowView == null) {
            holder = ViewHolder()
            rowView = LayoutInflater.from(activity).inflate(R.layout.v2_document_check_ist, null)
            holder.checkBox = rowView.findViewById(R.id.checkBoxDocumentSelection) as CheckBox
            rowView.tag = holder
        } else {
            holder = rowView.tag as ViewHolder
        }

        holder.checkBox?.text = docList[position].displayLabel
        holder.checkBox?.isChecked = position==selectedPosition
        holder.checkBox?.setOnClickListener(View.OnClickListener {

            selectedPosition = if(holder.checkBox!!.isChecked) {
                position
            } else {
                -1
            }
            itemClick.itemClick(docList[position], position)
            notifyDataSetChanged()
        })

        return rowView!!
    }


    internal class ViewHolder {
        var checkBox: CheckBox? = null
    }
}