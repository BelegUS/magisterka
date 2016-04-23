package pl.zazakretem.magisterka;

public class MeasureEntity {
    public enum Type {
        GFORCE_X,
        GFORCE_Y,
        GFORCE_Z,
        ROTATION_AZIMUTH,
        ROTATION_PITCH,
        ROTATION_ROLL,
        SPEED
    }

    private int _id;
    private Type _type;
    private float _value;

    public MeasureEntity(int id, Type type, float value) {
        this._id = id;
        this._type = type;
        this._value = value;
    }

    public MeasureEntity(Type type, float value) {
        this._type = type;
        this._value = value;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public Type getType() {
        return _type;
    }

    public void setType(Type type) {
        this._type = type;
    }

    public float getValue() {
        return _value;
    }

    public void setValue(float value) {
        this._value = value;
    }
}
