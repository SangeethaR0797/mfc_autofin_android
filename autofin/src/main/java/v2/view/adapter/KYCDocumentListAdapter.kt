package v2.view.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import com.mfc.autofin.framework.R
import v2.model.response.master.Docs

class KYCDocumentListAdapter(val activity: Context,val docList:List<Docs>): BaseAdapter()
{
    override fun getCount(): Int {
       return docList.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View
    {
        var convertView=convertView
        convertView=LayoutInflater.from(activity).inflate(R.layout.v2_document_check_ist,parent,false)
    return convertView
    }
}