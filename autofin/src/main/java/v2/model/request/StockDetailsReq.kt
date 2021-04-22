package v2.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

public class StockDetailsReq
{
    @SerializedName("vehicleNumber")
    @Expose
    public var vehicleNumber: String? = ""

}