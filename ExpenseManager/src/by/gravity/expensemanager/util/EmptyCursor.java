package by.gravity.expensemanager.util;

import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;

public class EmptyCursor implements Cursor{

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] getBlob(int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getColumnIndex(String columnName) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getColumnName(int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getColumnNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public double getDouble(int columnIndex) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Bundle getExtras() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getFloat(int columnIndex) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getInt(int columnIndex) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLong(int columnIndex) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPosition() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public short getShort(int columnIndex) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getString(int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getType(int columnIndex) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getWantsAllOnMoveCalls() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAfterLast() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isBeforeFirst() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isClosed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFirst() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLast() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isNull(int columnIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean move(int offset) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean moveToFirst() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean moveToLast() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean moveToNext() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean moveToPosition(int position) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean moveToPrevious() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void registerContentObserver(ContentObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean requery() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Bundle respond(Bundle extras) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNotificationUri(ContentResolver cr, Uri uri) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterContentObserver(ContentObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Uri getNotificationUri() {
		// TODO Auto-generated method stub
		return null;
	}

}
