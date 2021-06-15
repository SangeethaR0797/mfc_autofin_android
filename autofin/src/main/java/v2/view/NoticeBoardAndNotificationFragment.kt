package v2.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import com.mfc.autofin.framework.R
import v2.view.base.BaseFragment


class NoticeBoardAndNotificationFragment : BaseFragment() {

    lateinit var tvTitle: TextView
    lateinit var screenType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tvTitle = view.findViewById(R.id.tv_title)
        tvTitle.text = screenType
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.v2_fragment_noticeboard, container, false)
        arguments?.let {
            val safeArgs = NoticeBoardAndNotificationFragmentArgs.fromBundle(it)
            screenType = safeArgs.screenType
        }
        return view
    }


}







