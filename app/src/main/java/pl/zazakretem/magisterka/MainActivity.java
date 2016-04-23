package pl.zazakretem.magisterka;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SensorEventListener {

    private enum SelectedSensor {
        ACCELEROMETER,
        ROTATION,
        GPS_SPEED,
    }

    private SelectedSensor selectedSensor = SelectedSensor.ACCELEROMETER;

    private SensorManager sensorManager;

    private Sensor linearAccelerometer;
    private Sensor accelerometer;
    private Sensor magnetometer;


    private LocationManager locationManager;

    private RotationValue rotationValue = new RotationValue();
    private AccelerometerValue accelerometerValue = new AccelerometerValue();
    private SpeedValue speedValue = new SpeedValue();

    private float maxXValue = 0;
    private float maxYValue = 0;
    private float maxZValue = 0;


    private TextView xValueTextView;
    private TextView yValueTextView;
    private TextView zValueTextView;
    private TextView maxXValueTextView;
    private TextView maxYValueTextView;
    private TextView maxZValueTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        linearAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(this, linearAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, new SpeedListener());
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        xValueTextView = (TextView) findViewById(R.id.xValueTextView);
        yValueTextView = (TextView) findViewById(R.id.yValueTextView);
        zValueTextView = (TextView) findViewById(R.id.zValueTextView);
        maxXValueTextView = (TextView) findViewById(R.id.maxXValueTextView);
        maxYValueTextView = (TextView) findViewById(R.id.maxYValueTextView);
        maxZValueTextView = (TextView) findViewById(R.id.maxZValueTextView);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_accelerometer) {
            this.clearValues();
            this.selectedSensor = SelectedSensor.ACCELEROMETER;
        } else if (id == R.id.nav_rotation) {
            this.clearValues();
            this.selectedSensor = SelectedSensor.ROTATION;
        } else if (id == R.id.nav_gps) {
            this.clearValues();
            this.selectedSensor = SelectedSensor.GPS_SPEED;
        } else if (id == R.id.nav_location) {

        } else if (id == R.id.nav_ssm) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void clearValues()
    {
        this.maxXValue = this.maxYValue = this.maxZValue = 0;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        float xValue = 0, yValue = 0, zValue = 0;

        if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION && this.selectedSensor == SelectedSensor.ACCELEROMETER) {
            accelerometerValue.setAccelerometerValues(sensorEvent.values);

            xValue = accelerometerValue.getGForceX();
            yValue = accelerometerValue.getGForceY();
            zValue = accelerometerValue.getGForceZ();
        } else if(this.selectedSensor == SelectedSensor.ROTATION) {
            if(sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                rotationValue.setGravityValues(sensorEvent.values);
            }
            if(sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                rotationValue.setGeomagneticValues(sensorEvent.values);
            }
            xValue = rotationValue.getAzimuth();
            yValue = rotationValue.getPitch();
            zValue = rotationValue.getRoll();
        } else if(this.selectedSensor == SelectedSensor.GPS_SPEED) {
            xValue = speedValue.getSpeed();
        }

        if(xValue > maxXValue) {
            maxXValue = xValue;
        }
        if(yValue > maxYValue) {
            maxYValue = yValue;
        }
        if(zValue > maxZValue) {
            maxZValue = zValue;
        }

        xValueTextView.setText(String.valueOf(xValue));
        yValueTextView.setText(String.valueOf(yValue));
        zValueTextView.setText(String.valueOf(zValue));

        maxXValueTextView.setText(String.valueOf(maxXValue));
        maxYValueTextView.setText(String.valueOf(maxYValue));
        maxZValueTextView.setText(String.valueOf(maxZValue));
    }

    private class SpeedListener implements LocationListener
    {
        @Override
        public void onLocationChanged(Location location) {
            Log.d("KITTEN", "Location changed");
            speedValue.setSpeedValue(location.getSpeed());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("KITTEN", "Status changed");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("KITTEN", "Enabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("KITTEN", "Disabled");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, linearAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, linearAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
