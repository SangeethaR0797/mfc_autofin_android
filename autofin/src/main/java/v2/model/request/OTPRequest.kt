package v2.model.request

data class OTPRequest(
        var Data: OTPRequestData?=null,
        var UserId: String?=null,
        var UserType: String?=null
)

data class OTPRequestData(
        var CustomerMobile: String?=null,
        var OTP: String?=null
)