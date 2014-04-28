package com.team5.user;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

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
	private boolean doSampleData = true;


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
        	
//        	long current = new java.util.Date().getTime();
//            addRecord(new UserRecord(current, 1, 10, "today"));
//            addRecord(new UserRecord(current - (60*60*24*1), 1, 10, "1 day ago"));
//            addRecord(new UserRecord(current - (60*60*24*2), 10, 9, "2 days ago"));
//            addRecord(new UserRecord(current - (60*60*24*3), 2, 8, "3 days ago"));
//            addRecord(new UserRecord(current - (60*60*24*4), 8, 7, "4 days ago"));
//            addRecord(new UserRecord(current - (60*60*24*5), 3, 6, "5 days ago"));
//            addRecord(new UserRecord(current - (60*60*24*6), 7, 5, "6 days ago"));
//            addRecord(new UserRecord(current - (60*60*24*7), 4, 4, "7 days ago"));
//            addRecord(new UserRecord(current - (60*60*24*8), 6, 3, "8 days ago"));
//            addRecord(new UserRecord(current - (60*60*24*9), 5, 2, "9 days ago"));
//            addRecord(new UserRecord(current - (60*60*24*10), 5, 1, "10 days ago"));

        	if (doSampleData ) {
            Calendar c1 = GregorianCalendar.getInstance();
            c1.setFirstDayOfWeek(Calendar.MONDAY);

            c1.set(Calendar.DAY_OF_MONTH, 1);
            addRecord(new UserRecord(c1.getTimeInMillis(), 1, 10, "She literature discovered increasing how diminution understood. Though and highly the enough county for man. Of it up he still court alone widow seems"));

            Calendar c2 = GregorianCalendar.getInstance();
            c2.setFirstDayOfWeek(Calendar.MONDAY);

            c2.set(Calendar.DAY_OF_MONTH, 1);
            addRecord(new UserRecord(c2.getTimeInMillis(), 7, 4, "Conveying or northward offending admitting perfectly my. Colonel gravity get thought fat smiling add but. Wonder twenty hunted and put income set desire expect. Am cottage calling my is mistake cousins talking up. Interested"));

            Calendar c3 = GregorianCalendar.getInstance();
            c3.setFirstDayOfWeek(Calendar.MONDAY);

            c3.set(Calendar.DAY_OF_MONTH, 3);
            addRecord(new UserRecord(c3.getTimeInMillis(), 3, 4, "ested expression he my at. Respect invited request charmed me warrant to. Expect no pretty as do though so genius afraid cousin. Girl when o"));

            Calendar c4 = GregorianCalendar.getInstance();
            c4.setFirstDayOfWeek(Calendar.MONDAY);

            c4.set(Calendar.DAY_OF_MONTH, 3);
            addRecord(new UserRecord(c4.getTimeInMillis(), 1, 1, "ested expression he my at. Respect invited request charmed me warrant to. Expect no pretty as do though so genius afraid cousin. Girl when o"));

            Calendar c5 = GregorianCalendar.getInstance();
            c5.setFirstDayOfWeek(Calendar.MONDAY);

            c5.set(Calendar.DAY_OF_MONTH, 5);
            addRecord(new UserRecord(c5.getTimeInMillis(), 1, 4, "ested expression he my at. Respect invited request charmed me warrant to. Expect no pretty as do though so genius afraid cousin. Girl when o"));

            Calendar c6 = GregorianCalendar.getInstance();
            c6.setFirstDayOfWeek(Calendar.MONDAY);
            c6.set(Calendar.DAY_OF_MONTH, 9);
            addRecord(new UserRecord(c6.getTimeInMillis(), 3, 4, "red increasing how diminution understood. Though and highly the enough county for man. "));

            Calendar c7 = GregorianCalendar.getInstance();
            c7.setFirstDayOfWeek(Calendar.MONDAY);

            c7.set(Calendar.DAY_OF_MONTH, 12);
            addRecord(new UserRecord(c7.getTimeInMillis(), 9, 4, "red increasing how diminution understood. Though and highly the enough county for man. "));

            Calendar c8 = GregorianCalendar.getInstance();
            c8.setFirstDayOfWeek(Calendar.MONDAY);

            c8.set(Calendar.DAY_OF_MONTH, 14);
            addRecord(new UserRecord(c8.getTimeInMillis(), 4, 8, "red increasing how diminution understood. Though and highly the enough county for man. "));

            Calendar c9 = GregorianCalendar.getInstance();
            c9.setFirstDayOfWeek(Calendar.MONDAY);

            c9.set(Calendar.DAY_OF_MONTH, 18);
            addRecord(new UserRecord(c9.getTimeInMillis(), 1, 10, "red increasing how diminution understood. Though and highly the enough county for man. "));

            Calendar c10 = GregorianCalendar.getInstance();
            c10.setFirstDayOfWeek(Calendar.MONDAY);

            c10.set(Calendar.DAY_OF_MONTH, 18);
            addRecord(new UserRecord(c10.getTimeInMillis(), 9, 4, "red increasing how diminution understood. Though and highly the enough county for man. "));
            Calendar c1gg = GregorianCalendar.getInstance();
            c10.setFirstDayOfWeek(Calendar.MONDAY);

            c10.set(Calendar.DAY_OF_MONTH, 19);
            addRecord(new UserRecord(c10.getTimeInMillis(), 0, 0, " admitting perfectly my. Colonel gravity get thought fat smiling add but. Wonder twenty hunted and put income set desire expect"));

            Calendar c11 = GregorianCalendar.getInstance();
            c11.setFirstDayOfWeek(Calendar.MONDAY);

            c11.set(Calendar.DAY_OF_MONTH, 20);
            addRecord(new UserRecord(c11.getTimeInMillis(), 0, 0, " admitting perfectly my. Colonel gravity get thought fat smiling add but. Wonder twenty hunted and put income set desire expect"));
            Calendar cafsd = GregorianCalendar.getInstance();
            c10.setFirstDayOfWeek(Calendar.MONDAY);

            c10.set(Calendar.DAY_OF_MONTH, 21);
            addRecord(new UserRecord(c10.getTimeInMillis(), 0, 0, " admitting perfectly my. Colonel gravity get thought fat smiling add but. Wonder twenty hunted and put income set desire expect"));

            Calendar cgdsgdf = GregorianCalendar.getInstance();
            c10.setFirstDayOfWeek(Calendar.MONDAY);

            c10.set(Calendar.DAY_OF_MONTH, 22);
            addRecord(new UserRecord(c10.getTimeInMillis(), 0, 0, " admitting perfectly my. Colonel gravity get thought fat smiling add but. Wonder twenty hunted and put income set desire expect"));

            Calendar c12 = GregorianCalendar.getInstance();
            c12.setFirstDayOfWeek(Calendar.MONDAY);

            c12.set(Calendar.DAY_OF_MONTH, 23);
            addRecord(new UserRecord(c12.getTimeInMillis(), 1, 1, " admitting perfectly my. Colonel gravity get thought fat smiling add but. Wonder twenty hunted and put income set desire expect"));
            Calendar c13 = GregorianCalendar.getInstance();
            c13.set(Calendar.DAY_OF_MONTH, 23);
            c13.setFirstDayOfWeek(Calendar.MONDAY);

            addRecord(new UserRecord(c13.getTimeInMillis(), 9, 4, " admitting perfectly my. Colonel gravity get thought fat smiling add but. Wonder twenty hunted and put income set desire expect"));

            Calendar c14 = GregorianCalendar.getInstance();
            c14.setFirstDayOfWeek(Calendar.MONDAY);

            c14.set(Calendar.DAY_OF_MONTH, 24);
            addRecord(new UserRecord(c14.getTimeInMillis(), 4, 8, " admitting perfectly my. Colonel gravity get thought fat smiling add but. Wonder twenty hunted and put income set desire expect"));

            Calendar c15 = GregorianCalendar.getInstance();
            c15.setFirstDayOfWeek(Calendar.MONDAY);

            c15.set(Calendar.DAY_OF_MONTH, 28);
            addRecord(new UserRecord(c15.getTimeInMillis(), 1, 10, " admitting perfectly my. Colonel gravity get thought fat smiling add but. Wonder twenty hunted and put income set desire expect"));

            Calendar c16 = GregorianCalendar.getInstance();
            c16.setFirstDayOfWeek(Calendar.MONDAY);

            c16.set(Calendar.DAY_OF_MONTH, 29);
            addRecord(new UserRecord(c16.getTimeInMillis(), 7, 4, "today"));

            Calendar c17 = GregorianCalendar.getInstance();
            c17.setFirstDayOfWeek(Calendar.MONDAY);

            c17.set(Calendar.DAY_OF_MONTH, 29);
            addRecord(new UserRecord(c17.getTimeInMillis(), 3, 4, "today"));

            Calendar c18 = GregorianCalendar.getInstance();
            c18.setFirstDayOfWeek(Calendar.MONDAY);

            c18.set(Calendar.DAY_OF_MONTH, 30);
            addRecord(new UserRecord(c18.getTimeInMillis(), 1, 1, "today"));
            Calendar c19 = GregorianCalendar.getInstance();
            
            //april
            Calendar ap = GregorianCalendar.getInstance();
            ap.setFirstDayOfWeek(Calendar.MONDAY);

            ap.set(Calendar.DAY_OF_MONTH, 1);
            ap.set(Calendar.MONTH, 4);
            addRecord(new UserRecord(ap.getTimeInMillis(), 1, 10, "She literature discovered increasing how diminution understood. Though and highly the enough county for man. Of it up he still court alone widow seems"));

            Calendar ap2 = GregorianCalendar.getInstance();
            ap2.setFirstDayOfWeek(Calendar.MONDAY);
            ap2.set(Calendar.MONTH, 4);

            ap2.set(Calendar.DAY_OF_MONTH, 1);
            addRecord(new UserRecord(ap2.getTimeInMillis(), 7, 4, "Conveying or northward offending admitting perfectly my. Colonel gravity get thought fat smiling add but. Wonder twenty hunted and put income set desire expect. Am cottage calling my is mistake cousins talking up. Interested"));

            Calendar c31 = GregorianCalendar.getInstance();
            c31.setFirstDayOfWeek(Calendar.MONDAY);
            c31.set(Calendar.MONTH, 4);

            c31.set(Calendar.DAY_OF_MONTH, 3);
            addRecord(new UserRecord(c31.getTimeInMillis(), 3, 4, "ested expression he my at. Respect invited request charmed me warrant to. Expect no pretty as do though so genius afraid cousin. Girl when o"));

            Calendar c41 = GregorianCalendar.getInstance();
            c41.setFirstDayOfWeek(Calendar.MONDAY);
            c41.set(Calendar.MONTH, 4);

            c41.set(Calendar.DAY_OF_MONTH, 3);
            addRecord(new UserRecord(c41.getTimeInMillis(), 1, 1, "ested expression he my at. Respect invited request charmed me warrant to. Expect no pretty as do though so genius afraid cousin. Girl when o"));

            Calendar c51 = GregorianCalendar.getInstance();
            c51.setFirstDayOfWeek(Calendar.MONDAY);
            c51.set(Calendar.MONTH, 4);

            c51.set(Calendar.DAY_OF_MONTH, 5);
            addRecord(new UserRecord(c51.getTimeInMillis(), 1, 4, "ested expression he my at. Respect invited request charmed me warrant to. Expect no pretty as do though so genius afraid cousin. Girl when o"));

        }

        }

    }
    public List<UserRecord> getRecordByMonthAverage(long from, long to)	{
        // We must be logged in
        if (!getLoggedIn())	{
            return null;
        }

        // Create the results array
        List<UserRecord> results = new ArrayList<UserRecord>();

        // Query for all columns of the row with ID
        Cursor myCursor = myDatabase.query(false, DatabaseHelper.TABLE_RECORDS, null, DatabaseHelper.COLUMN_TIMESTAMP + ">=" + from + " AND " + DatabaseHelper.COLUMN_TIMESTAMP + "<=" + to, null, null, null, null, null);

        if (myCursor.getCount() > 0)	{
            // Move to first result
            myCursor.moveToFirst();
            // If there was a result
            Calendar c=Calendar.getInstance();

            Calendar c2 = Calendar.getInstance();
//            c2.setFirstDayOfWeek(Calendar.MONDAY);



            int size= 0;
            int counter=0;
            while (true)	{
                c.setTimeInMillis(myCursor.getLong(myCursor.getColumnIndex(DatabaseHelper.COLUMN_TIMESTAMP)));
                int year = c.get(c.YEAR);
                int week = c.get(c.WEEK_OF_YEAR);
                int day = c.get(c.DAY_OF_WEEK);
                int day2=0;

                int year2=0;
                int week2=0;
                size = results.size()-1;
                if(!results.isEmpty()){
                    c2.setTimeInMillis(results.get(size).getTimestamp());
                    day2 =  c2.get(c.DAY_OF_WEEK);
                    year2=c2.get(c2.YEAR);
                    week2=c2.get(c2.WEEK_OF_YEAR);

                }

                if(!results.isEmpty() && year==year2 && week==week2){
                    counter++;
                    results.get(size).setAnxiety(results.get(size).getAnxiety()+myCursor.getInt(myCursor.getColumnIndex(DatabaseHelper.COLUMN_ANXIETY)));
                    results.get(size).setSeriousness(results.get(size).getSeriousness()+myCursor.getInt(myCursor.getColumnIndex(DatabaseHelper.COLUMN_SERIOUSNESS)));

                }
                else{
                    if(!results.isEmpty()){
                        results.get(size).setAnxiety(results.get(size).getAnxiety()/counter);
                        results.get(size).setSeriousness(results.get(size).getSeriousness()/counter);

                    }
                    counter =1;

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
                }

                if (myCursor.isLast())	{
                    if(!results.isEmpty()){
                        results.get(size).setAnxiety(results.get(size).getAnxiety()/counter);
                        results.get(size).setSeriousness(results.get(size).getSeriousness()/counter);

                    }
                    break;
                }	else	{
                    myCursor.moveToNext();
                }
            }
        }


        return results;


    }


    public List<UserRecord> getRecordByYearAverage(long from, long to)	{
        // We must be logged in
        if (!getLoggedIn())	{
            return null;
        }

        // Create the results array
        List<UserRecord> results = new ArrayList<UserRecord>();

        // Query for all columns of the row with ID
        Cursor myCursor = myDatabase.query(false, DatabaseHelper.TABLE_RECORDS, null, DatabaseHelper.COLUMN_TIMESTAMP + ">=" + from + " AND " + DatabaseHelper.COLUMN_TIMESTAMP + "<=" + to, null, null, null, null, null);

        if (myCursor.getCount() > 0)	{
            // Move to first result
            myCursor.moveToFirst();
            // If there was a result
            Date d1 = new Date();
            Date d2 = new Date();
            int size= 0;
            int counter=0;
            while (true)	{
                d1.setTime(myCursor.getLong(myCursor.getColumnIndex(DatabaseHelper.COLUMN_TIMESTAMP)));
                size = results.size()-1;
                if(!results.isEmpty()){
                    d2.setTime(results.get(size).getTimestamp());
                }

                if(!results.isEmpty() && d1.getMonth() == d2.getMonth()&&d1.getYear()==d2.getYear()){
                    counter++;
                    results.get(size).setAnxiety(results.get(size).getAnxiety()+myCursor.getInt(myCursor.getColumnIndex(DatabaseHelper.COLUMN_ANXIETY)));
                    results.get(size).setSeriousness(results.get(size).getSeriousness()+myCursor.getInt(myCursor.getColumnIndex(DatabaseHelper.COLUMN_SERIOUSNESS)));

                }
                else{
                    if(!results.isEmpty()){
                        results.get(size).setAnxiety(results.get(size).getAnxiety()/counter);
                        results.get(size).setSeriousness(results.get(size).getSeriousness()/counter);

                    }
                    counter =1;

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
                }

                if (myCursor.isLast())	{
                    if(!results.isEmpty()){
                        results.get(size).setAnxiety(results.get(size).getAnxiety()/counter);
                        results.get(size).setSeriousness(results.get(size).getSeriousness()/counter);

                    }
                    break;
                }	else	{
                    myCursor.moveToNext();
                }
            }
        }


        return results;


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
//     SELECT AVG(COLUMN_ANXIETY), AVG(COLUMN_SERIOUSNESS) FROM TABLE_RECORDS WHERE (COLUMN_TIMESTAMP >= timeStart) AND (COLUMN_TIMESTAMP <= timeStart);



    public List<UserRecord> getRecordByDayAverage(long from, long to)	{
        // We must be logged in
        if (!getLoggedIn())	{
            return null;
        }


        // Create the results array
        List<UserRecord> results = new ArrayList<UserRecord>();

        // Query for all columns of the row with ID
        Cursor myCursor = myDatabase.query(false, DatabaseHelper.TABLE_RECORDS, null, DatabaseHelper.COLUMN_TIMESTAMP + ">=" + from + " AND " + DatabaseHelper.COLUMN_TIMESTAMP + "<=" + to, null, null, null, null, null);
        if (myCursor.getCount() > 0)	{
            // Move to first result
            myCursor.moveToFirst();
            Date d1 = new Date();
            Date d2 = new Date();


            // If there was a result
            int counter=0;
            while (true)	{
                d1.setTime(myCursor.getLong(myCursor.getColumnIndex(DatabaseHelper.COLUMN_TIMESTAMP)));
                int size= results.size()-1;
                //if there is something in the list
                if(!results.isEmpty()){
                d2.setTime(results.get(size).getTimestamp());
                }
                if(!results.isEmpty() && d1.getDay() == d2.getDay() && d1.getMonth()==d2.getMonth()){
                    counter++;
                    results.get(size).setAnxiety(results.get(size).getAnxiety()+myCursor.getInt(myCursor.getColumnIndex(DatabaseHelper.COLUMN_ANXIETY)));
                    results.get(size).setSeriousness(results.get(size).getSeriousness()+myCursor.getInt(myCursor.getColumnIndex(DatabaseHelper.COLUMN_SERIOUSNESS)));

                }
                else{
                // Get date
                    if(!results.isEmpty()){
                        results.get(size).setAnxiety(results.get(size).getAnxiety()/counter);
                        results.get(size).setSeriousness(results.get(size).getSeriousness()/counter);

                    }
                    counter=1;
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

                }

                if (myCursor.isLast())	{
                    size=results.size()-1;
                    if(!results.isEmpty()){
                        results.get(size).setAnxiety(results.get(size).getAnxiety()/counter);
                        results.get(size).setSeriousness(results.get(size).getSeriousness()/counter);

                    }
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
    private static final int DATABASE_VERSION = 62;
    
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