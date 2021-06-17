package v2.end_point

import io.reactivex.Observable
import retrofit2.http.*
import v2.model.dto.DashBoardDetailsRequest
import v2.model.dto.DashBoardDetailsResponse
import v2.model.request.CommonRequest
import v2.model.response.CommissionDetailsResponse

import v2.model.response.RuleEngineBanksResponse
import v2.model.response.master.MasterResponse

interface DashboardEndPoint {
    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun getDashboardDetails(
        @Body request: DashBoardDetailsRequest,
        @Url url: String?
    ): Observable<DashBoardDetailsResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET
    fun getRuleEngineBanks(@Url url: String?): Observable<RuleEngineBanksResponse?>?


    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun getDealerCommission(
        @Body request: CommonRequest,
        @Url url: String?
    ): Observable<CommissionDetailsResponse?>?

}