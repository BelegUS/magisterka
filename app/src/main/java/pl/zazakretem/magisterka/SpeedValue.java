package pl.zazakretem.magisterka;


import android.util.Log;

public class SpeedValue extends AbstractValue
{
    private float speedValue = 0;
    private Value speed = new Value();

    public SpeedValue setSpeedValue(float speedValue)
    {
        Log.d("KITTEN", String.valueOf(speedValue));
        this.speedValue = speedValue;
        this.calculate();
        return this;
    }

    private void calculate()
    {
        this.speed.add(speedValue);
    }

    public float getSpeed()
    {
        return speed.getAveraged();
    }

}

