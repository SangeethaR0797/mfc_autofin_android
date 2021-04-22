package v2.repository

import io.reactivex.Observable
import v2.end_point.StockEndPoint
import v2.model.request.StockDetailsReq
import v2.model.response.StockResponse
import v2.service.ApiServiceGenerator

class StockRepository
{
    var stockEndPoint: StockEndPoint = ApiServiceGenerator.createService<StockEndPoint>(
            StockEndPoint::class.java)

    fun getStockDetailsRes(request:StockDetailsReq,url: String): Observable<StockResponse?>? {
        return stockEndPoint.getStockDetails(request,url)
    }


}