package v2.end_point

import io.reactivex.Observable
import retrofit2.http.*
import v2.model.request.NoticeBoardRequest
import v2.model.response.CommonResponse
import v2.model.response.NoticeBoardDataResponse

import v2.model.response.RuleEngineBanksResponse

interface NoticeboardEndPoint {
    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun noticeBoardAction(
        @Body request: NoticeBoardRequest,
        @Url url: String?
    ): Observable<CommonResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun getNoticeBoardData(
        @Body request: NoticeBoardRequest,
        @Url url: String?
    ): Observable<NoticeBoardDataResponse?>?
}