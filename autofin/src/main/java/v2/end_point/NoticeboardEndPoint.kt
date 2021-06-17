package v2.end_point

import io.reactivex.Observable
import retrofit2.http.*
import v2.model.request.CommonRequest
import v2.model.response.CommonResponse
import v2.model.response.NoticeBoardDataResponse

interface NoticeboardEndPoint {
    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun noticeBoardAction(
        @Body request: CommonRequest,
        @Url url: String?
    ): Observable<CommonResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun getNoticeBoardData(
        @Body request: CommonRequest,
        @Url url: String?
    ): Observable<NoticeBoardDataResponse?>?
}