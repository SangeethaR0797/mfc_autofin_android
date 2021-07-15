package v2.view.adapter

import android.app.Activity
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import v2.model.dto.DataSelectionDTO
import v2.view.callBackInterface.itemClickCallBack

class AdditionalFieldsDTOAdapter (
        var context: Activity,
        var dataListValue: List<DataSelectionDTO>?,
        itemClick: itemClickCallBack?
) : RecyclerView.Adapter<DataRecyclerViewAdapter.MyViewHolder>(), Filterable {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataRecyclerViewAdapter.MyViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: DataRecyclerViewAdapter.MyViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }
}