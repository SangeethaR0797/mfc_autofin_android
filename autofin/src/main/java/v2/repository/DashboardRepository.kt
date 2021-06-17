package v2.repository

import io.reactivex.Observable
import v2.end_point.DashboardEndPoint
import v2.model.dto.DashBoardDetailsRequest
import v2.model.dto.DashBoardDetailsResponse
import v2.model.request.CommonRequest
import v2.model.response.CommissionDetailsResponse

import v2.model.response.RuleEngineBanksResponse
import v2.service.ApiServiceGenerator

class DashboardRepository {
    lateinit var dashboardEndPoint: DashboardEndPoint

    init {
        dashboardEndPoint = ApiServiceGenerator.createService<DashboardEndPoint>(
            DashboardEndPoint::class.java
        )
    }

    fun getDashboardDetails(
        request: DashBoardDetailsRequest,
        url: String?
    ): Observable<DashBoardDetailsResponse?>? {
        return dashboardEndPoint.getDashboardDetails(request, url)
    }

    fun getRuleEngineBanks(
        url: String?
    ): Observable<RuleEngineBanksResponse?>? {
        return dashboardEndPoint.getRuleEngineBanks(url)
    }

    fun getDealerCommission(
        request: CommonRequest,
        url: String?
    ): Observable<CommissionDetailsResponse?>? {
        return dashboardEndPoint.getDealerCommission(request, url)
    }



}