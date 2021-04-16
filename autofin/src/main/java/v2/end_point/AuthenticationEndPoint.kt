package v2.end_point

import io.reactivex.Observable
import model.token.GetTokenReq
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url
import v2.model.TokenDetails

interface AuthenticationEndPoint {
    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun getToken(@Body request: GetTokenReq?, @Url url: String?): Observable<TokenDetails?>?

}