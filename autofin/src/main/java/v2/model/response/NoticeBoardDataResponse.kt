package v2.model.response

data class NoticeBoardDataResponse(
    var `data`: Data?,
    var message: Any?,
    var status: Boolean?,
    var statusCode: String?
)

data class Data(
    var newCount: Int?,
    var notices: List<NoticeData>?,
    var totalCount: Int?
)

