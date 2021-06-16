package v2.model.response

data class ApplicationListResponse(
    var `data`: ApplicationData?,
    var message: Any?,
    var status: Boolean?,
    var statusCode: String?
)

data class ApplicationData(
    var customers: List<Customer>?,
    var total: Int?
)

data class Customer(
    var bankName: Any?,
    var caseId: String?,
    var createdDate: String?,
    var customerId: Int?,
    var customerMobile: String?,
    var emi: Any?,
    var firstName: String?,
    var fuelType: String?,
    var kMs: String?,
    var lastName: String?,
    var loanAmount: Any?,
    var losId: Any?,
    var make: String?,
    var model: String?,
    var ownerShip: String?,
    var registrationYear: Int?,
    var roi: Any?,
    var status: String?,
    var subStatus: String?,
    var tenure: Any?,
    var variant: String?,
    var vehiclePrice: String?
)