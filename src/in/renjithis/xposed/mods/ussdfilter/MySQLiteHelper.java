package in.renjithis.xposed.mods.ussdfilter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

  public static final String TABLE_FILTERS = "preferences";
  public static final String COLUMN_ID = "_id";
  
  public static final String COLUMN_FILTER_NAME = "filter_name";
  public static final String COLUMN_FILTER_TYPE = "filter_type";
  public static final String COLUMN_FILTER_STRING = "filter_string";
  public static final String COLUMN_FILTER_OUTPUT_TYPE = "filter_output";
  public static final String COLUMN_FILTER_PRIORITY = "filter_priority";
  public static final String COLUMN_FILTER_ENABLED = "filter_enabled";

  private static final String DATABASE_NAME = "ussdfilter.db";
  private static final int DATABASE_VERSION = 1;

  // Database creation sql statement
  private static final String DATABASE_CREATE = "create table "
      + TABLE_FILTERS + "(" + COLUMN_ID
      + " integer primary key autoincrement, " + COLUMN_FILTER_NAME
      + " text not null, " + COLUMN_FILTER_TYPE + " integer, " + COLUMN_FILTER_STRING 
      + " text not null, " + COLUMN_FILTER_OUTPUT_TYPE + "integer, " 
      + COLUMN_FILTER_PRIORITY + "integer, " + COLUMN_FILTER_ENABLED + "integer);";

  public MySQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//    Log.w(MySQLiteHelper.class.getName(),
//        "Upgrading database from version " + oldVersion + " to "
//            + newVersion + ", which will destroy all old data");
//    db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILTERS);
//    onCreate(db);
  }

} 