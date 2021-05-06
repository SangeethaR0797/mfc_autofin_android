package v2.model.response.bank_offers

data class GetBankOffersForApplicationResponse(
    var data: List<BankOffersData>?,
    var message: Any?,
    var status: Boolean?,
    var statusCode: String?
)

data class BankOffersData(
    var bankId: Int?,
    var bankName: String?,
    var emi: String?,
    var isRecommended: Boolean?,
    var loanAmount: String?,
    var message: Any?,
    var roi: String?,
    var tenure: String?
)