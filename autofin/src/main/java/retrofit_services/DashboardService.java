package retrofit_services;

import android.content.Context;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import model.CustomerDetailsRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit_config.RetroBase;

import static retrofit_config.RetroBase.retrofit;

public class DashboardService extends RetroBase
{

    public static void fetchDashBoardInfo(Context mContext, final RetrofitInterface mHttpCallResponse) {
        DashBoardInterface mInterface = retrofit.create(DashBoardInterface.class);

        Call<CustomerDetailsRes> mCall = mInterface.getDashBoardInfo();
        mCall.enqueue(new Callback<CustomerDetailsRes>() {
            @Override
            public void onResponse(@NotNull Call<CustomerDetailsRes> call, @NotNull Response<CustomerDetailsRes> response) {

                Log.i("HomeFragment", "URL: "+mCall.request().url().toString());

                if (response.isSuccessful()) {
                    mHttpCallResponse.OnSuccess(response);
                }
            }

            @Override
            public void onFailure(@NotNull Call<CustomerDetailsRes> call, @NotNull Throwable t) {
                mHttpCallResponse.OnFailure(t);
            }
        });
    }

    public interface DashBoardInterface {
        @Headers("Content-Type: application/json; charset=utf-8")
        @GET("/employee-type")
        Call<CustomerDetailsRes> getDashBoardInfo();
    }
}
