package com.rohithmovva.myfinances;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class FinancesDataSource {
    private SQLiteDatabase database;
    private FinancesDBHelper dbHelper;

    public FinancesDataSource(Context context){
        dbHelper = new FinancesDBHelper(context);
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public boolean insertFinance(Finance f){
        boolean didSucceed = false;
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ContentValues initialValues = new ContentValues();

            initialValues.put("AccountType", f.getAccountType());
            initialValues.put("AccountNumber", f.getAccountNumber());
            initialValues.put("InitialBalance", f.getInitialBalance());
            initialValues.put("CurrentBalance", f.getCurrentBalance());
            initialValues.put("PaymentAmount", f.getPaymentAmount());
            initialValues.put("InterestRate", f.getInterestRate());
            initialValues.put("CreatedDate", dateFormat.format(new Date()));

            didSucceed = database.insert("Finances", null, initialValues) > 0;
        }
        catch(Exception ex){
            System.out.println(Arrays.toString(ex.getStackTrace()));
        }
        return didSucceed;
    }

    public boolean updateFinance(Finance f){
        boolean didSucceed = false;
        try{
            Long rowId = (long) f.getFinanceId();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ContentValues updateValues = new ContentValues();

            updateValues.put("AccountType", f.getAccountType());
            updateValues.put("AccountNumber", f.getAccountNumber());
            updateValues.put("InitialBalance", f.getInitialBalance());
            updateValues.put("CurrentBalance", f.getCurrentBalance());
            updateValues.put("PaymentAmount", f.getPaymentAmount());
            updateValues.put("InterestRate", f.getInterestRate());
            updateValues.put("ModifiedDate", dateFormat.format(new Date()));

            didSucceed = database.update("Finances", updateValues, "_id=" + rowId, null) > 0;
        }
        catch(Exception ex){
            System.out.println(Arrays.toString(ex.getStackTrace()));
        }
        return didSucceed;
    }

    public Finance getFinanceData(String accountType){
        Cursor financeCursor = database.rawQuery("SELECT * FROM Finances WHERE AccountType = '" + accountType +"' LIMIT 1", null);

        Finance financeData= new Finance();
        if(financeCursor != null && financeCursor.getCount()> 0 && financeCursor.moveToFirst()){
            do{
                financeData.setFinanceId(financeCursor.getInt(0));
                financeData.setAccountType(financeCursor.getString(1));
                financeData.setAccountNumber(financeCursor.getString(2));
                financeData.setInitialBalance(financeCursor.getDouble(3));
                financeData.setCurrentBalance(financeCursor.getDouble(4));
                financeData.setPaymentAmount(financeCursor.getDouble(5));
                financeData.setInterestRate(financeCursor.getDouble(6));
            }while(financeCursor.moveToNext());
        }
        financeCursor.close();
        return financeData;
    }
}
