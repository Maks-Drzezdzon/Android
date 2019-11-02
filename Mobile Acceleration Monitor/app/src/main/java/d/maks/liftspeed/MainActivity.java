package d.maks.liftspeed;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;


//creating a sub class by extending and then using specific elements by using implements
public class MainActivity extends AppCompatActivity implements  SensorEventListener {

    //private static final String TAG = "MainActivity";// telling the chart where it is going to send the data to
    private LineChart mChart;// variable for linechart to specify that it is a linechart to the library
    private Thread thread; // needs to be set as a global variable for later use
    private boolean graphData = true; // conditional variable for later use
    private long isMoving = 0; // baseline value
    private float co_x,co_y,co_z; // assign variables to hold x,y,z values from accelerometer for testing purposes
    private static final int MoveThreshold = 600; // this value never changes and is the minimal value for motion to be registered later
    private float[] gravity = new  float[3]; // array to hold data
    private float[] linear_acceleration = new float[3]; // array to hold data
    // sensors from sensor manager
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Button pauseButton;
    private boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // instantiate object of sensor manager and accelerometer to be used by the application
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // get accelerometer sensor from sensor manager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        pauseButton = (Button) findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clicked) {
                    onPause();
                    pauseButton.setText("Resume");
                    clicked=true;
                }else if(clicked){
                    onResume();
                    pauseButton.setText("Pause");
                    clicked=false;
                }
            }
        });


        // register the accelerometer with the sensor manager
        if(sensorManager != null) {
            sensorManager.registerListener ( this, accelerometer, sensorManager.SENSOR_DELAY_NORMAL );
        }

        //**************************************************************************//
        // chart code
        // setting chart variables
        mChart = findViewById (R.id.chart);
        mChart.getDescription ().setEnabled ( true );

        // enabling dragging and disabling scale
        // disabling scale means that x and yAxis axis must be done separately
        mChart.setDragEnabled ( true );
        mChart.setScaleEnabled ( false );

        mChart.setPinchZoom ( true );

        LineData data = new LineData ( );
        data.setValueTextColor(Color.BLACK);

        // add data to set
        mChart.setData ( data );

        // creates a label from the mpandroidchart library referred to as the legend
        // this label is used for the data set passed to the graph and it is unique
        Legend label = mChart.getLegend();

        // the legend is the name given to the data set displayed at the bottom of the graph
        label.setForm(Legend.LegendForm.LINE);
        label.setTextColor(Color.BLUE);

        // creates an obj of the xAxis and assigns it to the mchart and calls getXAxis to it
        XAxis xAxis = mChart.getXAxis();
        // sets param for it such as color,being viable with drawgridlines and turning it on with setenabled
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(true);//will draw grid lines
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setEnabled(true);

        // creates an obj of the yAxis and assigns it to the mchart and calls getXAxis to it
        // data has AxisDependency.LEFT
        YAxis yAxis = mChart.getAxisLeft();
        // sets color of lines
        yAxis.setTextColor(Color.BLACK);
        yAxis.setDrawGridLines(false);
        // sets min and max values allowed for the yAxis to display on the graph
        yAxis.setAxisMaximum(10f);
        yAxis.setAxisMinimum(0f);//restricts the y axis from going all over the graph
        // enables display on graph
        yAxis.setDrawGridLines(true);//will draw grid lines

        // disables the getAxisRight as it is not used
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.setDrawBorders(false);

        // call the method to send data using a thread
        // if a thread is not used the app will crash
        feedData();
    }// end of onCreate


    @Override
    // method for detecting movement
    // when data is send from  sensor do this
    // store in var event
    public void onSensorChanged(SensorEvent event) {
        //since graphData is true the statement runs
        //calls the addEvent method and passes it the event data from SensorEvent
        //then sets it to false to break out of the statement
        if(graphData){ // if true
            addEvent (event); //pass event data to addEvent
            graphData = false; // set var to false breaking out of the loop
        }

        final float alpha = (float) 0.8;
        // gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        // remove  gravity from sensor
        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];

        // (modified acceleration method  - gravity) which gave me a negative value
        float x = event.values[0] - linear_acceleration[0];
        float y = event.values[1] - linear_acceleration[1];
        float z = event.values[2] - linear_acceleration[2];

        //sensor storing event from sensorEvent
        //this is used for filtering what sensor data i want to use
        Sensor sensorChanged = event.sensor;

        //text view to display values to screen
        TextView mainScreenTextView = findViewById(R.id.Pause);


        //using the accelerometer sensor
        //filtering what sensor data im looking for by TYPE_ACCELEROMETER

        if (sensorChanged.getType() == Sensor.TYPE_ACCELEROMETER){
            //returns system time
            //store system time
            long Time = System.currentTimeMillis();

            //checks if more than 100 ms passed since the sensor was evoked
            if((Time - isMoving) > 100){

                //stores currentTime in newTime - isMoving
                long newTime = (Time - isMoving);

                //stores currentTime in isMoving
                isMoving = Time;

                //calculates acceleration speed
                double GravitationalPull  = Math.sqrt(x*x+y*y+z*z);
                //the abs() method in the Math class returns the absolute value of what is passed in
                //this equation was used before to remove the effect of gravity from the original equation
                //i ended up not doing that because my answer want as accurate as the google method
                //calculates difference in gravity
                float speed = Math.abs(x + y +z - co_x - co_y - co_z)/newTime * 10000;
                //if value of speed is below 600 do nothing
                // this is done because the sensors are very sensitive and even when i place the phone on
                //a table the x and y axis still move slightly


                //this variable is unused i used it previously to take the force of gravity from the speed in a previous irritation of the application
                //double finalspeed = magnitude - speed;

                if (speed > MoveThreshold){

                }// end of if

                //storing x y z values in variables to be used later
                co_x = x;
                co_y = y;
                co_z = z;

                //uses the MPandroid library to call and set the min max values of the y axis for user feedback
                //the y axis is the one that is responsible for displaying the current speed
                float yMin = mChart.getYMin ();
                float yMax = mChart.getYMax ();

                mainScreenTextView.setText((String.format("\nGravitational pull is :  %.2f m/s2", GravitationalPull ))+""
                        +(String.format("\nCurrent speed is: %.2f m/s2", speed ))+""+(String.format ("\nMax speed %.2f m/s2 and Min speed %.2f m/s2 ",yMax,yMin)));
            }// end of time if

        }// end of sensor if

    }// end of sensor changed


    // method to initialize values for  graph
    private void addEvent(SensorEvent event) {

        LineData data = mChart.getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);

            //creates data set and adds data to the set
            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            //formula to printing the graph values
            //sets the location of the y axis
            data.addEntry(new Entry(set.getEntryCount(), event.values[0] + 5), 0);
            data.notifyDataChanged();//updates data on graph
            //updates chart data
            mChart.notifyDataSetChanged();
            //sets number of entries for consistency
            mChart.setVisibleXRangeMaximum(200);
            //uses the x axis as a static point
            mChart.moveViewToX(data.getEntryCount());

        }
    }

    //assigns data to a data set to be passed to the chart Library
    private LineDataSet createSet() {

        //this method is responsible for the data input to be created and changed as new data comes in from the sensors
        LineDataSet set = new LineDataSet(null, "");
        //sets the data stored in set to be showed on the Y axis
        // when t he speed changes this will cause the line to jump up or down on the y axis
        //the x axis keeps the data feed going there inst a change for the x axis on the graph
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        //line thickness
        set.setLineWidth(3f);
        //line color
        set.setColor(Color.BLUE);
        //disabling unneeded features
        set.setHighlightEnabled(false);
        set.setDrawValues(false);//displays position of each point
        set.setDrawCircles(false);//adds points between each point
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        //returns the LineDataSet
        return set;
    }

    public  void feedData() {

        //if the thread is not null  interrupt the thread
        if (thread != null){
            thread.interrupt();
        }

        //creates thread
        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true){
                    //plot data variable used to put the thread to sleep
                    graphData = true;
                    try {
                        Thread.sleep(10);
                        //error check
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });//end of thread

        //calls thread ref variable and then uses the start(); method from the O.S
        thread.start();
    }

    @Override
    //this  is an abstract method in the SensorEventListener , it must be implemented
    // but its not used in this application
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    // these functions dont work properly with the MPAndroid library and are disconnected from the UI
     // i wasnt able to find a reason why they stopped working once i started using the library
    @Override
    protected void onPause(){
        super.onPause();//calling parent constructor of method
        //unregisters all sensor listeners so they stop using app resources
        //if the thread is not null use the interrupt(); method on the thread variable
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();//calling parent constructor of method
        sensorManager.registerListener(this,accelerometer,sensorManager.SENSOR_DELAY_NORMAL);
    }


}// end of class

