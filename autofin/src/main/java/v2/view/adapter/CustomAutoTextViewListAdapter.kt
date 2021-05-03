package v2.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import androidx.annotation.NonNull
import com.mfc.autofin.framework.R


class CustomAutoTextViewListAdapter(private val mContext: Context, private val itemLayout: Int, private var dataList: List<String>?) : ArrayAdapter<Any?>(mContext, itemLayout, dataList!!) {
    private val listFilter: ListFilter = ListFilter()
    private var dataListAllItems: List<String>? = null
    override fun getCount(): Int {
        return dataList!!.size
    }

    override fun getItem(position: Int): String? {
        Log.d("CustomListAdapter",
                dataList!![position])
        return dataList!![position]
    }

    override fun getView(position: Int, view: View?, @NonNull parent: ViewGroup): View {
        var view = view
        if (view == null) {
            view = LayoutInflater.from(parent.context)
                    .inflate(itemLayout, parent, false)
        }
        val strName = view!!.findViewById<View>(R.id.tv_item) as TextView
        strName.text = getItem(position)
        return view
    }

    @NonNull
    override fun getFilter(): Filter {
        return listFilter
    }

    inner class ListFilter : Filter() {
        private val lock = Any()
        override fun performFiltering(prefix: CharSequence): FilterResults {
            val results = FilterResults()
            if (dataListAllItems == null) {
                synchronized(lock) { dataListAllItems = ArrayList(dataList!!) }
            }
            if (prefix == null || prefix.length == 0) {
                synchronized(lock) {
                    results.values = dataListAllItems
                    results.count = dataListAllItems!!.size
                }
            } else {
                val searchStrLowerCase = prefix.toString().toLowerCase()
                val matchValues = ArrayList<String>()
                for (dataItem in dataListAllItems!!) {
                    if (dataItem.toLowerCase().startsWith(searchStrLowerCase)) {
                        matchValues.add(dataItem)
                    }
                }
                results.values = matchValues
                results.count = matchValues!!.size
            }
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            dataList = if (results.values != null) {
                results.values as ArrayList<String>
            } else {
                null
            }
            if (results.count > 0) {
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
        }
    }
}