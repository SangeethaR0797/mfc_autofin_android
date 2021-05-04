package v2.model.request

data class AddResidentDetailsRequest(
        var Data: ResidentDetailsData? = null,
        var UserId: String? = null,
        var UserType: String? = null
)

data class ResidentDetailsData(
        var CustomerId: Int? = null,
        var personalDetails: ResidentDetailsDataPersonalDetails? = null,
        var residentialDetails: ResidentDetailsDataResidentialDetails? = null
)

data class ResidentDetailsDataPersonalDetails(
        var HaveExistingEMI: Boolean? = null,
        var PANNumber: String? = null,
        var TotalEMI: Int? = null
)

data class ResidentDetailsDataResidentialDetails(
        var CustomerCity: String? = null,
        var NoOfYearInResident: Int? = -1,
        var ResidenceType: String? = null
)