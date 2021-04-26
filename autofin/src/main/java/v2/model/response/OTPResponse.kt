package v2.model.response

data class OTPResponse(
        var data: String?,
        var message: String?,
        var status: Boolean?,
        var statusCode: Any?
)