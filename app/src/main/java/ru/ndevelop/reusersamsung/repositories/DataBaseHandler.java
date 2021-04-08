package ru.ndevelop.reusersamsung.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import ru.ndevelop.reusersamsung.core.objects.Tag;
import ru.ndevelop.reusersamsung.utils.Action;
import ru.ndevelop.reusersamsung.utils.Utils;


public class DataBaseHandler extends SQLiteOpenHelper {
    private static final String DATABASENAME = "REUSER Database";
    private static final String TAGS_TABLENAME = "TAGS";
    private static final String COL_ID = "ID";
    private static final String COL_NAME = "NAME";
    private static final String COL_ACTION = "TAG_ACTION";
    private static final int VERSION = 1;
    private static DataBaseHandler instance;
    private static SQLiteDatabase readableDatabase;
    private static  SQLiteDatabase writableDatabase;
    private Context mCxt;

    private DataBaseHandler(Context ctx) {
        super(ctx, DATABASENAME, null, VERSION);
        readableDatabase = getReadableDatabase();
        writableDatabase = getWritableDatabase();
        this.mCxt = ctx;
    }

    public static DataBaseHandler getInstance(Context ctx) {
        if (instance == null) {
            instance = new DataBaseHandler(ctx);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable =
                "CREATE TABLE " + TAGS_TABLENAME + " (" + COL_ID + " TEXT, " + COL_NAME + " TEXT, " + COL_ACTION + " TEXT)";
        Log.d("DEBUG", createTable);
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public static void updateIfExistsElseInsert(String tagId, String tagName, ArrayList<Action> actions) {

        ContentValues contentValues = new ContentValues();
        if (tagId.equals("")) return;
        contentValues.put(COL_ID, tagId);

        StringBuilder actionsString = new StringBuilder();
        for (int i = 0; i < actions.size(); i++) {
            actionsString.append(i).append("-").append(actions.get(i).getActionType().name()).append("-").append(actions.get(i).getStatus()).append("-").append(actions.get(i).getSpecialData()).append("~");
        }

        contentValues.put(COL_NAME, tagName);
        contentValues.put(COL_ACTION, actionsString.toString());
        int rows = writableDatabase.update(TAGS_TABLENAME, contentValues, COL_ID + " = ?", new String[]{tagId});
        if (rows == 0) {
            contentValues.put(COL_ID, tagId);
            writableDatabase.insert(TAGS_TABLENAME, null, contentValues);
        }
    }

    public boolean isTagAlreadyExist(String tagId) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, tagId);
        int rows = writableDatabase.update(TAGS_TABLENAME, contentValues, COL_ID + " = ?", new String[]{tagId});
        return rows > 0;
    }
    void clearData(){
        writableDatabase.execSQL("DROP TABLE IF EXISTS "+TAGS_TABLENAME);
    }

    public static ArrayList<Action> getTagActions(String tagId) {

        ArrayList<Action> resultActions = new ArrayList<>();
        String query = "SELECT * FROM " +
                TAGS_TABLENAME +
                " WHERE " +
                COL_ID +
                "=?";
        Cursor tempResult = readableDatabase.rawQuery(query, new String[]{tagId});
        if (tempResult.getCount() > 0) {
            tempResult.moveToFirst();
            String tempActions = tempResult.getString(tempResult.getColumnIndex(COL_ACTION));
            resultActions = Utils.getActionsFromString(tempActions);


        }
        tempResult.close();
        return resultActions;
    }

    public static ArrayList<Tag> getTagsList() {

        ArrayList<Tag> result = new ArrayList<Tag>();
        try {
            String query = "Select * from " + TAGS_TABLENAME;
            Cursor tempResult = readableDatabase.rawQuery(query, null);
            if (tempResult.moveToFirst()) {
                do {
                    Tag tempTag = new Tag(
                            tempResult.getString(tempResult.getColumnIndex(COL_NAME)),
                            tempResult.getString(
                                    tempResult.getColumnIndex(
                                            COL_ID
                                    )
                            )
                    );
                    tempTag.setActions(Utils.getActionsFromString(
                            tempResult.getString(
                                    tempResult.getColumnIndex(
                                            COL_ACTION
                                    )
                            )
                    ));
                    result.add(tempTag);
                } while (tempResult.moveToNext());
            }
            tempResult.close();

        } catch (SQLException e) {

        }
        return result;
    }

    public static void deleteData(String tagId) {

        writableDatabase.delete(TAGS_TABLENAME, COL_ID + " = ?", new String[]{tagId});

    }

    public static String getTagName(String tagId) {
        String result = "";
        try {
            String query = "SELECT * FROM " +
                    TAGS_TABLENAME +
                    " WHERE " +
                    COL_ID +
                    "=?";
            Cursor tempResult = readableDatabase.rawQuery(query, new String[]{tagId});
            if (tempResult.moveToFirst()) {
                String mTempResult = tempResult.getString(
                        tempResult.getColumnIndex(
                                COL_NAME
                        ));
                if (!mTempResult.equals("New Tag")) result = mTempResult;

            }
            tempResult.close();

        } catch (SQLiteException e) {

        }
        return result;
    }
}
