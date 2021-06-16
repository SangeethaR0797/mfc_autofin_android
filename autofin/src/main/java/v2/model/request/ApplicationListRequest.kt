package v2.model.request

data class ApplicationListRequest(
    var Data: Data?,
    var UserId: String?,
    var UserType: String?
)

data class Data(
    var Key: String?,
    var PageNo: Int?,
    var PerPage: Int?,
    var Status: String?
)