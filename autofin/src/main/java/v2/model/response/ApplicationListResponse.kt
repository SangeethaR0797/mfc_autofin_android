package v2.model.response

data class ApplicationListResponse(
    var `data`: ApplicationData?,
    var message: String?,
    var status: Boolean?,
    var statusCode: String?
)

data class ApplicationData(
    var customers: List<ApplicationDataItems>?,
    var total: Int?
)

data class ApplicationDataItems(
    var bankName: String?,
    var caseId: String?,
    var createdDate: String?,
    var customerId: Int?,
    var customerMobile: String?,
    var emi: String?,
    var firstName: String?,
    var fuelType: String?,
    var kMs: String?,
    var lastName: String?,
    var loanAmount: String?,
    var losId: String?,
    var make: String?,
    var model: String?,
    var ownerShip: String?,
    var registrationYear: Int?,
    var roi: String?,
    var status: String?,
    var subStatus: String?,
    var tenure: String?,
    var variant: String?,
    var vehiclePrice: String?,
    var bankLogoURL: String?
)