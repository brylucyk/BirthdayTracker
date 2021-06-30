package com.example.blucyk.birthdaytracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
    // Database & Table Names
    static final String DATABASE_NAME = "BirthdayTrackerDB";
    static final String DT_BIRTHDAYS = "Birthdays";
    static final String DT_GIFTS = "Gifts";
    static final String DT_BIRTHDAYS_GIFTS = "Birthdays_Gifts";

    // Table Keys
    static final String KEY_BIRTHDAY_ID = "Id";
    static final String KEY_BIRTHDAY_NAME = "Name";
    static final String KEY_BIRTHDAY_MONTH = "MONTH";
    static final String KEY_BIRTHDAY_DAY = "DAY";
    static final String KEY_GIFT_ID = "Id";
    static final String KEY_GIFT = "Gift";
    static final String KEY_BIRTHDAY_GIFT_ID = "Id";
    static final String KEY_FK_BIRTHDAY_ID = "Birthday_Id";
    static final String KEY_FK_GIFT_ID = "Gift_Id";

    static final String TAG = "DBAdapter";

    static final int DATABASE_VERSION = 1;

    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /*
         * Creates the SQLite Database. Populates the DT_ACTIVITIES table.
         *
         * @param   SQLiteDatabase db
         * @return  void
         */
        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL("create table " + DT_BIRTHDAYS
                        + "(" + KEY_BIRTHDAY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + KEY_BIRTHDAY_NAME + " STRING,"
                        + KEY_BIRTHDAY_MONTH + " INTEGER,"
                        + KEY_BIRTHDAY_DAY + " INTEGER)");
                db.execSQL("create table " + DT_GIFTS
                        + "(" + KEY_GIFT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + KEY_GIFT + " STRING)");
                db.execSQL("create table " + DT_BIRTHDAYS_GIFTS
                        + "(" + KEY_BIRTHDAY_GIFT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + KEY_FK_BIRTHDAY_ID + " INTEGER, "
                        + KEY_FK_GIFT_ID + " INTEGER, "
                        + "FOREIGN KEY (Birthday_ID) REFERENCES "
                        + DT_BIRTHDAYS + "(" + KEY_BIRTHDAY_ID + "),"
                        + "FOREIGN KEY (Gift_Id) REFERENCES "
                        + DT_GIFTS + "(" + KEY_GIFT_ID + "))");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /*
         * Creates the SQLite Database. Populates the DT_ACTIVITIES table.
         *
         * @param   SQLiteDatabase db, int oldVersion, int newVersion
         * @return  void
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }

    /*
     * Opens the Database.
     *
     * @param   void
     * @return  DBAdapter
     * @throws  SQLException
     *
     */
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    /*
     * Closes the Database.
     *
     * @param   void
     * @return  void
     */
    public void close()
    {
        DBHelper.close();
    }

    /*
     * Creates a new birthday.
     *
     * @param   void
     * @return  long (the session id in the DT_SESSIONS table)
     */
    public long insertBirthday(String name, int month, int day) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_BIRTHDAY_NAME, name);
        initialValues.put(KEY_BIRTHDAY_MONTH, month);
        initialValues.put(KEY_BIRTHDAY_DAY, day);
        return db.insert(DT_BIRTHDAYS, null, initialValues);
    }
//
//    /*
//     * Adds an activity to an existing session.
//     *
//     * @param   long sessionId, String name, int rating
//     * @return  long (the session id in the DT_SESSIONS_ACTIVITIES table)
//     */
//    public long addActivityToSession(long sessionId, String name, float rating) {
//        // retrieve the activity id from DT_ACTIVITIES
//        Cursor cursor = this.db.rawQuery("select " + KEY_ACTIVITY_ID + " from " + DT_ACTIVITIES
//                + " where " + KEY_ACTIVITY_NAME + " = '" + name + "'", null);
//
//        cursor.moveToFirst();
//        int retrievedActivityId = cursor.getInt(0);
//
//        ContentValues initialValues = new ContentValues();
//        initialValues.put(KEY_FK_SESSION_ID, (int) sessionId);
//        initialValues.put(KEY_FK_ACTIVITY_ID, retrievedActivityId);
//        initialValues.put(KEY_ACTIVITY_RATING, rating);
//
//        cursor.close();
//
//        return db.insert(DT_SESSIONS_ACTIVITIES, null, initialValues);
//    }
//
    /*
     * Retrieves all birthdays stored in the DT_BIRTHDAYS table.
     *
     * @param   void
     * @return  cursor storing all records from DT_BIRTHDAYS
     */
    public Cursor getAllBirthdays() {
        return db.query(DT_BIRTHDAYS, new String[] {KEY_BIRTHDAY_ID, KEY_BIRTHDAY_NAME,
                KEY_BIRTHDAY_MONTH, KEY_BIRTHDAY_DAY},
                null, null, null, null, KEY_BIRTHDAY_NAME
                        + " ASC");
    }
//
//    /*
//     * Retrieves the activity names and ratings associated with a specific training session.
//     *
//     * @param   int sessionId
//     * @return  cursor storing the activity names and ratings
//     */
//    public Cursor getSessionActivities(int sessionId) {
//        return this.db.rawQuery(
//                "select " + KEY_ACTIVITY_NAME + ", " + KEY_ACTIVITY_RATING
//                        + " from " + DT_SESSIONS_ACTIVITIES + " INNER JOIN " + DT_ACTIVITIES
//                        + " ON " + DT_ACTIVITIES + "." + KEY_ACTIVITY_ID
//                        + " = " + DT_SESSIONS_ACTIVITIES + "." + KEY_FK_ACTIVITY_ID
//                        + " where " + KEY_FK_SESSION_ID + " = " + sessionId, null);
//    }
//
//    /*
//     * Deletes a specific training session from the database.
//     *
//     * Note: Will first try to delete from the DT_SESSION_ACTIVITIES table.
//     *       If this does not work, will not proceed with trying to delete
//     *       from the DT_SESSIONS table.
//     *
//     * @param   int sessionId
//     * @return  boolean (true if deleted, false if not)
//     */
//    public boolean deleteSession(int sessionId) {
//
//        if(deleteSessionActivities(sessionId)) {
//            return db.delete(DT_SESSIONS, KEY_SESSION_ID + "=" + sessionId, null) > 0;
//        }
//        else {
//            return false;
//        }
//    }
//
//    /*
//     * Deletes all activities associated with a particular session.
//     *
//     * @param   int sessionId
//     * @return  boolean (true if deleted, false if not)
//     */
//    public boolean deleteSessionActivities(long sessionId) {
//        return db.delete(DT_SESSIONS_ACTIVITIES, KEY_FK_SESSION_ID + "=" + sessionId, null) > 0;
//    }
//
//    /*
//     * Deletes all training sessions from the database.
//     *
//     * @param   void
//     * @return  int (number of rows deleted)
//     */
//    public int deleteSessions() {
//        deleteAllSessionActivities();
//        return db.delete(DT_SESSIONS, "1", null);
//    }
//
//    /*
//     * Deletes all session activities from the database.
//     *
//     * @param   void
//     * @return  int (number of rows deleted)
//     */
//    public int deleteAllSessionActivities() {
//        return db.delete(DT_SESSIONS_ACTIVITIES, "1", null);
//    }

}
