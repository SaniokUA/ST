package azaza.myapplication.DataBase;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.HashMap;

/**
 * Created by Alex on 18.09.2015.
 */
public class PlansProvider extends ContentProvider {

    private static final int DATABASE_VERSION = 1;
    private static HashMap<String, String> sTasksProjectionMap;
    private static HashMap<String, String> sCategoriesProjectionMap;
    private static final int TASKS = 1;
    private static final int TASKS_ID = 2;
    private static final int CATEGORIES = 3;
    private static final int CATEGORIES_ID = 4;
    private static final UriMatcher sUriMatcher;
    private DatabaseHelper dbHelper;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(ContractPlans.AUTHORITY, "tasks", TASKS);
        sUriMatcher.addURI(ContractPlans.AUTHORITY, "tasks/#", TASKS_ID);
        sUriMatcher.addURI(ContractPlans.AUTHORITY, "categories", CATEGORIES);
        sUriMatcher.addURI(ContractPlans.AUTHORITY, "categories/#", CATEGORIES_ID);
        sTasksProjectionMap = new HashMap<String, String>();
        for (int i = 0; i < ContractPlans.Task.DEFAULT_PROJECTION.length; i++) {
            sTasksProjectionMap.put(
                    ContractPlans.Task.DEFAULT_PROJECTION[i],
                    ContractPlans.Task.DEFAULT_PROJECTION[i]);
        }
        sCategoriesProjectionMap = new HashMap<String, String>();
        for (int i = 0; i < ContractPlans.Category.DEFAULT_PROJECTION.length; i++) {
            sCategoriesProjectionMap.put(
                    ContractPlans.Category.DEFAULT_PROJECTION[i],
                    ContractPlans.Category.DEFAULT_PROJECTION[i]);
        }
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "ContractPlansDB";
        public static final String DATABASE_TABLE_TASKS = ContractPlans.Task.DB_TASK_TABLE;
        public static final String DATABASE_TABLE_CATEGORIES = ContractPlans.Category.DB_CATEGORIES_TABLE;

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

        public static final String CATEGORIES_COLUMN_ID = "_id";
        public static final String CATEGORY_COLUMN_NAME = "category_name";
        public static final String CATEGORY_COLUMN_COLOR = "color";


        private static final String DATABASE_CREATE_TABLE_TASKS =
                "create table " + DATABASE_TABLE_TASKS + " (" +
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
                        "); ";


        private static final String DATABASE_CREATE_TABLE_CATEGORIES =
                "create table " + DATABASE_TABLE_CATEGORIES + " (" +
                        CATEGORIES_COLUMN_ID + " integer primary key autoincrement, " +
                        CATEGORY_COLUMN_NAME + " text, " +
                        CATEGORY_COLUMN_COLOR + " text " +
                        "); ";

