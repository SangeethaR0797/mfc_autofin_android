package v2.model.response

data class ValidateLeadResponse(
        var data: ValidateLeadDataResponse?,
        var message: String?,
        var status: Boolean?,
        var statusCode: Any?
)
data class ValidateLeadDataResponse(
        var oldCustomerId: String? = null,
        var code: String? = null,
        var message: String? = null,
)