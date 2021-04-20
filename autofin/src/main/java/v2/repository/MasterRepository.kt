package v2.repository

import io.reactivex.Observable
import v2.end_point.IBB_MasterDetailsEndPoint
import v2.end_point.MasterEndPoint
import v2.model.request.Get_IBB_MasterDetailsRequest
import v2.model.response.Get_IBB_MasterDetailsResponse
import v2.model.response.master.KmsDrivenResponse
import v2.service.ApiServiceGenerator

class MasterRepository {
    lateinit var masterEndPoint: MasterEndPoint

    init {
        masterEndPoint = ApiServiceGenerator.createService<MasterEndPoint>(
                MasterEndPoint::class.java
        )
    }

    fun getKmsDrivenDetails(url: String): Observable<KmsDrivenResponse?>? {
        return masterEndPoint.getKmsDrivenDetails(url)
    }


}