package azaza.myapplication.DataBase;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Alex on 18.09.2015.
 */
public class ContractPlans {

    // // Uri
    // authority
    public static final String AUTHORITY = "futureplans.provider";

    private ContractPlans() {
    }

    public static final class Task implements BaseColumns {

        private Task() {
        }

        static final String DB_TASK_TABLE = "taskTable";

        private static final String TASKS_PATH = "/tasks";
        private static final String TASK_ID_PATH = "/tasks/";
        private static final String SCHEME = "content://";

        public static final int TASKS_ID_PATH_POSITION = 1;

        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + TASKS_PATH);
        public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + TASK_ID_PATH);

        // Типы данных
        // набор строк
        static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
                + AUTHORITY + "." + TASKS_PATH;
        // одна строка
        static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
                + AUTHORITY + "." + TASKS_PATH;

        public static final String DEFAULT_SORT_ORDER = "_id ASC";


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

        public static final String[] DEFAULT_PROJECTION = new String[]{
                Task.COLUMN_ID,
                Task.COLUMN_ACTIVE,
                Task.COLUMN_CATEGORY,
                Task.COLUMN_TXT,
                Task.COLUMN_ALARM_DATE,
                Task.COLUMN_MARKED
        };
    }

    public static final class Category implements BaseColumns {

        private Category(){
        }

        static final String DB_CATEGORIES_TABLE = "categoriesTable";

        private static final String CATEGORIES_PATH = "/categories";
        private static final String CATEGORIES_ID_PATH = "/categories/";
        private static final String SCHEME = "content://";

        public static final int CATEGORIES_ID_PATH_POSITION = 1;

        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + CATEGORIES_PATH);
        public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + CATEGORIES_ID_PATH);

        // Типы данных
        // набор строк
        static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
                + AUTHORITY + "." + CATEGORIES_PATH;
        // одна строка
        static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
                + AUTHORITY + "." + CATEGORIES_PATH;

        public static final String DEFAULT_SORT_ORDER = "category_name ASC";

        public static final String CATEGORY_COLUMN_ID = "_id";
        public static final String CATEGORY_COLUMN_NAME = "category_name";
        public static final String CATEGORY_COLUMN_COLOR = "color";

        public static final String[] DEFAULT_PROJECTION = new String[]{
                "DISTINCT category_name"
        };
    }
}
