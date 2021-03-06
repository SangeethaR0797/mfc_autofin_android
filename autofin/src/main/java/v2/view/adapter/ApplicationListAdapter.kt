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
import v2.model.enum_class.ApplicationStatusEnum
import v2.model.enum_class.ScreenTypeEnum
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import v2.model.response.ApplicationDataItems
import v2.view.base.BaseFragment

import v2.view.callBackInterface.ApplicationListClickCallBack

class ApplicationListAdapter(
    var baseFragment: BaseFragment,
    var dataListValue: List<ApplicationDataItems>,
    itemClick: ApplicationListClickCallBack
) : RecyclerView.Adapter<ApplicationListAdapter.MyViewHolder>(), Filterable {

    public var dataListFilter: List<ApplicationDataItems>?
    private var itemCallBack: ApplicationListClickCallBack = itemClick
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

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tvStatus.text = dataListFilter?.get(position)?.status.toString()
        holder.tvApplicantName.text =
            dataListFilter?.get(position)?.firstName + " " + dataListFilter?.get(position)?.lastName
        holder.tvVehicleDetails.text =
            dataListFilter?.get(position)?.make.toString() + " " + dataListFilter?.get(position)?.model.toString()

        holder.tvBankIcon.visibility = View.GONE

        if (dataListFilter?.get(position)?.caseId != null) {
            holder.tvCaseId.visibility = View.VISIBLE
            holder.tvCaseIdCaption.visibility = View.VISIBLE
            holder.tvCaseId.text = dataListFilter?.get(position)?.caseId?.toString()
        } else {
            holder.tvCaseId.visibility = View.GONE
            holder.tvCaseIdCaption.visibility = View.GONE
        }

        if (dataListFilter?.get(position)?.losId != null) {
            //holder.btnComplete.visibility = View.GONE
            holder.tvLOSId.visibility = View.VISIBLE
            holder.tvLOSIdCaption.visibility = View.VISIBLE
            holder.tvLOSId.text = dataListFilter?.get(position)?.losId?.toString()
            if(dataListFilter?.get(position)?.status==baseFragment.resources.getString(R.string.v2_lead_status_submitted_to_bank)||
                    dataListFilter?.get(position)?.status==ScreenTypeEnum.Disbursed.value||
                    dataListFilter?.get(position)?.status==ScreenTypeEnum.Approved.value||
                    dataListFilter?.get(position)?.status==ApplicationStatusEnum.Rejected.value||
                    dataListFilter?.get(position)?.status==ApplicationStatusEnum.Closed.value)
            {
                holder.btnComplete.text =baseFragment.resources.getString(R.string.btn_lbl_view_details)
            }
            else
                holder.btnComplete.text =baseFragment.resources.getString(R.string.btn_lbl_complete)

        } else {
            holder.tvLOSId.visibility = View.GONE
            holder.tvLOSIdCaption.visibility = View.GONE
            holder.btnComplete.visibility = View.VISIBLE
            if(dataListFilter?.get(position)?.status==baseFragment.resources.getString(R.string.v2_lead_status_submitted_to_bank))
            {
                holder.btnComplete.text =baseFragment.resources.getString(R.string.btn_lbl_view_details)
            }
            else
                holder.btnComplete.text =baseFragment.resources.getString(R.string.btn_lbl_complete)


        }
        val displayValue = dataListFilter?.get(position)?.createdDate?.toString()?.let {
            mBaseFragment.stringToDateString(
                    it,
                mBaseFragment.DATE_FORMATE_YYYYMMDD,
                mBaseFragment.DATE_FORMATE_DDMMMYYYY
            )
        }
        holder.tvDate.text = displayValue





        holder.clMainLayout.setOnClickListener(View.OnClickListener {
            itemCallBack.onItemClick(dataListFilter?.get(position), position)
        })
        holder.rlCall.setOnClickListener(View.OnClickListener {
            itemCallBack.onCallClick(dataListFilter?.get(position), position)
        })
        holder.btnComplete.setOnClickListener(View.OnClickListener {
            if(dataListFilter?.get(position)?.status==baseFragment.resources.getString(R.string.v2_lead_status_submitted_to_bank)||
                    dataListFilter?.get(position)?.status==ScreenTypeEnum.Disbursed.value||
                    dataListFilter?.get(position)?.status==ScreenTypeEnum.Approved.value||
                    dataListFilter?.get(position)?.status==ApplicationStatusEnum.Rejected.value||
                    dataListFilter?.get(position)?.status==ApplicationStatusEnum.Closed.value)
            {
                itemCallBack.onItemClick(dataListFilter?.get(position), position)
            }else
                itemCallBack.onCompleteClick(dataListFilter?.get(position), position)

        })

        if (dataListFilter?.get(position)?.bankLogoURL != null) {
            Picasso.get()
                .load(dataListFilter?.get(position)?.bankLogoURL)
                .into(holder.tvBankIcon, object : Callback {
                    override fun onSuccess() {
                        holder.tvBankIcon.visibility = View.VISIBLE

                    }

                    override fun onError(ex: Exception) {
                        holder.tvBankIcon.visibility = View.GONE

                    }
                })
        }

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
        return dataListFilter?.size!!
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                dataListFilter = if (charSearch.isEmpty()) {
                    dataListValue as ArrayList<ApplicationDataItems>
                } else {
                    val resultList = ArrayList<ApplicationDataItems>()
                    for (row in dataListValue) {
                        if (row.customerId.toString().toLowerCase()
                                .contains(constraint.toString().toLowerCase()) ||
                            row.firstName.toString().toLowerCase()
                                .contains(constraint.toString().toLowerCase()) ||
                            row.customerMobile.toString().toLowerCase()
                                .contains(constraint.toString().toLowerCase()) ||
                            row.bankName.toString().toLowerCase()
                                .contains(constraint.toString().toLowerCase()) ||
                            row.losId.toString().toLowerCase()
                                .contains(constraint.toString().toLowerCase()) ||
                            row.caseId.toString().toLowerCase()
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
                dataListFilter = results?.values as ArrayList<ApplicationDataItems>
                notifyDataSetChanged()
            }
        }
    }


}