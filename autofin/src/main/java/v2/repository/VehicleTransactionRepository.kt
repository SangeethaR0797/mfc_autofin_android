package v2.repository

import io.reactivex.Observable
import v2.end_point.VehicleTransactionEndPoint
import v2.model.dto.AddLeadRequest
import v2.model.response.AddLeadResponse
import v2.service.ApiServiceGenerator

class VehicleTransactionRepository {
    lateinit var vehicleTransactionEndPoint: VehicleTransactionEndPoint

    init {
        vehicleTransactionEndPoint = ApiServiceGenerator.createService<VehicleTransactionEndPoint>(
                VehicleTransactionEndPoint::class.java
        )
    }

    fun addLead(request: AddLeadRequest, url: String?): Observable<AddLeadResponse?>? {
        return vehicleTransactionEndPoint.addLead(request, url)
    }


}