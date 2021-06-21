package v2.model.response

data class EmiResponse(
    var `data`: Double?,
    var message: Any?,
    var status: Boolean?,
    var statusCode: String?
)