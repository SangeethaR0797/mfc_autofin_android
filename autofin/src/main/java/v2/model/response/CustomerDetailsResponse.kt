package v2.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import v2.model.response.master.EquifaxData

@Parcelize
data class CustomerDetailsResponse(
        var data: CustomerDetailsData?,
        var message: String?,
        var status: Boolean?,
        var statusCode: String?
) : Parcelable

@Parcelize
data class CustomerDetailsData(
        var additionalDetails: List<AdditionalDetailsData>,
        var documents: List<AdditionalDetailsData>,
        var basicDetails: BasicDetails?,
        var caseId: String?,
        var dealerDetails: DealerDetails?,
        var employmentDetails: EmploymentDetails?,
        var equifaxFields: EquifaxData?,
        var loanDetails: LoanDetails?,
        var residentialDetails: ResidentialDetails?,
        var status: String?,
        var statusDetails: List<StatusDetail>,
        var subStatus: String?,
        var vehicleDetails: VehicleDetails?
) : Parcelable

@Parcelize
data class BasicDetails(
        var birthDate: String?,
        var customerMobile: String?,
        var email: String?,
        var firstName: String?,
        var haveExistingEMI: Boolean?,
        var lastName: String?,
        var panNumber: String?,
        var salutation: String?,
        var totalEMI: Double?
) : Parcelable

@Parcelize
data class DealerDetails(
        var dealerCity: String?,
        var dealerCode: String?,
        var dealerEmail: String?,
        var dealerMobile: String?,
        var dealerName: String?,
        var dealerPinCode: String?,
        var externalRef: String?
) : Parcelable

@Parcelize
data class EmploymentDetails(
        var currentCompanyExpMoreThanOne: Boolean?,
        var employmentType: String?,
        var netAnualIncome: Double?,
        var primaryAccount: String?,
        var salaryAccount: String?,
        var totalWorkExperience: String?="0"
) : Parcelable

@Parcelize
data class LoanDetails(

        @SerializedName("loanCategory") var loanCategory : String,
        @SerializedName("bankName") var bankName : String,
        @SerializedName("loanAmount") var loanAmount : String,
        @SerializedName("emi") var emi : String,
        @SerializedName("roi") var roi : String,
        @SerializedName("tenure") var tenure : String,
        @SerializedName("loanApplicationNumber") var loanApplicationNumber : String,
        @SerializedName("bankLogoURL") var bankLogoURL : String,
        @SerializedName("processingFees") var processingFees : String


) : Parcelable

@Parcelize
data class ResidentialDetails(
        var currentAddress: CurrentAddress?,
        var customerCity: String?,
        var noOfYearInResident: Int?,
        var permanentAddress: PermanentAddress?,
        var residenceType: String?
) : Parcelable

@Parcelize
data class StatusDetail(
        var createdDate: String?,
        var description: String?,
        var status: String?
) : Parcelable

@Parcelize
data class VehicleDetails(
        var fuelType: String?,
        var kMs: String?,
        var make: String?,
        var model: String?,
        var ownership: Int?,
        var registrationYear: Int?,
        var variant: String?,
        var vehicleNumber: String?,
        var vehicleSellingPrice: Double?
) : Parcelable

@Parcelize
data class CurrentAddress(
        var addressLine1: String?,
        var addressLine2: String?,
        var addressLine3: String?,
        var city: String?,
        var isPermanent: Boolean?,
        var pincode: String?,
        var state: String?
) : Parcelable

@Parcelize
data class PermanentAddress(
        var addressLine1: String?,
        var addressLine2: String?,
        var addressLine3: String?,
        var city: String?,
        var pincode: String?,
        var state: String?
) : Parcelable

@Parcelize
data class AdditionalDetailsData(
        var apiKey: String?,
        var value: String,
        var DisplayLabel:String,
        var freeText : String
) : Parcelable