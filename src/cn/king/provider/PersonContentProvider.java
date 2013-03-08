package cn.king.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import cn.king.dao.DBHelper;

public class PersonContentProvider extends ContentProvider {
	private DBHelper dbHelper;
	private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	public static int ALLPERSON = 1;
	public static int PERSON = 2;
	static {
		uriMatcher.addURI("cn.king.provider.person", "person", ALLPERSON);  //匹配所有的
		uriMatcher.addURI("cn.king.provider.person", "person/#", PERSON);	//#为通配符，匹配person/id
	}
	@Override
	public boolean onCreate() {
		dbHelper = new DBHelper(this.getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		int uriType = uriMatcher.match(uri);
		if(uriType == ALLPERSON){
			return db.query("person", projection, selection, selectionArgs, null, null, sortOrder);
		}else if(uriType == PERSON){
			long id = ContentUris.parseId(uri);
			String where = TextUtils.isEmpty(selection) ? "id=?" : selection + " and id=?";
			String[] whereArgs = null;
			if(selectionArgs != null){
				whereArgs = new String[selectionArgs.length + 1];
				int i = 0;
				for (; i < selectionArgs.length; i++) {
					whereArgs[i] = selectionArgs[i];
				}
				whereArgs[i] = String.valueOf(id);
			}else{
				whereArgs = new String[]{String.valueOf(id)};
			}
			return db.query("person", projection, where, whereArgs, null, null, sortOrder);
		}
		throw new IllegalArgumentException("Unkown uri:"+uri);
	}

	@Override
	public String getType(Uri uri) {
		int uriType = uriMatcher.match(uri);
		if(uriType == ALLPERSON){
			return "vnd.android.cursor.dir/person";
		}else if(uriType == PERSON){
			return "vnd.android.cursor.item/person";
		}
		throw new IllegalArgumentException("Unkown uri:"+uri);
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if(uriMatcher.match(uri) == ALLPERSON){
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			long id = db.insert("person", "name", values);
			return ContentUris.withAppendedId(uri, id);
		}
		throw new IllegalArgumentException("Unkown uri:"+uri);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int uriType = uriMatcher.match(uri);
		if(uriType == ALLPERSON){
			return db.delete("person", selection, selectionArgs);
		}else if(uriType == PERSON){
			long id = ContentUris.parseId(uri);
			String where = TextUtils.isEmpty(selection) ? "id=?" : selection + " and id=?";
			String[] whereArgs = null;
			if(selectionArgs != null){
				whereArgs = new String[selectionArgs.length + 1];
				int i = 0;
				for (; i < selectionArgs.length; i++) {
					whereArgs[i] = selectionArgs[i];
				}
				whereArgs[i] = String.valueOf(id);
			}else{
				whereArgs = new String[]{String.valueOf(id)};
			}
			return db.delete("person", where, whereArgs);
		}
		throw new IllegalArgumentException("Unkown uri:"+uri);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int uriType = uriMatcher.match(uri);
		if(uriType == ALLPERSON){
			return db.update("person", values, selection, selectionArgs);
		}else if(uriType == PERSON){
			long id = ContentUris.parseId(uri);
			selection = TextUtils.isEmpty(selection) ? "id=?" : selection + " and id=?";
			String[] whereArgs = null;
			if(selectionArgs != null){
				whereArgs = new String[selectionArgs.length + 1];
				int i = 0;
				for (; i < selectionArgs.length; i++) {
					whereArgs[i] = selectionArgs[i];
				}
				whereArgs[i] = String.valueOf(id);
			}else{
				whereArgs = new String[]{String.valueOf(id)};
			}
			return db.update("person", values, selection, whereArgs);
		}
		throw new IllegalArgumentException("Unkown uri:"+uri);
	}

}
