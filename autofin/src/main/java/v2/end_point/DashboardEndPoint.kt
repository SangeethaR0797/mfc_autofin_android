package v2.end_point

import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url
import v2.model.dto.DashBoardDetailsRequest
import v2.model.dto.DashBoardDetailsResponse

interface DashboardEndPoint {
    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun getDashboardDetails(@Body request: DashBoardDetailsRequest, @Url url: String?): Observable<DashBoardDetailsResponse?>?

}