        private Context ctx;

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            ctx = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_TABLE_TASKS);
            db.execSQL(DATABASE_CREATE_TABLE_CATEGORIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_TASKS);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_CATEGORIES);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String finalWhere;
        int count;
        switch (sUriMatcher.match(uri)) {
            case TASKS:
                count = db.delete(ContractPlans.Task.DB_TASK_TABLE, where, whereArgs);
                break;
            case TASKS_ID:
                finalWhere = ContractPlans.Task._ID + " = " + uri.getPathSegments().get(ContractPlans.Task.TASKS_ID_PATH_POSITION);
                if (where != null) {
                    finalWhere = finalWhere + " AND " + where;
                }
                count = db.delete(ContractPlans.Task.DB_TASK_TABLE, finalWhere, whereArgs);
                break;
            case CATEGORIES:
                count = db.delete(ContractPlans.Category.DB_CATEGORIES_TABLE, where, whereArgs);
                break;
            case CATEGORIES_ID:
                finalWhere = ContractPlans.Category._ID + " = " + uri.getPathSegments().get(ContractPlans.Category.CATEGORIES_ID_PATH_POSITION);
                if (where != null) {
                    finalWhere = finalWhere + " AND " + where;
                }
                count = db.delete(ContractPlans.Category.DB_CATEGORIES_TABLE, finalWhere, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case TASKS:
                return ContractPlans.Task.CONTENT_TYPE;
            case TASKS_ID:
                return ContractPlans.Task.CONTENT_ITEM_TYPE;
            case CATEGORIES:
                return ContractPlans.Category.CONTENT_TYPE;
            case CATEGORIES_ID:
                return ContractPlans.Category.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        if (
                sUriMatcher.match(uri) != TASKS &&
                        sUriMatcher.match(uri) != CATEGORIES
                ) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }
        long rowId = -1;
        Uri rowUri = Uri.EMPTY;
        switch (sUriMatcher.match(uri)) {
            case TASKS:
                rowId = db.insert(ContractPlans.Task.DB_TASK_TABLE, null, values);
                if (rowId > 0) {
                    rowUri = ContentUris.withAppendedId(ContractPlans.Task.CONTENT_ID_URI_BASE, rowId);
                    getContext().getContentResolver().notifyChange(rowUri, null);
                }
                break;
            case CATEGORIES:
                rowId = db.insert(ContractPlans.Category.DB_CATEGORIES_TABLE, null, values);
                if (rowId > 0) {
                    rowUri = ContentUris.withAppendedId(ContractPlans.Category.CONTENT_ID_URI_BASE, rowId);
                    getContext().getContentResolver().notifyChange(rowUri, null);
                }
                break;
        }
        return rowUri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String orderBy = null;
        switch (sUriMatcher.match(uri)) {
            case TASKS:
                qb.setTables(ContractPlans.Task.DB_TASK_TABLE);
                qb.setProjectionMap(sTasksProjectionMap);
                orderBy = ContractPlans.Task.DEFAULT_SORT_ORDER;
                break;
            case TASKS_ID:
                qb.setTables(ContractPlans.Task.DB_TASK_TABLE);
                qb.setProjectionMap(sTasksProjectionMap);
                qb.appendWhere(ContractPlans.Task._ID + "=" + uri.getPathSegments().get(ContractPlans.Task.TASKS_ID_PATH_POSITION));
                orderBy = ContractPlans.Task.DEFAULT_SORT_ORDER;
                break;
            case CATEGORIES:
                qb.setTables(ContractPlans.Category.DB_CATEGORIES_TABLE);
                qb.setProjectionMap(sCategoriesProjectionMap);
                orderBy = ContractPlans.Category.DEFAULT_SORT_ORDER;
                break;
            case CATEGORIES_ID:
                qb.setTables(ContractPlans.Category.DB_CATEGORIES_TABLE);
                qb.setProjectionMap(sCategoriesProjectionMap);
                qb.appendWhere(ContractPlans.Category._ID + "=" + uri.getPathSegments().get(ContractPlans.Category.CATEGORIES_ID_PATH_POSITION));
                orderBy = ContractPlans.Category.DEFAULT_SORT_ORDER;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        String finalWhere;
        String id;
        switch (sUriMatcher.match(uri)) {
            case TASKS:
                count = db.update(ContractPlans.Task.DB_TASK_TABLE, values, where, whereArgs);
                break;
            case TASKS_ID:
                id = uri.getPathSegments().get(ContractPlans.Task.TASKS_ID_PATH_POSITION);
                finalWhere = ContractPlans.Task._ID + " = " + id;
                if (where != null) {
                    finalWhere = finalWhere + " AND " + where;
                }
                count = db.update(ContractPlans.Task.DB_TASK_TABLE, values, finalWhere, whereArgs);
                break;
            case CATEGORIES:
                count = db.update(ContractPlans.Category.DB_CATEGORIES_TABLE, values, where, whereArgs);
                break;
            case CATEGORIES_ID:
                id = uri.getPathSegments().get(ContractPlans.Category.CATEGORIES_ID_PATH_POSITION);
                finalWhere = ContractPlans.Category._ID + " = " + id;
                if (where != null) {
                    finalWhere = finalWhere + " AND " + where;
                }
                count = db.update(ContractPlans.Category.DB_CATEGORIES_TABLE, values, finalWhere, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    //Робота з тасками

    // добавить запись в DB_TASK_TABLE
    public void addRec(Context ctx, int active, String category, String number, String contact, String date, String text, long alarmdate, int marked, String typeCall, int position) {
        ContentValues cv = new ContentValues();
        cv.put(ContractPlans.Task.COLUMN_POSITION, position);
        cv.put(ContractPlans.Task.COLUMN_ACTIVE, active);
        cv.put(ContractPlans.Task.COLUMN_CATEGORY, category);
        cv.put(ContractPlans.Task.COLUMN_NUMBER, number);
        cv.put(ContractPlans.Task.COLUMN_MARKED, marked);
        cv.put(ContractPlans.Task.COLUMN_CONTACT, contact);
        cv.put(ContractPlans.Task.COLUMN_CREATION_DATE, date);
        cv.put(ContractPlans.Task.COLUMN_TXT, text);
        cv.put(ContractPlans.Task.COLUMN_TYPE_CALL, typeCall);
        cv.put(ContractPlans.Task.COLUMN_ALARM_DATE, alarmdate);
        ctx.getContentResolver().insert(ContractPlans.Task.CONTENT_URI, cv);
    }

    // удалить запись из DB_TASK_TABLE
    public void delRec(Context ctx, int id) {
        Uri uri = ContentUris.withAppendedId(ContractPlans.Task.CONTENT_URI, id);
        ctx.getContentResolver().delete(uri, null, null);
    }

    // удалить все записи из DB_TASK_TABLE
    public void delAllRec(Context ctx) {
        ctx.getContentResolver().delete(ContractPlans.Task.CONTENT_URI, null, null);
    }


    public Cursor getAllTasks(Context ctx) {
        Cursor cursor = ctx.getContentResolver().query(ContractPlans.Task.CONTENT_URI, null, null, null, null);
        return cursor;
    }

    public Cursor getMainListTask(Context ctx) {
        Cursor cursor = ctx.getContentResolver().query(ContractPlans.Task.CONTENT_URI, ContractPlans.Task.DEFAULT_PROJECTION, null, null, null);
        return cursor;
    }

    public Cursor getMainListTaskToDate(Context ctx, long startDay, long endDay, int marked) {
        String selectionClause = null;
        if (marked == 0) {
            selectionClause = ContractPlans.Task.COLUMN_ALARM_DATE + " >= " + startDay + " AND " + ContractPlans.Task.COLUMN_ALARM_DATE + " <= " + endDay;

        } else {
            selectionClause = ContractPlans.Task.COLUMN_ALARM_DATE + " >= " + startDay + " AND " + ContractPlans.Task.COLUMN_ALARM_DATE + " <= " + endDay + " AND "
                    + ContractPlans.Task.COLUMN_MARKED + " = " + marked;
        }
        Cursor cursor = ctx.getContentResolver().query(ContractPlans.Task.CONTENT_URI, ContractPlans.Task.DEFAULT_PROJECTION, selectionClause, null, ContractPlans.Task.COLUMN_ALARM_DATE);
        return cursor;
    }

    public Cursor getMainListTaskToFuture(Context ctx, long startDay, int marked) {
        String selectionClause = null;
        if (marked == 0) {
            selectionClause = ContractPlans.Task.COLUMN_ALARM_DATE + " >= " + startDay;
        } else {
            selectionClause = ContractPlans.Task.COLUMN_ALARM_DATE + " >= " + startDay + " AND " + ContractPlans.Task.COLUMN_MARKED + " = " + marked;
        }
        Cursor cursor = ctx.getContentResolver().query(ContractPlans.Task.CONTENT_URI, ContractPlans.Task.DEFAULT_PROJECTION, selectionClause, null, ContractPlans.Task.COLUMN_ALARM_DATE);
        return cursor;
    }

    //Робота з категоріями
    public Cursor getMainListTaskAllCategory(Context ctx) {
        Cursor cursor = ctx.getContentResolver().query(ContractPlans.Category.CONTENT_URI, ContractPlans.Category.DEFAULT_PROJECTION, null, null, "category_name ASC");
        return cursor;
    }

    public void addCategory(Context ctx, String categoryName, String color){
        ContentValues cv = new ContentValues();
        cv.put(ContractPlans.Category.CATEGORY_COLUMN_NAME, categoryName);
        cv.put(ContractPlans.Category.CATEGORY_COLUMN_COLOR, color);
        ctx.getContentResolver().insert(ContractPlans.Category.CONTENT_URI, cv);
    }

}
