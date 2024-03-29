package pl.zazakretem.magisterka;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "measureHolder";

    private static final String TABLE_MEASURES = "measures";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TYPE = "type";
    private static final String KEY_VALUE = "value";
    private static final String KEY_TIME = "time";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MEASURES_TABLE = "CREATE TABLE " + TABLE_MEASURES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TYPE + " TEXT,"
                + KEY_VALUE + " INTEGER,"
                + KEY_TIME + " INTEGER" + ")";
        db.execSQL(CREATE_MEASURES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEASURES);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    void addMeasure(MeasureEntity measure) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, measure.getType().name());
        values.put(KEY_VALUE, measure.getValue());
        values.put(KEY_TIME, System.currentTimeMillis());

        // Inserting Row
        db.insert(TABLE_MEASURES, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    MeasureEntity getMeasure(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MEASURES, new String[] { KEY_ID,
                        KEY_TYPE, KEY_VALUE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        MeasureEntity measure = new MeasureEntity(Integer.parseInt(cursor.getString(0)),
                MeasureEntity.Type.valueOf(cursor.getString(1)), cursor.getFloat(2), cursor.getInt(3));
        return measure;
    }

    // Getting All Contacts
    public List<MeasureEntity> getAllMeasures() {
        List<MeasureEntity> measureList = new ArrayList<MeasureEntity>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MEASURES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MeasureEntity measure = new MeasureEntity();
                measure.setId(Integer.parseInt(cursor.getString(0)));
                measure.setType(MeasureEntity.Type.valueOf(cursor.getString(1)));
                measure.setValue(cursor.getFloat(2));
                measure.setTime(cursor.getInt(3));
                // Adding contact to list
                measureList.add(measure);
            } while (cursor.moveToNext());
        }

        // return contact list
        return measureList;
    }

    // Updating single contact
    public int updateMeasure(MeasureEntity measure) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, measure.getType().name());
        values.put(KEY_VALUE, measure.getValue());

        // updating row
        return db.update(TABLE_MEASURES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(measure.getId()) });
    }

    // Deleting single contact
    public void deleteMeasure(MeasureEntity measure) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEASURES, KEY_ID + " = ?",
                new String[] { String.valueOf(measure.getId()) });
        db.close();
    }


    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MEASURES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
