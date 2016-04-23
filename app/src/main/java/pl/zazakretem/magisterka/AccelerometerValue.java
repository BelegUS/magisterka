package pl.zazakretem.magisterka;

public class AccelerometerValue extends AbstractValue {
    private float[] accelerometerValues = new float[3];
    private Value x = new Value();
    private Value y = new Value();
    private Value z = new Value();


    public AccelerometerValue setAccelerometerValues(float[] accelerometerValues) {
        this.accelerometerValues = accelerometerValues;
        this.calculate();
        return this;
    }

    public float getGForceX() {
        return calculateGForce(accelerometerValues[0]);
//        return x.getNext();
    }

    public float getGForceY() {
        return calculateGForce(accelerometerValues[1]);
//        return y.getNext();
    }

    public float getGForceZ() {
        return calculateGForce(accelerometerValues[2]);
//        return z.getNext();
    }

    private void calculate() {
        x.add(this.calculateGForce(accelerometerValues[0]));
        y.add(this.calculateGForce(accelerometerValues[1]));
        z.add(this.calculateGForce(accelerometerValues[2]));
    }

    private float calculateGForce(float value) {
        float g = 9.8f;
        return Math.abs(value / g);
    }

}
