package com.example.v3rt1ag0.smartgridsystem.DataGraph;

import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.v3rt1ag0.smartgridsystem.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    LineChart chart;
    DatabaseReference database;
    List<GraphStructure> dataList = new ArrayList<>();
    String year,month;
    int flag = 0;
    private static final String CURRENT = "Current";
    private static final String VOLTAGE = "Voltage";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Calendar c = Calendar.getInstance();
        year = String.valueOf(c.get(Calendar.YEAR));
        month = getResources().getStringArray(R.array.months)[c.get(Calendar.MONTH)];



        chart = findViewById(R.id.chart);
        chart.setDrawGridBackground(false);
        chart.setDrawBorders(false);
        chart.setVisibleXRange(5,10);
        Description description = new Description();
        description.setText("DAY");
        chart.setDescription(description);
        //chart.getAxisLeft().setDrawGridLines(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.disableGridDashedLine();
        xAxis.setTextSize(8);
        xAxis.setDrawLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);

        chart.getAxisRight().setDrawLabels(false);
        initializeChart();
        setSpinner();

    }

    private void initializeChart()
    {
        database = FirebaseDatabase.getInstance().getReference();
        final List<Entry> voltageEntries = new ArrayList<>();
        final List<Entry> currentEntries = new ArrayList<>();

        database.child(year).child(month).child(CURRENT).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(!dataSnapshot.hasChildren())
                    return;
                for(DataSnapshot snap:dataSnapshot.getChildren()){
                    Log.d("TAG",snap.toString());
                    GraphStructure currentGraph = snap.getValue(GraphStructure.class);
                    currentEntries.add(new Entry(currentGraph.getDate(),currentGraph.getValue()));
                    Log.d("TAG1",currentGraph.getValue()+""+year + " " + month + " ");
                }
                flag++;
                if (flag == 2)
                    DisplayGraph(currentEntries,voltageEntries);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });


        database.child(year).child(month).child(VOLTAGE).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(!dataSnapshot.hasChildren())
                    return;
                for(DataSnapshot snap:dataSnapshot.getChildren()){
                    Log.d("TAG",snap.toString());
                    GraphStructure voltageGraph = snap.getValue(GraphStructure.class);
                    voltageEntries.add(new Entry(voltageGraph.getDate(),voltageGraph.getValue()));
                    Log.d("TAG",voltageGraph.getValue()+ "");


                }

                flag++;
                if(flag==2)
                    DisplayGraph(currentEntries,voltageEntries);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
        /*dataList.add(new GraphStructure(5,"24th"));
        dataList.add(new GraphStructure(6,"25th"));
        dataList.add(new GraphStructure(7,"26th"));
        dataList.add(new GraphStructure(9,"27th"));
        currentEntries.add(new Entry(5, 6));
        currentEntries.add(new Entry(6, 10));
        currentEntries.add(new Entry(7, 100));
        voltageEntries.add(new Entry(5, 20));
        voltageEntries.add(new Entry(6, 20));
        voltageEntries.add(new Entry(7, 20));*/



    }

    void DisplayGraph(List<Entry> currentEntries, List<Entry> voltageEntries){
        flag = 0;
        LineDataSet currentDataSet = new LineDataSet(currentEntries, "Current");
        currentDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        currentDataSet.setLineWidth(2);
        currentDataSet.setCircleRadius(4);
        currentDataSet.setValueTextSize(8);
        currentDataSet.setCircleColor(ContextCompat.getColor(this, R.color.red));
        currentDataSet.setColors(ContextCompat.getColor(this, R.color.red));


        LineDataSet voltageDataSet = new LineDataSet(voltageEntries, "Voltage");
        voltageDataSet.setColors(ContextCompat.getColor(this, R.color.blue));
        voltageDataSet.setCircleRadius(4);
        voltageDataSet.setCircleColor(ContextCompat.getColor(this, R.color.blue));
        voltageDataSet.setLineWidth(2);
        voltageDataSet.setValueTextSize(8);
        voltageDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);


        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(currentDataSet);
        dataSets.add(voltageDataSet);


        LineData lineData = new LineData(dataSets);
        chart.setData(lineData);
        chart.postInvalidate();


    }

    private void setSpinner()
    {
        final Spinner Yearspinner = findViewById(R.id.yearSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.years, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Yearspinner.setAdapter(adapter);
        Yearspinner.setSelection(Integer.parseInt(year)-2018);
        Yearspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                Resources res = getResources();
                String[] years = res.getStringArray(R.array.years);
                year = years[i];
                clearData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });


        final Spinner Monthspinner = findViewById(R.id.monthSpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.months, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Monthspinner.setAdapter(adapter2);
        Monthspinner.setSelection(Calendar.getInstance().get(Calendar.MONTH));

        Monthspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                Resources res = getResources();
                String[] months = res.getStringArray(R.array.months);
                month = months[i];
                clearData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    private void clearData()
    {
        chart.clear();
//        chart.clearValues();
        chart.postInvalidate();
        initializeChart();
    }
}
