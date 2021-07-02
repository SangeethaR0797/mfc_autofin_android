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
        var details: PersonalBasicDetails? = null,
        var status:String?=""
)

data class PersonalBasicDetails(
        var salutation: String? = null,
        var firstName: String? = null,
        var lastName: String? = null,
        var email: String? = null,
)