package v2.model.request.bank_offers

data class BankOffersForApplicationRequest(
    var Data: LeadApplicationData?,
    var UserId: String?,
    var UserType: String?
)

data class LeadApplicationData(
    var CaseId: String?,
    var CustomerId: String?,
    var LoanAmount: Int?,
    var ReqTenure: Int?
)