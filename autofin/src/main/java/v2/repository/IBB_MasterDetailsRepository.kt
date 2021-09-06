package v2.repository

import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url
import v2.end_point.IBB_MasterDetailsEndPoint
import v2.model.request.Get_IBB_MasterDetailsRequest
import v2.model.request.add_lead.IBBPriceRequest
import v2.model.response.Get_IBB_MasterDetailsResponse
import v2.model.response.master.IBBPriceResponse
import v2.service.ApiServiceGenerator

class IBB_MasterDetailsRepository {

    lateinit var masterDetailsEndPoint: IBB_MasterDetailsEndPoint

    init {
        masterDetailsEndPoint = ApiServiceGenerator.createService<IBB_MasterDetailsEndPoint>(
                IBB_MasterDetailsEndPoint::class.java
        )
    }

    fun getIBB_MasterDetails(request: Get_IBB_MasterDetailsRequest, url: String): Observable<Get_IBB_MasterDetailsResponse?>? {
        return masterDetailsEndPoint.getIBB_MasterDetails(request, url)
    }

    fun getIBBPrice(request: IBBPriceRequest, url: String): Observable<IBBPriceResponse>
    {
        return masterDetailsEndPoint.getIBBPrice(request,url)
    }

}