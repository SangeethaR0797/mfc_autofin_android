package v2.model.request

data class EmiRequest(
    var Data: EmiRequestData?,
    var UserId: String?,
    var UserType: String?
)

data class EmiRequestData(
    var LoanAmount: Int?,
    var ROI: Int?,
    var Tenure: Int?
)