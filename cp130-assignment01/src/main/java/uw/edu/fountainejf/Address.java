package uw.edu.fountainejf;

public class Address implements edu.uw.ext.framework.account.Address {

    private String street;
    private String city;
    private String state;
    private String zip;
    @Override
    public String getStreetAddress() {
        return street;
    }

    @Override
    public void setStreetAddress(String s) {
        street = s;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public void setCity(String s) {
        city = s;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public void setState(String s) {
        state = s;
    }

    @Override
    public String getZipCode() {
        return zip;
    }

    @Override
    public void setZipCode(String s) {
        zip = s;
    }
}
