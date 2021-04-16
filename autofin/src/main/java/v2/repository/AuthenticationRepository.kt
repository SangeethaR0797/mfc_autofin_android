package v2.repository

import io.reactivex.Observable
import model.ibb_models.IBBVehDetailsReq
import model.token.GetTokenReq
import v2.end_point.AuthenticationEndPoint
import v2.model.request.GetTokenDetailsRequest
import v2.model.request.Get_IBB_TokenRequest
import v2.model.response.IBB_TokenResponse
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

    fun getIBBToken(request: Get_IBB_TokenRequest, url: String): Observable<IBB_TokenResponse?>? {
        return authenticationEndPoint.getIBBToken(request, url)
    }


}