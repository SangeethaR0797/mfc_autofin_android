package v2.model.request

data class OTPRequest(
        var Data: Data?,
        var UserId: String?,
        var UserType: String?
)

data class Data(
        var CustomerMobile: String?,
        var OTP: String?
)