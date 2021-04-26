package v2.model.request

data class ValidateLeadRequest(
        var Data: ValidateLeadDataRequest?,
        var UserId: String?,
        var UserType: String?
)

data class ValidateLeadDataRequest(
        var CustomerMobile: String?,
        var Make: String?,
        var Model: String?,
        var Variant: String?
)