/******************Notes***********************************************************************************************************
 ***Use motion sensors***
 --read SensorManager class lib--
 https://developer.android.com/guide/topics/sensors/sensors_motion#java
 //way more accurate formula use//

 --read MPAndroid chart lib--
 //https://github.com/PhilJay/MPAndroidChart/wiki/Getting-Started
 --------------------------------------Plan how to--------------------------------------------------------------------------------------------
 sensorEvent stores data
 sensorEventListener is used by the app to fetch data encapsulated in a sensorEvent

 Monitor hardware event for changes in status(this requires registering a broadcastReceiver to
 listen and respond to the event and applying an intentFilter to the BroadcastReceiver so
 that it responds to a specific intent that you want/need)

 or

 Get info directly from a sensor(this requires a reference to the desired sensor (motion sensor) and then registering a
 sensorEventListener with the SensorManager that tells the sensor manger which listener to notify for each sensor)


 ---------------------------------------------------------------------------------------------------------------------------------------------
 is phone moving 0 or 1
 yes or no
 ------------------------------------------------------------------------------------------------------------------------------------------



 ---------------------------------------------------------------------------------------------------
 Acquire raw sensor data and define the minimum rate at which you acquire sensor data
 1)take in  value data
 2)get min max values
 3)graph data instead possibly ?
 /**********************************************************************************************
 **********************************************************************************************
 *
 * Requirements
 *
 * make an app  that will measure the speed of the lift in the main building on kevin street
 * using the app you should be able to:
 *
 * enter the lift on the ground floor
 * start the app 'buttons' pause/resume
 * app measures the speed continuously send to graph
 * use accelerometer to measure speed
 * comment code
 * write report
 * explain
 *
 *
 ***/
