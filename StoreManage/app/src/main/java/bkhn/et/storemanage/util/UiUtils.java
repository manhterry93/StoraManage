package bkhn.et.storemanage.util;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntegerRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by PL_itto on 2/14/2018.
 */

public class UiUtils {
    public static void loadImageLink(Context context, String link, ImageView view, @DrawableRes int resId, boolean circle) {
        RequestOptions requestOptions;
        if (circle)
            requestOptions = RequestOptions.circleCropTransform();
        else
            requestOptions = new RequestOptions();
        requestOptions.placeholder(resId);
        Glide.with(context).load(link).apply(requestOptions).into(view);
    }
}
