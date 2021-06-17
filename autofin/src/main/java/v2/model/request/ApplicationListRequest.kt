package v2.model.request

data class ApplicationListRequest(
    var Data: ApplicationListRequestData?,
    var UserId: String?,
    var UserType: String?
)

data class ApplicationListRequestData(
    var Key: String?,
    var Status: String?,
    var BankName: String?,
    var PageNo: Int?,
    var PerPage: Int?

)