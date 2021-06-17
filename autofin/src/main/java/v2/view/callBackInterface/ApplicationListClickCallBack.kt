package v2.view.callBackInterface

interface ApplicationListClickCallBack {
    fun onItemClick(item: Any?, position: Int)
    fun onCompleteClick(item: Any?, position: Int)
    fun onCallClick(item: Any?, position: Int)
}