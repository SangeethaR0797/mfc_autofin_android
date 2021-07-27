package v2.view.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mfc.autofin.framework.R
import v2.model.dto.DataSelectionDTO
import v2.view.callBackInterface.itemClickCallBack

public class AdditionalFieldsDTOAdapter (
        var context: Activity,
        var dataListValue: List<DataSelectionDTO>?,
        itemClick: itemClickCallBack?
) : RecyclerView.Adapter<AdditionalFieldsDTOAdapter.AdditionalViewHolder>() {

    lateinit var currentSectionButton:Button

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):AdditionalViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem =
                layoutInflater.inflate(R.layout.v2_application_item_data_layout, parent, false)

        return AdditionalViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: AdditionalViewHolder, position: Int) {
        if (dataListValue?.get(position)?.displayValue?.isNotEmpty() == true) {
            holder.textViewTitleLabel.visibility=View.VISIBLE
            holder.textViewTitleLabel.text=dataListValue?.get(position)?.displayValue
        }

    }

    override fun getItemCount(): Int {
        return dataListValue!!.size
    }

    class AdditionalViewHolder(currentSectionLayout: View) : RecyclerView.ViewHolder(currentSectionLayout) {
        var linearLayout: LinearLayout = currentSectionLayout.findViewById<LinearLayout>(R.id.linearLayoutCustomAddressSectionLayout)
        var textViewTitleLabel:TextView=currentSectionLayout.findViewById(R.id.textViewTitleLabel)
        var addressButton: Button = currentSectionLayout.findViewById<Button>(R.id.buttonSubmitAddressDetails)
    }
}