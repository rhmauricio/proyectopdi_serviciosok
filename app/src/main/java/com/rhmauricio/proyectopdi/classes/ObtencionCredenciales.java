package com.rhmauricio.proyectopdi.classes;

import android.content.Context;
import android.os.AsyncTask;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;

import java.lang.ref.WeakReference;

import static com.rhmauricio.proyectopdi.constants.Constants.COGNITO_IDENTITY_POOL;

public class ObtencionCredenciales extends AsyncTask<Void, Void, AmazonRekognitionClient> {

    public interface AsyncResponse{
        void processFinish(AmazonRekognitionClient amazonRekognitionClient);
    }

    private WeakReference<Context> context;
    private final AsyncResponse callBack;

    public ObtencionCredenciales(Context context, AsyncResponse callBack) {
        this.context = new WeakReference<>(context);
        this.callBack = callBack;
    }

    /**
     * Cancel background network operation if we do not have network connectivity.
     */
    /*
    @Override
    protected void onPreExecute() {
        if (callBack != null) {
            NetworkInfo networkInfo = mCallback.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected() ||
                    (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                            && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                // If no connectivity, cancel task and update Callback with null data.
                mCallback.updateFromDownload(null);
                cancel(true);
            }
        }
    }
    */

    @Override
    protected AmazonRekognitionClient doInBackground(Void ...voids) {
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context.get(),
                COGNITO_IDENTITY_POOL,
                Regions.US_EAST_2
        );

        return new AmazonRekognitionClient(credentialsProvider);

    }

    @Override
    protected void onPostExecute(AmazonRekognitionClient client) {
        if (callBack != null) {
            callBack.processFinish(client);
        }
    }
}
