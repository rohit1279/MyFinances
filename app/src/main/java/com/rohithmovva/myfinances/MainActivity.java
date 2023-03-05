package com.rohithmovva.myfinances;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Button;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    RadioGroup rgAccountType;

    RadioButton rbCDs, rbLoans, rbCheckingAccounts;

    EditText etAccountNumber, etInitialBalance, etCurrentBalance, etPaymentAmount, etInterestRate;

    Button btnSave, btnCancel;

    Double initialBalance, currentBalance, paymentAmount, interestRate;

    final NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "US"));

    final DecimalFormat df = new DecimalFormat("##.##%");
    private Finance currentFinance = new Finance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rgAccountType = findViewById(R.id.rgAccountType);

        rbCDs = findViewById(R.id.rbCDs);
        rbLoans = findViewById(R.id.rbLoans);
        rbCheckingAccounts = findViewById(R.id.rbCheckingAccounts);

        etAccountNumber = findViewById(R.id.etAccountNumber);
        etInitialBalance = findViewById(R.id.etInitialBalance);
        etCurrentBalance = findViewById(R.id.etCurrentBalance);
        etPaymentAmount = findViewById(R.id.etPaymentAmount);
        etInterestRate = findViewById(R.id.etInterestRate);

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        rgAccountType.setOnCheckedChangeListener((radioGroup, i) -> {
            FinancesDataSource ds = new FinancesDataSource(MainActivity.this);
            try {
                ds.open();

                etAccountNumber.setEnabled(false);
                etInitialBalance.setEnabled(false);
                etCurrentBalance.setEnabled(false);
                etPaymentAmount.setEnabled(false);
                etInterestRate.setEnabled(false);

                ClearEditTextControls();

                if (rbCDs.isChecked()) {
                    currentFinance = ds.getFinanceData("CDs");

                    if(currentFinance.getFinanceId() != -1){
                        etAccountNumber.setText(currentFinance.getAccountNumber());
                        etInitialBalance.setText(formatter.format(currentFinance.getInitialBalance()));
                        etCurrentBalance.setText(formatter.format(currentFinance.getCurrentBalance()));
                        etInterestRate.setText(df.format(currentFinance.getInterestRate()/100));
                    }

                    etAccountNumber.setEnabled(true);
                    etInitialBalance.setEnabled(true);
                    etCurrentBalance.setEnabled(true);
                    etInterestRate.setEnabled(true);
                } else if (rbLoans.isChecked()) {
                    currentFinance = ds.getFinanceData("Loans");

                    if(currentFinance.getFinanceId() != -1){
                        etAccountNumber.setText(currentFinance.getAccountNumber());
                        etInitialBalance.setText(formatter.format(currentFinance.getInitialBalance()));
                        etCurrentBalance.setText(formatter.format(currentFinance.getCurrentBalance()));
                        etPaymentAmount.setText(formatter.format(currentFinance.getPaymentAmount()));
                        etInterestRate.setText(df.format(currentFinance.getInterestRate()/100));
                    }

                    etAccountNumber.setEnabled(true);
                    etInitialBalance.setEnabled(true);
                    etCurrentBalance.setEnabled(true);
                    etPaymentAmount.setEnabled(true);
                    etInterestRate.setEnabled(true);
                }else if(rbCheckingAccounts.isChecked()) {
                    currentFinance = ds.getFinanceData("Checking Accounts");

                    if(currentFinance.getFinanceId() != -1){
                        etAccountNumber.setText(currentFinance.getAccountNumber());
                        etCurrentBalance.setText(formatter.format(currentFinance.getCurrentBalance()));
                    }

                    etAccountNumber.setEnabled(true);
                    etCurrentBalance.setEnabled(true);
                }else{
                    currentFinance = new Finance();
                }

                ds.close();
            } catch (Exception ex) {
                System.out.println(Arrays.toString(ex.getStackTrace()));
            }
        });

        btnSave.setOnClickListener(view -> {
            boolean wasSuccessful = false;
            FinancesDataSource ds = new FinancesDataSource(MainActivity.this);
            try{
                ds.open();
                if (rbCDs.isChecked() || rbLoans.isChecked() || rbCheckingAccounts.isChecked()) {
                    boolean isValidDataEntered = CheckEnteredValues();
                    if (isValidDataEntered) {
                        initialBalance = etInitialBalance.length() > 0 ? GetFormattedTwoDecimalValue(Double.parseDouble(etInitialBalance.getText().toString().replace("$","").replace(",",""))) : null;
                        currentBalance = etCurrentBalance.length() > 0 ? GetFormattedTwoDecimalValue(Double.parseDouble(etCurrentBalance.getText().toString().replace("$","").replace(",",""))) : null;
                        paymentAmount = etPaymentAmount.length() > 0 ? GetFormattedTwoDecimalValue(Double.parseDouble(etPaymentAmount.getText().toString().replace("$","").replace(",",""))) : null;
                        interestRate = etInterestRate.length() > 0 ? GetFormattedTwoDecimalValue(Double.parseDouble(etInterestRate.getText().toString().replace("%","").replace(",",""))) : null;

                        if (rbCDs.isChecked()) {
                            currentFinance.setAccountType("CDs");
                            currentFinance.setAccountNumber(etAccountNumber.getText().toString());
                            currentFinance.setInitialBalance(initialBalance);
                            currentFinance.setCurrentBalance(currentBalance);
                            currentFinance.setInterestRate(interestRate);
                        } else if (rbLoans.isChecked()) {
                            currentFinance.setAccountType("Loans");
                            currentFinance.setAccountNumber(etAccountNumber.getText().toString());
                            currentFinance.setInitialBalance(initialBalance);
                            currentFinance.setCurrentBalance(currentBalance);
                            currentFinance.setPaymentAmount(paymentAmount);
                            currentFinance.setInterestRate(interestRate);
                        } else if (rbCheckingAccounts.isChecked()) {
                            currentFinance.setAccountType("Checking Accounts");
                            currentFinance.setAccountNumber(etAccountNumber.getText().toString());
                            currentFinance.setCurrentBalance(currentBalance);
                        }


                        if (currentFinance.getFinanceId() == -1) {
                            wasSuccessful = ds.insertFinance(currentFinance);
                        } else {
                            wasSuccessful = ds.updateFinance(currentFinance);
                        }
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Please select Account Type", Toast.LENGTH_SHORT).show();
                }
                ds.close();
            }catch(Exception ex)
            {
                wasSuccessful = false;
                System.out.println(Arrays.toString(ex.getStackTrace()));
            }

            if(wasSuccessful){
                Toast.makeText(getApplicationContext(), "Data saved successfully", Toast.LENGTH_SHORT).show();
                rgAccountType.clearCheck();
                ClearEditTextControls();
            }
        });

        btnCancel.setOnClickListener(view -> {
            rgAccountType.clearCheck();
            ClearEditTextControls();
        });
    }

    private void ClearEditTextControls(){
        etAccountNumber.getText().clear();
        etInitialBalance.getText().clear();
        etCurrentBalance.getText().clear();
        etPaymentAmount.getText().clear();
        etInterestRate.getText().clear();
    }

    private boolean CheckEnteredValues(){
        if(etAccountNumber.isEnabled() && etAccountNumber.length() == 0){
            etAccountNumber.setError("Enter valid account number");
            return false;
        }

        if(etInitialBalance.isEnabled() && (etInitialBalance.length() == 0 || etInitialBalance.getText().toString().equals("."))){
            etInitialBalance.setError("Enter valid initial balance");
            return false;
        }

        if(etCurrentBalance.isEnabled() && (etCurrentBalance.length() == 0 || etCurrentBalance.getText().toString().equals("."))){
            etCurrentBalance.setError("Enter valid current balance");
            return false;
        }

        if(etPaymentAmount.isEnabled() && (etPaymentAmount.length() == 0 || etPaymentAmount.getText().toString().equals("."))){
            etPaymentAmount.setError("Enter valid payment amount");
            return false;
        }

        if(etInterestRate.isEnabled() && (etInterestRate.length() == 0 || etInterestRate.getText().toString().equals("."))){
            etInterestRate.setError("Enter valid interest rate");
            return false;
        }

        return true;
    }

    private double GetFormattedTwoDecimalValue(double doubleValue)
    {
        doubleValue = doubleValue * 100;
        doubleValue = Math.round(doubleValue);
        doubleValue = doubleValue/100;
        return doubleValue;
    }
}