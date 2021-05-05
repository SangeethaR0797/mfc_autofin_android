package v2.model.response

data class CommonBanksResponse(
        var data: List<BankData>?,
        var message: String?,
        var status: Boolean?,
        var statusCode: String?
)

data class BankData(
        var bankName: String?,
        var logoUrl: String?
)