package v2.model.response

import v2.model.response.master.Types

data class BankListResponse(
        var data: List<String>?,
        var message: String?,
        var status: Boolean?,
        var statusCode: Any?
)