package in.renjithis.xposed.mods.ussdfilter;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class FiltersDataSource {

  // Database fields
  private SQLiteDatabase database;
  private MySQLiteHelper dbHelper;
  private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
      MySQLiteHelper.COLUMN_FILTER_NAME, MySQLiteHelper.COLUMN_FILTER_TYPE, 
      MySQLiteHelper.COLUMN_FILTER_STRING, MySQLiteHelper.COLUMN_FILTER_PRIORITY,
      MySQLiteHelper.COLUMN_FILTER_ENABLED, MySQLiteHelper.COLUMN_FILTER_OUTPUT_TYPE };

  public FiltersDataSource(Context context) {
    dbHelper = new MySQLiteHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public Filter createFilter(Filter filter) {
    ContentValues values = new ContentValues();
    values.put(MySQLiteHelper.COLUMN_FILTER_NAME, filter.name);
    values.put(MySQLiteHelper.COLUMN_FILTER_TYPE, filter.type.name());
    values.put(MySQLiteHelper.COLUMN_FILTER_STRING, filter.subStringRegEx);
    values.put(MySQLiteHelper.COLUMN_FILTER_PRIORITY, filter.priority);
    values.put(MySQLiteHelper.COLUMN_FILTER_ENABLED, filter.enabled);
    values.put(MySQLiteHelper.COLUMN_FILTER_OUTPUT_TYPE, filter.outputType.name());
    
    long insertId = database.insert(MySQLiteHelper.TABLE_FILTERS, null,
        values);
    Cursor cursor = database.query(MySQLiteHelper.TABLE_FILTERS,
        allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    Filter newFilter = cursorToFilter(cursor);
    cursor.close();
    return newFilter;
  }

  public void deleteFilter(Filter filter) {
    long id = filter.getId();
    System.out.println("Filter deleted with id: " + id);
    database.delete(MySQLiteHelper.TABLE_FILTERS, MySQLiteHelper.COLUMN_ID
        + " = " + id, null);
  }

  public List<Filter> getAllFilters() {
    List<Filter> filters = new ArrayList<Filter>();

    Cursor cursor = database.query(MySQLiteHelper.TABLE_FILTERS,
        allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Filter filter = cursorToFilter(cursor);
      filters.add(filter);
      cursor.moveToNext();
    }
    // Make sure to close the cursor
    cursor.close();
    return filters;
  }

  private Filter cursorToFilter(Cursor cursor) {
    Filter filter = new Filter();
    filter.setId(cursor.getLong(0));
    
    // ???: is this right?
    
    filter.name=cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_FILTER_NAME));
    filter.type=FilterType.valueOf(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_FILTER_TYPE)));
    filter.subStringRegEx=cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_FILTER_STRING));
    filter.priority=cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_FILTER_PRIORITY));
    filter.enabled=Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_FILTER_ENABLED)));
    filter.outputType=OutputType.valueOf(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_FILTER_OUTPUT_TYPE)));
    
    return filter;
  }
} 