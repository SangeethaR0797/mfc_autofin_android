package v2.service.utility;

import android.util.Log;

import com.avinash.kotlinmvvmandcleanarchitecture.util.APIError;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.HttpException;
import retrofit2.Response;
import v2.service.ApiServiceGenerator;

public class ErrorUtils {
    private final static String TAG = "ErrorUtils";


    private static APIError parseError(Response<?> response) {
        Converter<ResponseBody, APIError> converter =
                ApiServiceGenerator.getRetrofitInstance()
                        .responseBodyConverter(APIError.class, new Annotation[0]);

        APIError error;

        try {
            error = converter.convert(response.errorBody());
            error.setStatusCode(response.code());
        } catch (IOException e) {
            return new APIError();
        }
        return error;
    }

//    public static APIError parseError(Response<?> response) {
//
//        APIError error = new APIError();
//        ResponseBody responseBody = response.errorBody();
//        try {
//
//            JSONObject obj = new JSONObject(responseBody.string());
//            error.setMessage(obj.toString());
//            error.setStatusCode(response.code());
//        } catch (JSONException je) {
//            try {
//                JSONArray jsonArray = new JSONArray(responseBody.string());
//                error.setMessage(jsonArray.toString());
//                error.setStatusCode(response.code());
//            } catch (Exception e) {
//                Log.e(TAG, e.toString());
//            }
//        } catch (Exception e) {
//
//            Log.e("ErrorUtilty", e.toString());
//        }
//
//        return error;
//    }

    public static APIError parseThrowable(Throwable t) {
        APIError apiError = new APIError();
        Gson gson = new Gson();
        try {
            if (t instanceof HttpException) {
//                ResponseBody responseBody = ((HttpException) t).response().errorBody();
                apiError = parseError(((HttpException) t).response());
            } else if (t instanceof retrofit2.HttpException) {
                apiError = parseError(((retrofit2.HttpException) t).response());
            } else {
                new APIError();
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return apiError;
    }

}
