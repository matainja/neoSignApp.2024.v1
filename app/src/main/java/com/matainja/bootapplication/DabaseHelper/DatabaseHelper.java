package com.matainja.bootapplication.DabaseHelper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.matainja.bootapplication.Model.VideoItem;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "video_database";
    public static final int DATABASE_VERSION = 1;

    // Table name and columns
    public static final String TABLE_VIDEOS = "videos";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_VIDEO_URL = "video_url";
    public static final String COLUMN_LOCAL_FILE_PATH = "local_file_path";

    // Create table SQL statement
    public static final String CREATE_TABLE_VIDEOS = "CREATE TABLE " + TABLE_VIDEOS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TITLE + " TEXT,"
            + COLUMN_VIDEO_URL + " TEXT,"
            + COLUMN_LOCAL_FILE_PATH + " TEXT"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_VIDEOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEOS);
        // Create tables again
        onCreate(db);
    }

    public long addVideo(VideoItem videoItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, videoItem.getTitle());
        values.put(COLUMN_VIDEO_URL, videoItem.getVideo_url());
        values.put(COLUMN_LOCAL_FILE_PATH, videoItem.getVideo_path());
        long id = db.insert(TABLE_VIDEOS, null, values);
        db.close();
        return id;
    }
    /*@SuppressLint({"Range", "SuspiciousIndentation"})
    public VideoItem getVideo(String filenameFromUri) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_VIDEOS,
                new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_VIDEO_URL, COLUMN_LOCAL_FILE_PATH},
                COLUMN_TITLE + "=?",
                new String[]{title}, // Replace `title` with the actual title you want to search for
                null,
                null,
                null,
                null);
        if (cursor != null)
        cursor.moveToFirst();
        VideoItem videoItem = new VideoItem();
        videoItem.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
        videoItem.setVideo_url(cursor.getString(cursor.getColumnIndex(COLUMN_VIDEO_URL)));
        videoItem.setVideo_path(cursor.getString(cursor.getColumnIndex(COLUMN_LOCAL_FILE_PATH)));
        cursor.close();
        return videoItem;
    }*/

    public Cursor getVideoByTitle(String title) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {COLUMN_TITLE, COLUMN_VIDEO_URL, COLUMN_LOCAL_FILE_PATH};
        String selection = COLUMN_TITLE + "=?";
        String[] selectionArgs = {title};

        Cursor cursor = db.query(TABLE_VIDEOS, columns, selection, selectionArgs, null, null, null);

        // You can now use the cursor to iterate over the results
        return cursor;
    }
    public void deleteVideoByTitle(String title) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = COLUMN_TITLE + "=?";
        String[] whereArgs = {title};

        db.delete(TABLE_VIDEOS, whereClause, whereArgs);

        db.close();
    }
    /*public void deleteAllVideos() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VIDEOS, null, null);
        db.close();
    }*/
    public void deleteAllVideos() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if there are any records in the TABLE_VIDEOS
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_VIDEOS, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0); // Getting count of records

            // Close the cursor after getting the count
            cursor.close();

            // If there are records, delete them
            if (count > 0) {
                db.delete(TABLE_VIDEOS, null, null);
            }
        }

        // Close the database connection
        db.close();
    }


}
