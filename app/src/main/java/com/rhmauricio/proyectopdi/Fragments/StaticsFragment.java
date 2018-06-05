package com.rhmauricio.proyectopdi.Fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rhmauricio.proyectopdi.R;
import com.rhmauricio.proyectopdi.classes.DatosPublicidad;
import com.rhmauricio.proyectopdi.constants.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StaticsFragment extends Fragment {

    private ViewFlipper mViewFlipper;

    private Context mContext;

    private CombinedChart mChartCine;

    private CombinedChart mChartColombina;

    private CombinedChart mChartCrepes;

    private CombinedChart mChartEpm;

    private CombinedChart mChartSura;

    private Map<String, List<DatosPublicidad>> brandsData;

    private float initialX;

    private int brandsIndex;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbReference = database.getReference("Publicidad");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statics, container, false);

        mContext = getContext();

        mChartCine = setChartProperties(view, R.id.chart_cine);
        mChartColombina = setChartProperties(view, R.id.chart_colombina);
        mChartCrepes = setChartProperties(view, R.id.chart_crepes);
        mChartEpm = setChartProperties(view, R.id.chart_epm);
        mChartSura = setChartProperties(view, R.id.chart_sura);

        mViewFlipper = view.findViewById(R.id.view_flipper);

        mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left));
        mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.slide_out_right));

        mViewFlipper.setOnTouchListener(touchListener);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        brandsIndex = 0;

        dbReference.addValueEventListener(DBListener);

        brandsData = new HashMap<>();
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initialX = event.getX();
                    return true;
                case MotionEvent.ACTION_UP:
                    float finalX = event.getX();
                    if (initialX > finalX) {
                        mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.in_from_right));
                        mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.out_from_left));
                        mViewFlipper.showNext();
                    } else {
                        mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.in_from_left));
                        mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.out_from_right));
                        mViewFlipper.showPrevious();
                    }
                    return true;
            }
            return false;
        }
    };

    private CombinedChart setChartProperties(View view, int id) {
        CombinedChart mChart = view.findViewById(id);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(true);
        mChart.setDrawBarShadow(true);
        mChart.setHighlightFullBarEnabled(true);

        // draw bars behind lines
        mChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR,  CombinedChart.DrawOrder.LINE
        });

        Legend l = mChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mEmotions[(int) value % mEmotions.length];
            }
        });

        return mChart;
    }

    private BarData generateBarData(List<DatosPublicidad> brandData) {

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries = getBarEntries(entries, brandData);

        BarDataSet set1 = new BarDataSet(entries, "Bar");
        //set1.setColor(Color.rgb(60, 220, 78));
        set1.setColors(ColorTemplate.COLORFUL_COLORS);
        set1.setValueTextColor(Color.rgb(60, 220, 78));
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        float barWidth = 0.45f; // x2 dataset

        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);

        return d;
    }

    private ArrayList<BarEntry> getBarEntries(ArrayList<BarEntry> entries, List<DatosPublicidad> brandData){
        double felicidad = 0;
        double tristeza = 0;
        double calma = 0;
        double sorpresa = 0;
        double enojo = 0;
        double confusion = 0;

        int size = brandData.size();

        for(DatosPublicidad singleData: brandData) {
            felicidad += singleData.getFelicidad();
            tristeza += singleData.getTristeza();
            calma += singleData.getCalma();
            sorpresa += singleData.getSorpresa();
            enojo += singleData.getEnojo();
            confusion += singleData.getConfusion();
        }

        entries.add(new BarEntry(1, (int)felicidad / size));
        entries.add(new BarEntry(2, (int)calma / size));
        entries.add(new BarEntry(3, (int)tristeza / size));
        entries.add(new BarEntry(4, (int)enojo / size));
        entries.add(new BarEntry(5, (int)sorpresa / size));
        entries.add(new BarEntry(6, (int)confusion / size));
        return  entries;
    }

    private String[] mEmotions = new String[] {
            "Felicidad", "Calma", "Tristeza", "Enojo", "Sorpresa", "Confusión"
    };

    ValueEventListener DBListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.hasChildren()){
                int index = 0;
                for (DataSnapshot children:dataSnapshot.getChildren()) {
                    List<DatosPublicidad> dataList = new ArrayList<>();
                    for (DataSnapshot child: children.getChildren()) {
                        dataList.add(child.getValue(DatosPublicidad.class));
                    }

                    brandsData.put(Constants.BRANDS[index], dataList);

                    index++;
                }
            }



        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    private void setBarData(List) {
        CombinedData data = new CombinedData();

        data.setData(generateBarData());

        xAxis.setAxisMaximum(data.getXMax() + 0.25f);
        mChart.setData(data);
        mChart.invalidate();
    }

}
