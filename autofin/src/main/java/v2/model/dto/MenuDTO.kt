package v2.model.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class MenuDTO(
    public var menuName: String? = null,
    public var menuCode: String? = null,
    public var amount: String? = null,
    public var total: Int,
    public var menuIcon: Int,
    public var backgroundResource: Int

) : Parcelable