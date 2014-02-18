package com.team5.user;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserAccount {
	// Variables
	private DatabaseHelper myDatabaseHelper;
	private SQLiteDatabase myDatabase;  
	private boolean loggedIn = false;
	private String passKey = "";
    // Login attempts
	private int loginAttempts = 0;
	private final int maxLoginAttempts = 5;


    public UserAccount(Context context){
		// temporary
		passKey = "1234";


        // Get a database helper
		myDatabaseHelper = new DatabaseHelper(context);  
		// Have the database helper get us the database
        myDatabase = myDatabaseHelper.getWritableDatabase();
    }

    public boolean getLoggedIn()	{
		// return whether we are logged in
		return loggedIn;
	}
	
	public boolean logIn(String passKey)	{
		// If the password is right
		if (this.passKey.contentEquals(passKey))	{
			// Process login
			logInProcess();
			// Success
			return true;
		// If the password required is blank
		}	else if (this.passKey.contentEquals(""))	{
			// Process login
			logInProcess();
			// Success
			return true;
		}

        // No success and increment amount user tried
        loginAttempts++;
        if (loginAttempts > maxLoginAttempts)	{
        	// Go to password recovery
        	
        }
        return false;
	}
	
	private void logInProcess()	{
		// We logged in
		loggedIn = true;
		
		// Temporay data
        if (getRecordByID(1).size() == 0)	{
        	// if no records, then add some
        	
        	long current = new java.util.Date().getTime();
            addRecord(new UserRecord(current, 1, 10, "today"));
            addRecord(new UserRecord(current - (60*60*24*1), 1, 10, "1 day ago"));
            addRecord(new UserRecord(current - (60*60*24*2), 10, 9, "2 days ago"));
            addRecord(new UserRecord(current - (60*60*24*3), 2, 8, "3 days ago"));
            addRecord(new UserRecord(current - (60*60*24*4), 8, 7, "4 days ago"));
            addRecord(new UserRecord(current - (60*60*24*5), 3, 6, "5 days ago"));
            addRecord(new UserRecord(current - (60*60*24*6), 7, 5, "6 days ago"));
            addRecord(new UserRecord(current - (60*60*24*7), 4, 4, "7 days ago"));
            addRecord(new UserRecord(current - (60*60*24*8), 6, 3, "8 days ago"));
            addRecord(new UserRecord(current - (60*60*24*9), 5, 2, "9 days ago"));
            addRecord(new UserRecord(current - (60*60*24*10), 5, 1, "10 days ago"));
        }
	}
	
	public boolean addRecord(UserRecord record)	{
		// We must be logged in
		if (!getLoggedIn())	{
			return false;
		}
		
		// Create a new values object
		ContentValues values = new ContentValues();
		// Store the values from the record
		values.put(DatabaseHelper.COLUMN_TIMESTAMP, record.getTimestamp());;
		values.put(DatabaseHelper.COLUMN_ANXIETY, record.getAnxiety());
		values.put(DatabaseHelper.COLUMN_SERIOUSNESS, record.getSeriousness());
		values.put(DatabaseHelper.COLUMN_COMMENTS, record.getComments());
		// Query using the values
		boolean result = insert(DatabaseHelper.TABLE_RECORDS, null, values);
		if (result)	{
			return true;
		}	else	{
			Log.w("UserAccount", "UserAccount.addRecord() - failed to add record:  " + result);
			return false;
		}
	}
	
	public List<UserRecord> getRecordByID(int id)	{
		// We must be logged in
		if (!getLoggedIn())	{
			return null;
		}
		
		// Create the results array
		List<UserRecord> results = new ArrayList<UserRecord>();
		
		// Query for all columns of the row with ID
		Cursor myCursor = myDatabase.query(false, DatabaseHelper.TABLE_RECORDS, null, DatabaseHelper.COLUMN_ID + "=" + id, null, null, null, null, null);
		
		if (myCursor.getCount() > 0)	{
			// Move to first result
			myCursor.moveToFirst();
			// If there was a result
			while (true)	{			
				// Get date
				long recordDate = myCursor.getLong(myCursor.getColumnIndex(DatabaseHelper.COLUMN_TIMESTAMP));
				// Get anxiety
				int recordAnxiety = myCursor.getInt(myCursor.getColumnIndex(DatabaseHelper.COLUMN_ANXIETY));
				// get depression
				int recordSeriousness = myCursor.getInt(myCursor.getColumnIndex(DatabaseHelper.COLUMN_SERIOUSNESS));
				// get comments
				String recordComments = myCursor.getString(myCursor.getColumnIndex(DatabaseHelper.COLUMN_COMMENTS));
				
				// Create a new record using the information just read from database
				UserRecord newRecord = new UserRecord(recordDate, recordAnxiety, recordSeriousness, recordComments);
				
				// Set the record ID as we know it
				newRecord.setId(myCursor.getInt(myCursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
				
				// add it to the results array
				results.add(newRecord);
				
				if (myCursor.isLast())	{
					break;
				}	else	{
					myCursor.moveToNext();
				}
			}
		}
		
		
		return results;
	}
	
	public List<UserRecord> getRecordByTime(long from, long to)	{
		// We must be logged in
		if (!getLoggedIn())	{
			return null;
		}
		
		// Create the results array
		List<UserRecord> results = new ArrayList<UserRecord>();
		
		// Query for all columns of the row with ID
		Cursor myCursor = myDatabase.query(false, DatabaseHelper.TABLE_RECORDS, null, DatabaseHelper.COLUMN_TIMESTAMP + ">" + from + " AND " + DatabaseHelper.COLUMN_TIMESTAMP + "<" + to, null, null, null, null, null);
		
		if (myCursor.getCount() > 0)	{
			// Move to first result
			myCursor.moveToFirst();
			// If there was a result
			while (true)	{			
				// Get date
				long recordDate = myCursor.getLong(myCursor.getColumnIndex(DatabaseHelper.COLUMN_TIMESTAMP));
				// Get anxiety
				int recordAnxiety = myCursor.getInt(myCursor.getColumnIndex(DatabaseHelper.COLUMN_ANXIETY));
				// get depression
				int recordSeriousness = myCursor.getInt(myCursor.getColumnIndex(DatabaseHelper.COLUMN_SERIOUSNESS));
				// get comments
				String recordComments = myCursor.getString(myCursor.getColumnIndex(DatabaseHelper.COLUMN_COMMENTS));
				
				// Create a new record using the information just read from database
				UserRecord newRecord = new UserRecord(recordDate, recordAnxiety, recordSeriousness, recordComments);
				
				// Set the record ID as we know it
				newRecord.setId(myCursor.getInt(myCursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
				
				// add it to the results array
				results.add(newRecord);
				
				if (myCursor.isLast())	{
					break;
				}	else	{
					myCursor.moveToNext();
				}
			}
		}
		
		
		return results;
		
		
	}
	
	private boolean insert(String table, String nullPointer, ContentValues values)	{
		// Begin a transaction
		myDatabase.beginTransactionNonExclusive();
		boolean success = false;
		try {
			// Attempt to make changes
			 myDatabase.insert(table, nullPointer, values);
		     myDatabase.setTransactionSuccessful();
		} finally {
			// Save the changes
			 myDatabase.endTransaction();
			 success = true;
		}
		
		// Return success
		return success;
	}
}


class DatabaseHelper extends SQLiteOpenHelper	{
	// Define basic database information
	private static final String DATABASE_NAME = "mentalHealthApp_Database";
    private static final int DATABASE_VERSION = 5;
    
    // Define tables
    public static final String TABLE_RECORDS = "myRecords";
    
    // Define columns
    public static final String COLUMN_ID= "COLUMN_ID";
	public static final String COLUMN_TIMESTAMP = "COLUMN_TIMESTAMP";
	public static final String COLUMN_ANXIETY = "COLUMN_ANXIETY";
	public static final String COLUMN_SERIOUSNESS = "COLUMN_SERIOUSNESS";
	public static final String COLUMN_COMMENTS = "COLUMN_COMMENTS";

	// Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	@Override
	public void onCreate(SQLiteDatabase database) {
		// Called when a brand new database is created
		
		// Create the records table with its data fields
		database.execSQL("create table " + TABLE_RECORDS + " (" +
							COLUMN_ID + " integer primary key," +
							COLUMN_TIMESTAMP + " integer not null," +
							COLUMN_ANXIETY + " integer not null," +
							COLUMN_SERIOUSNESS + " integer not null, " +
							COLUMN_COMMENTS + " text" +
							");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		// Called when there is an older database present
		// CURRENTLY: No upgrade scripts.. sorry :( 
		
		// Drop the old tables
		database.execSQL("drop table " + TABLE_RECORDS);
		
		// Create the new tables
		onCreate(database);
	}
}