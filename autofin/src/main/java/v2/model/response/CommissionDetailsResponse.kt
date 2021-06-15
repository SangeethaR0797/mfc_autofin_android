package v2.model.response

data class CommissionDetailsResponse(
    var `data`: CommissionDetails?,
    var message: Any?,
    var status: Boolean?,
    var statusCode: String?
)

