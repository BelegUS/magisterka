package pl.zazakretem.magisterka;

import android.hardware.SensorManager;

import java.util.List;

public class RotationValue extends AbstractValue {
    private Value azimuth = new Value();
    private Value pitch = new Value();
    private Value roll = new Value();

    private float[] geomagneticValues = new float[3];
    private float[] gravityValues = new float[3];

    public float getAzimuth()
    {
        float averagedAzimuth = (float)Math.toDegrees(this.azimuth.getAveraged());
//        this.db.addMeasure(new MeasureEntity(MeasureEntity.Type.ROTATION_AZIMUTH, averagedAzimuth));
        return averagedAzimuth;
    }

    public float getPitch()
    {
        float averagedPitch = (float)Math.toDegrees(this.pitch.getAveraged());
//        this.db.addMeasure(new MeasureEntity(MeasureEntity.Type.ROTATION_PITCH, averagedPitch));
        return averagedPitch;
    }

    public float getRoll()
    {
        float averagedRoll = (float)Math.toDegrees(this.roll.getAveraged());
//        this.db.addMeasure(new MeasureEntity(MeasureEntity.Type.ROTATION_ROLL, averagedRoll));
        return averagedRoll;
    }

    public float getMaxAzimuth()
    {
        return (float)Math.toDegrees(this.azimuth.getMax());
    }

    public float getMaxPitch()
    {
        return (float)Math.toDegrees(this.pitch.getMax());
    }

    public float getMaxRoll()
    {
        return (float)Math.toDegrees(this.roll.getMax());
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
