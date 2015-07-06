package azaza.myapplication.DataBase;

/**
 * Created by Alex on 05.06.2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DB {

    private static final String DB_NAME = "mydb";
    private static final int DB_VERSION = 2;
    private static final String DB_TABLE = "mytab";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_NUBMER = "number";
    public static final String COLUMN_CONTACT = "contact";
    public static final String COLUMN_TXT = "txt";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_ALARMDATE = "alarmDate";

    private static final String DB_CREATE =
            "create table " + DB_TABLE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_TYPE + " text, " +
                    COLUMN_NUBMER + " text, " +
                    COLUMN_CONTACT + " text, " +
                    COLUMN_DATE + " text, " +
                    COLUMN_TXT + " text, " +
                    COLUMN_ALARMDATE + " long" +
                    ");";

    private final Context mCtx;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DB(Context ctx) {
        mCtx = ctx;
    }

    // открыть подключение
    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    // закрыть подключение
    public void close() {
        if (mDBHelper != null) mDBHelper.close();
    }

    // получить все данные из таблицы DB_TABLE
    public Cursor getAllData() {
       if(!mDB.isOpen()){
           open();
       }
        return mDB.query(DB_TABLE, null, null, null, null, null, COLUMN_ID + " DESC");

    }

    // добавить запись в DB_TABLE
    public void addRec(String myType, String number, String contact, String date, String text, long alarmdate) {

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TYPE, myType);
        cv.put(COLUMN_NUBMER, number);
        cv.put(COLUMN_CONTACT, contact);
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_TXT, text);
        cv.put(COLUMN_ALARMDATE, alarmdate);
        mDB.insert(DB_TABLE, null, cv);

    }

    // удалить запись из DB_TABLE
    public void delRec(int id) {
        if(!mDB.isOpen()){
            open();
        }
        mDB.delete(DB_TABLE, COLUMN_ID + "=" + id, null);

    }

    // удалить все записи из DB_TABLE
    public void delAllRec() {
        if(!mDB.isOpen()){
            open();
        }
        mDB.delete(DB_TABLE, null, null);

    }

    // класс по созданию и управлению БД
    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }

        // создаем и заполняем БД
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);

            ContentValues cv = new ContentValues();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}