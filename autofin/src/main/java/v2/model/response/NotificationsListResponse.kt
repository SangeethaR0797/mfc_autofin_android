package v2.model.response

data class NotificationsListResponse(
    var `data`: NotificationsData?,
    var message: Any?,
    var status: Boolean?,
    var statusCode: String?
)

data class NotificationsData(
    var newCount: Int?,
    var notifications: List<NotificationItemData>?,
    var totalCount: Int?
)

data class NotificationItemData(
    var description: String?,
    var isNew: Boolean?,
    var showMore: Boolean?=false,
    var message: String?,
    var notificationId: Int?,
    var notificationKey: String?
)