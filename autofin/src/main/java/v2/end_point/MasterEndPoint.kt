package v2.end_point

import io.reactivex.Observable
import retrofit2.http.*
import v2.model.response.master.KmsDrivenResponse

interface MasterEndPoint {
    @Headers("Content-Type: application/json; charset=utf-8")
    @GET
    fun getKmsDrivenDetails( @Url url: String?): Observable<KmsDrivenResponse?>?
}