package v2.model.dto

import v2.model.response.CommissionDetails
import v2.model.response.NoticeData

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
    var softOffer: Int?,
    var approvedAmount: String?,
    var disbursedAmount: String?
)



data class NoticeBoard(
    var newCount: Int?,
    var notices: List<NoticeData>?,
    var totalCount: Int?
)





