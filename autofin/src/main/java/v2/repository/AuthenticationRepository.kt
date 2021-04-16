package v2.repository

import io.reactivex.Observable
import model.token.GetTokenReq
import v2.end_point.AuthenticationEndPoint
import v2.model.request.GetTokenDetailsRequest
import v2.model.response.TokenDetailsResponse
import v2.service.ApiServiceGenerator

class AuthenticationRepository {
    lateinit var authenticationEndPoint: AuthenticationEndPoint
    init {
        authenticationEndPoint = ApiServiceGenerator.createService<AuthenticationEndPoint>(
                AuthenticationEndPoint::class.java
        )
    }

    fun getToken(request: GetTokenDetailsRequest, url: String): Observable<TokenDetailsResponse?>? {
        return authenticationEndPoint.getToken(request, url)
    }


}