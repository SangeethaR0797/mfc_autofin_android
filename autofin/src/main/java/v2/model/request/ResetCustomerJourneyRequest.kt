package v2.model.request

data class ResetCustomerJourneyRequest(
        var Data: ResetCustomerJourneyDataRequest?,
        var UserId: String?,
        var UserType: String?
)

data class ResetCustomerJourneyDataRequest(
        var CustomerId: String?
)