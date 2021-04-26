package v2.end_point

import io.reactivex.Observable
import retrofit2.http.*
import v2.model.response.master.MasterResponse

interface MasterEndPoint {
    @Headers("Content-Type: application/json; charset=utf-8")
    @GET
    fun getKmsDrivenDetails( @Url url: String?): Observable<MasterResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET
    fun getSalutations( @Url url: String?): Observable<MasterResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET
    fun getResidentYears( @Url url: String?): Observable<MasterResponse?>?
}