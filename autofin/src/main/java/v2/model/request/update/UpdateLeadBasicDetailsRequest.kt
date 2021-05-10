package v2.model.request.update

data class UpdateLeadBasicDetailsRequest(
    var Data: LeadUpdateData?=null,
    var UserId: String?=null,
    var UserType: String?=null
)

data class LeadUpdateData(
    var CustomerId: Int?=null,
    var basicDetails: LeadBasicBasicDetails?=null
)

data class LeadBasicBasicDetails(
    var Email: String?=null,
    var FirstName: String?=null,
    var LastName: String?=null,
    var Salutation: String?=null
)