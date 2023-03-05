package com.rohithmovva.myfinances;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FinancesDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "myfinances.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_FINANCES =
            "CREATE TABLE Finances (_id integer primary key autoincrement, " +
                                    "AccountType text not null, " +
                                    "AccountNumber text, " +
                                    "InitialBalance real," +
                                    "CurrentBalance real," +
                                    "PaymentAmount real," +
                                    "InterestRate real," +
                                    "CreatedDate date," +
                                    "ModifiedDate date);";

    public FinancesDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE_FINANCES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.w(FinancesDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS Finances");
        onCreate(db);
    }
}
