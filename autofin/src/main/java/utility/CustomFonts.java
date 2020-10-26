package utility;

import android.content.Context;
import android.graphics.Typeface;

public class CustomFonts
{
    public static Typeface getRobotoMedium(Context context)
    {
        Typeface robotoMediumTF = Typeface.createFromAsset(context.getAssets(), "roboto_medium.ttf");
        return robotoMediumTF;
    }

    public static Typeface getRobotoRegularTF(Context context) {
        Typeface robotoRegularTF = Typeface.createFromAsset(context.getAssets(), "roboto_regular.ttf");
        return robotoRegularTF;
    }
}
