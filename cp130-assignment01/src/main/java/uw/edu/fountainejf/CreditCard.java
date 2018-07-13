package uw.edu.fountainejf;

public class CreditCard implements edu.uw.ext.framework.account.CreditCard {
    private String issuer;
    private String type;
    private String holder;
    private String acctNumber;
    private String expDate;
    @Override
    public String getIssuer() {
        return issuer;
    }

    @Override
    public void setIssuer(String s) {
        issuer = s;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String s) {
        type = s;
    }

    @Override
    public String getHolder() {
        return holder;
    }

    @Override
    public void setHolder(String s) {
        holder = s;
    }

    @Override
    public String getAccountNumber() {
        return acctNumber;
    }

    @Override
    public void setAccountNumber(String s) {
        acctNumber = s;
    }

    @Override
    public String getExpirationDate() {
        return expDate;
    }

    @Override
    public void setExpirationDate(String s) {
        expDate = s;
    }
}
