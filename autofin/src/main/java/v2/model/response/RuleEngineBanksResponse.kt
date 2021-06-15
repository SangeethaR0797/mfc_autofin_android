package v2.model.response

data class RuleEngineBanksResponse(
    var `data`: List<RuleEngineBankData>?,
    var message: String?,
    var status: Boolean?,
    var statusCode: String?
)

data class RuleEngineBankData(
    var bankName: String?,
    var id: Int?,
    var logoUrl: String?,
    var processingFee: Double?,
    var roi: Double?
)