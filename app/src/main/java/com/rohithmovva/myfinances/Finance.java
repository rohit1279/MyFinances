package com.rohithmovva.myfinances;

public class Finance {
    private int financeId;
    private String accountType;
    private String accountNumber;
    private double initialBalance;
    private double currentBalance;
    private double paymentAmount;
    private double interestRate;

    public Finance(){
        financeId = -1;
    }

    public int getFinanceId(){
        return financeId;
    }

    public void setFinanceId(int i){
        financeId = i;
    }

    public String getAccountType(){
        return accountType;
    }

    public void setAccountType(String a){
        accountType = a;
    }

    public String getAccountNumber(){
        return accountNumber;
    }

    public void setAccountNumber(String a){
        accountNumber = a;
    }

    public double getInitialBalance(){
        return initialBalance;
    }

    public void setInitialBalance(double a){
        initialBalance = a;
    }

    public double getCurrentBalance(){
        return currentBalance;
    }

    public void setCurrentBalance(double a){
        currentBalance = a;
    }

    public double getPaymentAmount(){
        return paymentAmount;
    }

    public void setPaymentAmount(double a){
        paymentAmount = a;
    }

    public double getInterestRate(){
        return interestRate;
    }

    public void setInterestRate(double a){
        interestRate = a;
    }
}
