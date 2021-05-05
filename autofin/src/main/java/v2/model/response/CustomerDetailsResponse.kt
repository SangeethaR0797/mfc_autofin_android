package v2.model.response

import v2.model.response.master.EquifaxData

data class CustomerDetailsResponse(
        var data: CustomerDetailsData?,
        var message: String?,
        var status: Boolean?,
        var statusCode: String?
)

data class CustomerDetailsData(
        var additionalDetails: List<Any>?,
        var basicDetails: BasicDetails?,
        var caseId: String?,
        var dealerDetails: DealerDetails?,
        var employmentDetails: EmploymentDetails?,
        var equifaxFields: EquifaxData?,
        var loanDetails: LoanDetails?,
        var residentialDetails: ResidentialDetails?,
        var status: String?,
        var statusDetails: List<StatusDetail>?,
        var subStatus: String?,
        var vehicleDetails: VehicleDetails?
)

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
)

data class DealerDetails(
        var dealerCity: String?,
        var dealerCode: String?,
        var dealerEmail: String?,
        var dealerMobile: String?,
        var dealerName: String?,
        var dealerPinCode: String?,
        var externalRef: String?
)

data class EmploymentDetails(
        var currentCompanyExpMoreThanOne: Boolean?,
        var employmentType: String?,
        var netAnualIncome: Double?,
        var primaryAccount: String?,
        var salaryAccount: String?,
        var totalWorkExperience: String?
)

data class LoanDetails(
        var bankName: String?,
        var emi: String?,
        var loanAmount: String?,
        var loanCategory: String?,
        var roi: String?,
        var tenure: String?
)

data class ResidentialDetails(
        var currentAddress: CurrentAddress?,
        var customerCity: String?,
        var noOfYearInResident: Int?,
        var permanentAddress: PermanentAddress?,
        var residenceType: String?
)

data class StatusDetail(
        var createdDate: String?,
        var description: String?,
        var status: String?
)

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
)

data class CurrentAddress(
        var addressLine1: String?,
        var addressLine2: String?,
        var addressLine3: String?,
        var city: String?,
        var isPermanent: Boolean?,
        var pincode: String?,
        var state: String?
)

data class PermanentAddress(
        var addressLine1: String?,
        var addressLine2: String?,
        var addressLine3: String?,
        var city: String?,
        var pincode: String?,
        var state: String?
)