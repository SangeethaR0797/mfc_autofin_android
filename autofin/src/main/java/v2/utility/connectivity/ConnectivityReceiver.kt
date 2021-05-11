package v2.utility.connectivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager


class ConnectivityReceiver : BroadcastReceiver() {

    companion object {
        var connectivityReceiverListener: ConnectivityReceiverListener? = null
        fun isConnected(mContext: Context): Boolean {
            var isConnected = false
            val cm = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            if (activeNetwork != null) {
                if (activeNetwork != null && activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                    isConnected = true
                } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                    isConnected = true
                }
            } else {
                isConnected = false
            }
            return isConnected
        }
    }

    override fun onReceive(context: Context, arg1: Intent) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        var isConnected = false
        if (activeNetwork != null) {
            if (activeNetwork != null && activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                isConnected = true
            } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                isConnected = true
            }
        } else {
            isConnected = false
        }
        if (connectivityReceiverListener != null) {
            connectivityReceiverListener!!.onNetworkConnectionChanged(isConnected)
        }
    }



}
