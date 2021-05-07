package v2.model.request.bank_offers

data class SelectRecommendedBankOfferRequest(
    var Data: BankOfferData?,
    var UserId: String?,
    var UserType: String?
)

data class BankOfferData(
    var CaseId: String?,
    var CustomerId: String?,
    var RecommendedBankId: String?
)