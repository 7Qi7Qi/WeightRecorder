package com.example.yui.server;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.yui.server.model.Record;

import java.sql.Timestamp;
import java.util.*;

public class YuiSQLiteHelper extends SQLiteOpenHelper {

    private static String dbName = "yui";
    private static String tableName = "RECORDER";
    private Integer maxId;


    public YuiSQLiteHelper(Context context) {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n" +
                "  `id` int(11) primary key,\n" +
                "  `date_time` TEXT ,\n" +
                "  `weight` NUMERIC(10, 2) \n" +
                ") ; ";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long add(Record record) {
        ContentValues cv = new ContentValues();
        if (maxId == null) {
            maxId = getMaxRecordId();
            System.out.println("Current max id: " + maxId);
        }
        cv.put("id", ++maxId);
        cv.put("date_time", record.getDateTime());
        cv.put("weight", record.getWeight());
        return getWritableDatabase().insert(tableName, null, cv);
    }

    public List<Record> queryRecords() {

        Calendar calendar = Calendar.getInstance();
        List<Record> records = new ArrayList<>();
        String[] args = new String[2];
        args[1] = new Timestamp(calendar.getTimeInMillis()).toString();
        calendar.add(Calendar.MONTH, -1);
        args[0] = new Timestamp(calendar.getTimeInMillis()).toString();

        Cursor cursor = getReadableDatabase().query(tableName, null, " date_time >= ? and date_time <= ? ", args, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Record record = new Record(cursor.getInt(0), cursor.getDouble(2), cursor.getString(1));
                records.add(record);
            }
        }
        return records;
    }

    public boolean deleteById(String text) {
        // validate text is integer
        Integer id = Integer.parseInt(text);
        int delete = delete(text);
        return delete == 1;
    }

    private int delete(String id) {
        String where;
        String[] args = new String[1];
        if (id == null) {
            where = "1 = 1";
        } else {
            where = " id = ? ";
            args[0] = id;
        }
        return getWritableDatabase().delete(tableName, where, args);
    }

    private Integer getMaxRecordId() {
        Cursor cursor = getReadableDatabase().rawQuery("select max(id) from " + tableName, null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                return cursor.getInt(0);
            }
        }
        return 0;
    }
}
