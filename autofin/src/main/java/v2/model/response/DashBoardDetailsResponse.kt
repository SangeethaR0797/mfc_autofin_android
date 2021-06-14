package v2.model.dto

data class DashBoardDetailsResponse(
    var `data`: Data?,
    var message: Any?,
    var status: Boolean?,
    var statusCode: String?
)

data class Data(
    var approved: Int?,
    var commissionDetails: CommissionDetails?,
    var disbursed: Int?,
    var loggedIn: Int?,
    var newNotificationCount: Int?,
    var noticeBoard: NoticeBoard?,
    var registered: Int?,
    var softOffer: Int?
)

data class CommissionDetails(
    var forDays: Int?,
    var potentialCommission: Int?,
    var totalCommission: Int?
)

data class NoticeBoard(
    var newCount: Int?,
    var notices: List<Notice>?,
    var totalCount: Int?
)

data class Notice(
    var bankId: String?,
    var caseId: String?,
    var customerId: Int?,
    var description: String?,
    var isNew: Boolean?,
    var messageKey: String?,
    var noticeBoardId: Int?,
    var text: String?
)



