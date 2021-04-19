package v2.repository

import io.reactivex.Observable
import v2.end_point.AuthenticationEndPoint
import v2.end_point.IBB_MasterDetailsEndPoint
import v2.model.request.GetTokenDetailsRequest
import v2.model.request.Get_IBB_MasterDetailsRequest
import v2.model.request.Get_IBB_TokenRequest
import v2.model.response.Get_IBB_MasterDetailsResponse
import v2.model.response.IBB_TokenResponse
import v2.model.response.TokenDetailsResponse
import v2.service.ApiServiceGenerator

class IBB_MasterDetailsRepository {
    lateinit var masterDetailsEndPoint: IBB_MasterDetailsEndPoint

    init {
        masterDetailsEndPoint = ApiServiceGenerator.createService<IBB_MasterDetailsEndPoint>(
                IBB_MasterDetailsEndPoint::class.java
        )
    }

    fun getToken(request: Get_IBB_MasterDetailsRequest, url: String): Observable<Get_IBB_MasterDetailsResponse?>? {
        return masterDetailsEndPoint.getIBB_MasterDetails(request, url)
    }


}