package v2.repository

import io.reactivex.Observable
import model.token.GetTokenReq
import retrofit2.http.Body
import retrofit2.http.Url
import v2.end_point.AuthenticationEndPoint
import v2.model.TokenDetails
import v2.service.ApiServiceGenerator

class AuthenticationRepository {
    lateinit var authenticationEndPoint: AuthenticationEndPoint
    init {
        authenticationEndPoint = ApiServiceGenerator.createService<AuthenticationEndPoint>(
                AuthenticationEndPoint::class.java
        )
    }

    fun getToken(request: GetTokenReq, url: String): Observable<TokenDetails?>? {
        return authenticationEndPoint.getToken(request, url)
    }


}