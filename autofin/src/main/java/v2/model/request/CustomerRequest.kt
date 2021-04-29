package v2.model.request

data class CustomerRequest(
        var Data: ResetCustomerJourneyDataRequest? = null,
        var UserId: String? = null,
        var UserType: String? = null
)

data class ResetCustomerJourneyDataRequest(
        var CustomerId: String? = null
)