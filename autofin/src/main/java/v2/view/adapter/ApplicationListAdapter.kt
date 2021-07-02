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
import v2.model.response.ApplicationDataItems
import v2.view.base.BaseFragment

import v2.view.callBackInterface.ApplicationListClickCallBack

class ApplicationListAdapter(
    var baseFragment: BaseFragment,
    var dataListValue: List<ApplicationDataItems>?,
    itemClick: ApplicationListClickCallBack?
) : RecyclerView.Adapter<ApplicationListAdapter.MyViewHolder>(), Filterable {

    public var dataListFilter: List<ApplicationDataItems>?
    private var itemCallBack: ApplicationListClickCallBack = itemClick!!
    private var mBaseFragment: BaseFragment = baseFragment

    init {
        dataListFilter = dataListValue as List<ApplicationDataItems>

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem =
            layoutInflater.inflate(R.layout.v2_application_item_data_layout, parent, false)

        return MyViewHolder(listItem)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tvStatus.text = dataListFilter!![position].status.toString()
        holder.tvApplicantName.text = dataListFilter?.get(position)?.firstName + " " + dataListFilter?.get(position)?.lastName
        holder.tvVehicleDetails.text =
            dataListFilter!![position].make.toString() + " " + dataListFilter!![position].model.toString()

        holder.tvBankIcon.visibility = View.GONE

        if (dataListFilter!![position].caseId != null) {
            holder.tvCaseId.visibility = View.VISIBLE
            holder.tvCaseIdCaption.visibility = View.VISIBLE
            holder.tvCaseId.text = dataListFilter!![position].caseId!!.toString()
        } else {
            holder.tvCaseId.visibility = View.GONE
            holder.tvCaseIdCaption.visibility = View.GONE
        }

        if (dataListFilter!![position].losId != null) {
            holder.btnComplete.visibility = View.GONE
            holder.tvLOSId.visibility = View.VISIBLE
            holder.tvLOSIdCaption.visibility = View.VISIBLE
            holder.tvLOSId.text = dataListFilter!![position].losId!!.toString()
        } else {
            holder.tvLOSId.visibility = View.GONE
            holder.tvLOSIdCaption.visibility = View.GONE
            if(dataListFilter!![position].status==baseFragment.resources.getString(R.string.v2_lead_status_submitted_to_bank))
                holder.btnComplete.text =baseFragment.resources.getString(R.string.btn_lbl_view_details)
            else
                holder.btnComplete.text =baseFragment.resources.getString(R.string.btn_lbl_complete)
            holder.btnComplete.visibility = View.VISIBLE
        }
        if (mBaseFragment != null) {
            var displayValue = mBaseFragment.stringToDateString(
                dataListFilter!![position].createdDate!!.toString(),
                mBaseFragment.DATE_FORMATE_YYYYMMDD,
                mBaseFragment.DATE_FORMATE_DDMMMYYYY
            )
            holder.tvDate.text = displayValue
        } else {
            holder.tvDate.text = dataListFilter!![position].createdDate!!.toString()
        }





        holder.clMainLayout.setOnClickListener(View.OnClickListener {
            itemCallBack.onItemClick(dataListFilter?.get(position), position)
        })
        holder.rlCall.setOnClickListener(View.OnClickListener {
            itemCallBack.onCallClick(dataListFilter?.get(position), position)
        })
        holder.btnComplete.setOnClickListener(View.OnClickListener {
            if(dataListFilter!![position].status==baseFragment.resources.getString(R.string.v2_lead_status_submitted_to_bank))
            {
                itemCallBack.onItemClick(dataListFilter?.get(position), position)
            }else
                itemCallBack.onCompleteClick(dataListFilter?.get(position), position)

        })

    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var clMainLayout: ConstraintLayout
        var tvStatus: TextView
        var tvApplicantName: TextView
        var tvVehicleDetails: TextView
        var ivIcon: ImageView
        var tvBankIcon: ImageView
        var rlCall: RelativeLayout
        var tvCaseId: TextView
        var tvCaseIdCaption: TextView
        var tvLOSId: TextView
        var tvLOSIdCaption: TextView
        var tvDate: TextView
        var btnComplete: Button

        init {
            clMainLayout = itemView.findViewById(R.id.cl_main_layout)

            tvStatus = itemView.findViewById(R.id.tv_status)
            tvApplicantName = itemView.findViewById(R.id.tv_applicant_name)

            tvVehicleDetails = itemView.findViewById(R.id.tv_vehicle_details)
            ivIcon = itemView.findViewById(R.id.iv_icon)
            tvBankIcon = itemView.findViewById(R.id.tv_bank_icon)
            rlCall = itemView.findViewById(R.id.rl_call)
            tvCaseId = itemView.findViewById(R.id.tv_case_id)
            tvCaseIdCaption = itemView.findViewById(R.id.tv_case_id_caption)
            tvLOSId = itemView.findViewById(R.id.tv_los_id)
            tvLOSIdCaption = itemView.findViewById(R.id.tv_los_id_caption)
            tvDate = itemView.findViewById(R.id.tv_date)
            btnComplete = itemView.findViewById(R.id.btn_complete)
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
                    dataListValue as ArrayList<ApplicationDataItems>
                } else {
                    val resultList = ArrayList<ApplicationDataItems>()
                    for (row in dataListValue!!) {
                        if (row.customerId.toString().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                row.firstName.toString().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                row.customerMobile.toString().toLowerCase().contains(constraint.toString().toLowerCase())||
                                        row.bankName.toString().toLowerCase().contains(constraint.toString().toLowerCase())||
                                row.losId.toString().toLowerCase().contains(constraint.toString().toLowerCase())||
                                        row.caseId.toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
                dataListFilter = results?.values as ArrayList<ApplicationDataItems>
                notifyDataSetChanged()
            }
        }
    }


}