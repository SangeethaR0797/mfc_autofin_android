package utility;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class RobotoMedium extends AppCompatTextView {

    public RobotoMedium(@NonNull Context context) {
        super(context);
    }

    public RobotoMedium(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RobotoMedium(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "roboto_medium.ttf");
        setTypeface(tf ,Typeface.NORMAL);
    }
}
