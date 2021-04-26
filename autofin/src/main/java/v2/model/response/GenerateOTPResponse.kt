package v2.model.response

data class GenerateOTPResponse(
        var data: String?,
        var message: String?,
        var status: Boolean?,
        var statusCode: Any?
)