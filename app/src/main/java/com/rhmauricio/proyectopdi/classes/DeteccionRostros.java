package com.rhmauricio.proyectopdi.classes;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.Attribute;
import com.amazonaws.services.rekognition.model.DetectFacesRequest;
import com.amazonaws.services.rekognition.model.DetectFacesResult;
import com.amazonaws.services.rekognition.model.FaceDetail;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.List;

public class DeteccionRostros extends AsyncTask<String, Void, List<FaceDetail>> {

    public interface AsyncResponse{
        void processFinish(List<FaceDetail> detallesRostro);
    }

    private WeakReference<Context> context;
    private AmazonRekognitionClient rekognitionClient;
    private final AsyncResponse callBack;

    public DeteccionRostros(Context context, AmazonRekognitionClient rekognitionClient, AsyncResponse callBack) {
        this.context = new WeakReference<>(context);
        this.callBack = callBack;
        this.rekognitionClient = rekognitionClient;
    }

    @Override
    protected List<FaceDetail> doInBackground(String... params) {
        Image image = new Image();

        try {
            //Log.d("Absolute path: ", file.getAbsolutePath());
            //TODO: modificar por imagen tomada en app
            //File file = new File(Environment.getExternalStorageDirectory()+"/"+ "foto_face.jpg");
            File file = new File(Environment.getExternalStorageDirectory()+"/"+ "foto_face.jpg");
            InputStream inputStream = new FileInputStream(file);
            ByteBuffer imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
            //ByteBuffer imageBytes = params[0];
            image.withBytes(imageBytes);

        } catch (Exception e) {
            //e.printStackTrace();
            Log.e("Error: ", "IO Exception");
        }

        String bucket = "S3bucket";

        DetectFacesRequest facesRequest = new DetectFacesRequest()
                .withImage(image)
                .withAttributes(Attribute.ALL.toString());

        DetectFacesResult facesResult;
        try {
            facesResult = rekognitionClient.detectFaces(facesRequest);
        } catch (AmazonServiceException e) {
            e.printStackTrace();
            return null;
        }

        return facesResult.getFaceDetails();
    }

    @Override
    protected void onPostExecute(List<FaceDetail> faceDetails) {
        if (callBack != null) {
            callBack.processFinish(faceDetails);
        }
    }
}
