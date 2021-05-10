package v2.model.request.update

data class UpdateLeadBasicDetailsRequest(
    var Data: LeadUpdateData?,
    var UserId: String?,
    var UserType: String?
)

data class LeadUpdateData(
    var CustomerId: Int?,
    var basicDetails: LeadBasicBasicDetails?
)

data class LeadBasicBasicDetails(
    var Email: String?,
    var FirstName: String?,
    var LastName: String?,
    var Salutation: String?
)