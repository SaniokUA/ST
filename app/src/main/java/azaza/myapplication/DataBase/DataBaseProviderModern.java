package azaza.myapplication.DataBase;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Alex on 07.09.2015.
 */
public class DataBaseProviderModern extends ContentProvider {
    final String LOG_TAG = "myLogs";

    // // Константы для БД
    // БД
    static final String DB_NAME = "taskDataBase";
    static final int DB_VERSION = 1;

    // Таблица


    private static final String DB_TABLE = "taskTable";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CREATION_DATE = "creationDate";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_MARKED = "mark";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_CONTACT = "contact";
    public static final String COLUMN_TXT = "txt";
    public static final String COLUMN_TYPE_CALL = "typeCall";
    public static final String COLUMN_ALARM_DATE = "alarmDate";
    public static final String COLUMN_ACTIVE = "activeTask";
    public static final String COLUMN_POSITION = "position";


    // Скрипт создания таблицы
    private static final String DB_CREATE =
            "create table " + DB_TABLE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_CATEGORY + " text, " +
                    COLUMN_MARKED + " int, " +
                    COLUMN_NUMBER + " text, " +
                    COLUMN_CONTACT + " text, " +
                    COLUMN_CREATION_DATE + " text, " +
                    COLUMN_TXT + " text, " +
                    COLUMN_TYPE_CALL + " text, " +
                    COLUMN_ACTIVE + " int, " +
                    COLUMN_POSITION + " int, " +
                    COLUMN_ALARM_DATE + " long" +
                    ");";

    // // Uri
    // authority
    static final String AUTHORITY = "futureplans.provider";

    // path
    static final String CONTACT_PATH = "tasks";

