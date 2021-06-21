package v2.model.request

data class EmiRequest(
    var Data: Data?,
    var UserId: String?,
    var UserType: String?
)

data class Data(
    var LoanAmount: Int?,
    var ROI: Int?,
    var Tenure: Int?
)