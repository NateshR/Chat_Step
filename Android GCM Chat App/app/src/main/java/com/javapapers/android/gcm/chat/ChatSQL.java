package com.javapapers.android.gcm.chat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatSQL {

    private String database_name;
    private String database_table;
    private final String copy_database_table = "COPYTABLE";
    private int database_version;
    private Context ourContext;
    private DBhelper ourDBhelper;
    private SQLiteDatabase ourDatabase;

    //Columns
    public static final String chat_id = "chat_id";
    public static final String chat_text = "chat_text";
    public static final String chat_gravity = "chat_gravity";

    //TAGS
    private final static String TAG_TABLE_CREATED = "TABLE_CREATED";
    private final static String TAG_VALUE_ADDED = "VALUE_ADDED";
    private final static String TAG_VALUE_DRAWN = "VALUE_DRAWN";
    private final static String TAG_VALUE_DELETE = "VALUE DELETED";
    private final static String TAG_VALUE_DELETE_TABLE = "TABLE DELETED";

    private class DBhelper extends SQLiteOpenHelper {

        private String database_table;

        // Initializes table for DBhelper class
        public DBhelper(Context context, String database_name,
                        String database_table, int database_version) {
            super(context, database_name, null, database_version);
            this.database_table = database_table;
            // TODO Auto-generated constructor stub
        }

        // Used to create a dummy table
        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL("CREATE TABLE " + this.database_table + " (" + "key_id"
                    + " INTEGER PRIMARY KEY AUTOINCREMENT); ");
            Log.d(TAG_TABLE_CREATED, this.database_table + " CREATED");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL(" DROP TABLE IF EXISTS " + this.database_table);
            onCreate(db);
        }

    }

    public ChatSQL(Context ourContext, String database_name,
                   String database_table, int database_version) {
        this.ourContext = ourContext;
        this.database_name = database_name;
        this.database_table = database_table;
        this.database_version = database_version;
        this.Open();
    }

    public ChatSQL Open() throws SQLException {
        this.ourDBhelper = new DBhelper(this.ourContext, this.database_name,
                this.database_table, this.database_version);
        this.ourDatabase = this.ourDBhelper.getWritableDatabase();
        return this;
    }

    public void Close() {
        ourDBhelper.close();
    }

    public void CreateTable() {
        this.ourDatabase.execSQL("CREATE TABLE " + this.copy_database_table + " ( " + this.chat_id
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + this.chat_text
                + " TEXT NOT NULL, " + this.chat_gravity + " TEXT NOT NULL); ");
        this.ourDatabase.execSQL("DROP TABLE " + this.database_table);
        this.ourDatabase.execSQL("ALTER TABLE " + this.copy_database_table
                + " RENAME TO " + this.database_table);
    }

    public long CreateEntry(String chat_text, String chat_gravity) {
        ContentValues cv = new ContentValues();
        cv.put(this.chat_text, chat_text);
        cv.put(this.chat_gravity, chat_gravity);

        return this.ourDatabase.insert(this.database_table, null, cv);
    }

    public String[] getChatText() {
        String[] columns = {this.chat_id, this.chat_text, this.chat_gravity};
        Cursor c = this.ourDatabase.query(this.database_table, columns, null, null, null,
                null, null);
        int get = c.getColumnIndex(this.chat_text);
        int i = 0;
        int count = 0;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            count = count + 1;
        }
        String[] result = new String[count];
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result[i] = c.getString(get);
            i++;

        }

        return result;
    }

    public String[] getChatGravity() {
        String[] columns = {this.chat_id, this.chat_text, this.chat_gravity};
        Cursor c = this.ourDatabase.query(this.database_table, columns, null, null, null,
                null, null);
        int get = c.getColumnIndex(this.chat_gravity);
        int i = 0;
        int count = 0;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            count = count + 1;
        }
        String[] result = new String[count];
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result[i] = c.getString(get);
            i++;

        }
        return result;
    }


}