    // Общий Uri
    public static final Uri CONTACT_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + CONTACT_PATH);

    // Типы данных
    // набор строк
    static final String CONTACT_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + CONTACT_PATH;

    // одна строка
    static final String CONTACT_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + CONTACT_PATH;

    //// UriMatcher
    // общий Uri
    static final int URI_CONTACTS = 1;

    // Uri с указанным ID
    static final int URI_CONTACTS_ID = 2;

    // описание и создание UriMatcher
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, CONTACT_PATH, URI_CONTACTS);
        uriMatcher.addURI(AUTHORITY, CONTACT_PATH + "/#", URI_CONTACTS_ID);
    }

    DBHelper dbHelper;
    SQLiteDatabase db;

    public boolean onCreate() {
        Log.d(LOG_TAG, "onCreate");
        dbHelper = new DBHelper(getContext(), DB_NAME, null, DB_VERSION);

        return true;
    }

    // чтение
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, "query, " + uri.toString());
        // проверяем Uri
        switch (uriMatcher.match(uri)) {
            case URI_CONTACTS: // общий Uri
                Log.d(LOG_TAG, "URI_CONTACTS");
                // если сортировка не указана, ставим свою - по имени
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = COLUMN_ID + " ASC";
                }
                break;
            case URI_CONTACTS_ID: // Uri с ID
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
                // добавляем ID к условию выборки
                if (TextUtils.isEmpty(selection)) {
                    selection = COLUMN_ID + " = " + id;
                } else {
                    selection = selection + " AND " + COLUMN_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(DB_TABLE, projection, selection,
                selectionArgs, null, null, sortOrder);
        // просим ContentResolver уведомлять этот курсор
        // об изменениях данных в CONTACT_CONTENT_URI
        cursor.setNotificationUri(getContext().getContentResolver(),
                CONTACT_CONTENT_URI);
        return cursor;
    }

    public Uri insert(Uri uri, ContentValues values) {
        Log.d(LOG_TAG, "insert, " + uri.toString());
        if (uriMatcher.match(uri) != URI_CONTACTS)
            throw new IllegalArgumentException("Wrong URI: " + uri);
        dbHelper = new DBHelper(getContext(), DB_NAME, null, DB_VERSION);
        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(DB_TABLE, null, values);
        Uri resultUri = ContentUris.withAppendedId(CONTACT_CONTENT_URI, rowID);
        // уведомляем ContentResolver, что данные по адресу resultUri изменились
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "delete, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_CONTACTS:
                Log.d(LOG_TAG, "URI_CONTACTS");
                break;
            case URI_CONTACTS_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = COLUMN_ID + " = " + id;
                } else {
                    selection = selection + " AND " + COLUMN_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.delete(DB_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.d(LOG_TAG, "update, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_CONTACTS:
                Log.d(LOG_TAG, "URI_CONTACTS");

                break;
            case URI_CONTACTS_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = COLUMN_ID + " = " + id;
                } else {
                    selection = selection + " AND " + COLUMN_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.update(DB_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_CONTACTS:
                return CONTACT_CONTENT_TYPE;
            case URI_CONTACTS_ID:
                return CONTACT_CONTENT_ITEM_TYPE;
        }
        return null;
    }


    public Cursor getAllData(Context ctx) {
        Cursor cursor = ctx.getContentResolver().query(CONTACT_CONTENT_URI, null, null, null, null);
        return cursor;
    }

    public Cursor getMainListTask(Context ctx) {
        String[] columns = new String[]{COLUMN_ID, COLUMN_ACTIVE, COLUMN_CATEGORY, COLUMN_TXT, COLUMN_ALARM_DATE, COLUMN_MARKED};
        Cursor cursor = ctx.getContentResolver().query(CONTACT_CONTENT_URI, columns, null, null, null);
        return cursor;
    }

    public Cursor getMainListTaskToDate(Context ctx, long startDay, long endDay, int marked) {
        String selectionClause = null;
        if (marked == 0) {
            selectionClause = COLUMN_ALARM_DATE + " >= " + startDay + " AND " + COLUMN_ALARM_DATE + " <= " + endDay;

        } else {
            selectionClause = COLUMN_ALARM_DATE + " >= " + startDay + " AND " + COLUMN_ALARM_DATE + " <= " + endDay + " AND " + COLUMN_MARKED + " = " + marked;
        }
        String[] columns = new String[]{COLUMN_ID, COLUMN_ACTIVE, COLUMN_CATEGORY, COLUMN_TXT, COLUMN_ALARM_DATE, COLUMN_MARKED};
        Cursor cursor = ctx.getContentResolver().query(CONTACT_CONTENT_URI, columns, selectionClause, null, COLUMN_ALARM_DATE);
        return cursor;
    }

    public Cursor getMainListTaskToFuture(Context ctx, long startDay, int marked) {
        String selectionClause = null;
        if (marked == 0) {
            selectionClause = COLUMN_ALARM_DATE + " >= " + startDay;
        } else {
            selectionClause = COLUMN_ALARM_DATE + " >= " + startDay + " AND " + COLUMN_MARKED + " = " + marked;
        }
        String[] columns = new String[]{COLUMN_ID, COLUMN_ACTIVE, COLUMN_CATEGORY, COLUMN_TXT, COLUMN_ALARM_DATE, COLUMN_MARKED};
        Cursor cursor = ctx.getContentResolver().query(CONTACT_CONTENT_URI, columns, selectionClause, null, COLUMN_ALARM_DATE);
        return cursor;
    }

    public Cursor getMainListTaskAllCategory(Context ctx) {
        String[] columns = new String[]{"DISTINCT category"};
        //String selectionClause = "DISTINCT(" + COLUMN_CATEGORY + ")";
        Cursor cursor = ctx.getContentResolver().query(CONTACT_CONTENT_URI, columns, null, null, "category ASC");
        return cursor;
    }



    // добавить запись в DB_TABLE
    public void addRec(Context ctx, int active, String category, String number, String contact, String date, String text, long alarmdate, int marked, String typeCall, int position) {

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_POSITION, position);
        cv.put(COLUMN_ACTIVE, active);
        cv.put(COLUMN_CATEGORY, category);
        cv.put(COLUMN_NUMBER, number);
        cv.put(COLUMN_MARKED, marked);
        cv.put(COLUMN_CONTACT, contact);
        cv.put(COLUMN_CREATION_DATE, date);
        cv.put(COLUMN_TXT, text);
        cv.put(COLUMN_TYPE_CALL, typeCall);
        cv.put(COLUMN_ALARM_DATE, alarmdate);
        ctx.getContentResolver().insert(CONTACT_CONTENT_URI, cv);
    }

    // удалить запись из DB_TABLE
    public void delRec(Context ctx, int id) {
        Uri uri = ContentUris.withAppendedId(CONTACT_CONTENT_URI, id);
        ctx.getContentResolver().delete(uri, null, null);
    }

    // удалить все записи из DB_TABLE
    public void delAllRec(Context ctx) {
        ctx.getContentResolver().delete(CONTACT_CONTENT_URI, null, null);
    }

    // класс по созданию и управлению БД
    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
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
