package v2.view.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.mfc.autofin.framework.R
import v2.model.response.master.Addres

public class PostSoftOfferAdapter(var context: Activity, var list: List<Addres>?, addressSelectionCallBackInstance: AddressSelectionCallBack?) : RecyclerView.Adapter<PostSoftOfferAdapter.EquiFaxAddressViewHolder>()  {

    private var addressSelectionCallBackInstance: AddressSelectionCallBack = addressSelectionCallBackInstance!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquiFaxAddressViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.v2_equi_fax_address_item_layout, parent, false)
        return EquiFaxAddressViewHolder(view)

    }

    override fun onBindViewHolder(holder: EquiFaxAddressViewHolder, position: Int)
    {
        val customerAddress= list?.get(position)
        holder.checkBoxEquiFaxAddress.text=customerAddress?.address+ customerAddress?.pincode
        holder.checkBoxEquiFaxAddress.setOnClickListener(View.OnClickListener {
            holder.checkBoxEquiFaxAddress.isChecked=true
            if( holder.linearLayoutIsPermanentAddress.visibility!=View.VISIBLE)
                holder.linearLayoutIsPermanentAddress.visibility=View.VISIBLE

        })
        holder.buttonPermanentAddress.setOnClickListener(View.OnClickListener
        {
            if(holder.checkboxIsPermanentAddress.isChecked)
            {
                addressSelectionCallBackInstance.onSelect(customerAddress, true)
            }
            else
            {
                addressSelectionCallBackInstance.onSelect(customerAddress, false)
            }
        })
    }

    override fun getItemCount(): Int {
      return list!!.size
    }

    class EquiFaxAddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val checkBoxEquiFaxAddress: CheckBox =itemView.findViewById(R.id.checkBoxEquiFaxAddress)
        val linearLayoutIsPermanentAddress: LinearLayout =itemView.findViewById(R.id.linearLayoutIsPermanentAddress)
        val checkboxIsPermanentAddress: CheckBox = itemView.findViewById(R.id.checkboxIsPermanentAddress)
        val buttonPermanentAddress: Button = itemView.findViewById(R.id.buttonPermanentAddress)

    }

    interface AddressSelectionCallBack
    {
        fun onSelect(address: Addres?, isPermanentAddress: Boolean)

    }
}