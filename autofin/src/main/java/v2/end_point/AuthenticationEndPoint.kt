package v2.end_point

import io.reactivex.Observable
import model.token.GetTokenReq
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url
import v2.model.request.GetTokenDetailsRequest
import v2.model.response.TokenDetailsResponse

interface AuthenticationEndPoint {
    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun getToken(@Body request: GetTokenDetailsRequest?, @Url url: String?): Observable<TokenDetailsResponse?>?

}