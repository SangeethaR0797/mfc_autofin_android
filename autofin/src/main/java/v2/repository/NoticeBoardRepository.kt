package v2.repository

import io.reactivex.Observable
import v2.end_point.NoticeboardEndPoint
import v2.model.request.NoticeBoardRequest
import v2.model.response.CommonResponse
import v2.model.response.NoticeBoardDataResponse
import v2.service.ApiServiceGenerator

class NoticeBoardRepository {
    private lateinit var noticeboardEndPoint: NoticeboardEndPoint

    init {
        noticeboardEndPoint = ApiServiceGenerator.createService<NoticeboardEndPoint>(
            NoticeboardEndPoint::class.java
        )
    }

    fun noticeBoardAction(
        request: NoticeBoardRequest,
        url: String?
    ): Observable<CommonResponse?>? {
        return noticeboardEndPoint.noticeBoardAction(request, url)
    }

    fun getNoticeBoardData(
        request: NoticeBoardRequest,
        url: String?
    ): Observable<NoticeBoardDataResponse?>? {
        return noticeboardEndPoint.getNoticeBoardData(request, url)
    }


}