package v2.repository

import io.reactivex.Observable
import v2.end_point.MasterEndPoint
import v2.model.response.master.MasterResponse
import v2.service.ApiServiceGenerator

class MasterRepository {
    lateinit var masterEndPoint: MasterEndPoint

    init {
        masterEndPoint = ApiServiceGenerator.createService<MasterEndPoint>(
                MasterEndPoint::class.java
        )
    }

    fun getKmsDrivenDetails(url: String): Observable<MasterResponse?>?
    {
        return masterEndPoint.getKmsDrivenDetails(url)
    }

}