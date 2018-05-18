package com.rhmauricio.proyectopdi.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.FaceDetail;
import com.rhmauricio.proyectopdi.R;
import com.rhmauricio.proyectopdi.classes.DeteccionRostros;
import com.rhmauricio.proyectopdi.classes.ObtencionCredenciales;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmotionFragment extends Fragment {


    ImageView imageView2;
    Button button3;

    public EmotionFragment() {
        // Required empty public constructor
    }

    private AmazonRekognitionClient rekognitionClient;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_emotion, container, false);
        button3 = view.findViewById(R.id.button3);
        //imageView2 = view.findViewById(R.id.imageView2);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        ObtencionCredenciales credenciales = new ObtencionCredenciales(getContext(), listenerCredenciales);
        credenciales.execute();
    }

    public void onTomarFotoClicked(View view) {
        DeteccionRostros deteccionRostros = new DeteccionRostros(getContext(), rekognitionClient, listenerDeteccion);

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
