package pl.zazakretem.magisterka;

import android.hardware.SensorManager;

public class RotationValue extends AbstractValue {
    private Value azimuth = new Value();
    private Value pitch = new Value();
    private Value roll = new Value();

    private float[] geomagneticValues = new float[3];
    private float[] gravityValues = new float[3];

    public float getAzimuth()
    {
        return (float)Math.toDegrees(this.azimuth.getAveraged());
    }

    public float getPitch()
    {
        return (float)Math.toDegrees(this.pitch.getAveraged());
    }

    public float getRoll()
    {
        return (float)Math.toDegrees(this.roll.getAveraged());
    }

    public RotationValue setGeomagneticValues(float[] geomagneticValues)
    {
        this.geomagneticValues = geomagneticValues;
        this.calculate();
        return this;
    }

    public RotationValue setGravityValues(float[] gravityValues)
    {
        this.gravityValues = gravityValues;
        this.calculate();
        return this;
    }

    private void calculate()
    {
        float[] Rm = new float[9];
        SensorManager.getRotationMatrix(Rm, null, gravityValues, geomagneticValues);

        float[] results = new float[3];
        results = SensorManager.getOrientation(Rm, results);
        this.azimuth.add(results[0]);
        this.pitch.add(results[1]);
        this.roll.add(results[2]);
    }

}
