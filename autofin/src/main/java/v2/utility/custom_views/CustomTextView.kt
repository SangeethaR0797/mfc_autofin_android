package v2.utility.custom_views

import android.content.Context
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView

public class CustomTextView(context: Context) : AppCompatTextView(context)
{

    override fun setTextSize(size: Float) {
        super.setTextSize(size)
    }
    override fun setTextColor(color: Int) {
        super.setTextColor(color)
    }
}