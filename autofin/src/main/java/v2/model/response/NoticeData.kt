package v2.model.response

data class NoticeData(
    var bankId: String?,
    var caseId: String?,
    var customerId: Int?,
    var description: String?,
    var isNew: Boolean?,
    var messageKey: String?,
    var noticeBoardId: Int?,
    var text: String?,
    var showMore: Boolean?=false
)