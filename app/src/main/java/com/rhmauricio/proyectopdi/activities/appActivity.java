package com.rhmauricio.proyectopdi.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.FaceDetail;
import com.rhmauricio.proyectopdi.R;
import com.rhmauricio.proyectopdi.classes.DeteccionRostros;
import com.rhmauricio.proyectopdi.classes.ObtencionCredenciales;

import java.util.List;


public class appActivity extends AppCompatActivity  {

    ImageView imageView2;
    Button button3;

    private final static int REQUEST_IMAGE_CAPTURE = 1;

    private AmazonRekognitionClient rekognitionClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        button3 = findViewById(R.id.button3);
        imageView2 = findViewById(R.id.imageView2);

        ObtencionCredenciales credenciales = new ObtencionCredenciales(this, listenerCredenciales);
        credenciales.execute();

        //llamarIntent();
    }

    private void  llamarIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView2.setImageBitmap(imageBitmap);
        }
    }

    public void onTomarFotoClicked(View view) {
        DeteccionRostros deteccionRostros = new DeteccionRostros(this, rekognitionClient, listenerDeteccion);

        // Ejecuta el hilo de detección de rostro con la ruta de la imagen
        // TODO: cambiar nombre a imagen existente
        deteccionRostros.execute("girl_image.JPG");
    }

    ObtencionCredenciales.AsyncResponse listenerCredenciales = new ObtencionCredenciales.AsyncResponse() {
        @Override
        public void processFinish(AmazonRekognitionClient amazonRekognitionClient) {
            rekognitionClient = amazonRekognitionClient;
        }
    };

    DeteccionRostros.AsyncResponse listenerDeteccion = new DeteccionRostros.AsyncResponse() {
        @Override
        public void processFinish(List<FaceDetail> detallesRostro) {
            for (FaceDetail detalleRostro: detallesRostro) {
                Log.d("Emociones: ", detalleRostro.getEmotions().toString());
            }
        }
    };


}
