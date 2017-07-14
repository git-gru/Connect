
package com.jarrebnnee.connect.Helper.Login;

import java.util.HashMap;
import java.util.Map;

public class Data {

    private String uId;
    private String uFirstName;
    private String uLastName;
    private String uEmail;
    private String uPassword;
    private String uAddress;
    private String uLatitute;
    private String uLongitute;
    private String uCity;
    private String uPhone;
    private String uPostcode;
    private String uCountry;
    private String uStatus;
    private String uType;
    private String uIsNotificationSound;
    private String uCreated;
    private String uModified;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getUId() {
        return uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public String getUFirstName() {
        return uFirstName;
    }

    public void setUFirstName(String uFirstName) {
        this.uFirstName = uFirstName;
    }

    public String getULastName() {
        return uLastName;
    }

    public void setULastName(String uLastName) {
        this.uLastName = uLastName;
    }

    public String getUEmail() {
        return uEmail;
    }

    public void setUEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getUPassword() {
        return uPassword;
    }

    public void setUPassword(String uPassword) {
        this.uPassword = uPassword;
    }

    public String getUAddress() {
        return uAddress;
    }

    public void setUAddress(String uAddress) {
        this.uAddress = uAddress;
    }

    public String getULatitute() {
        return uLatitute;
    }

    public void setULatitute(String uLatitute) {
        this.uLatitute = uLatitute;
    }

    public String getULongitute() {
        return uLongitute;
    }

    public void setULongitute(String uLongitute) {
        this.uLongitute = uLongitute;
    }

    public String getUCity() {
        return uCity;
    }

    public void setUCity(String uCity) {
        this.uCity = uCity;
    }

    public String getUPhone() {
        return uPhone;
    }

    public void setUPhone(String uPhone) {
        this.uPhone = uPhone;
    }

    public String getUPostcode() {
        return uPostcode;
    }

    public void setUPostcode(String uPostcode) {
        this.uPostcode = uPostcode;
    }

    public String getUCountry() {
        return uCountry;
    }

    public void setUCountry(String uCountry) {
        this.uCountry = uCountry;
    }

    public String getUStatus() {
        return uStatus;
    }

    public void setUStatus(String uStatus) {
        this.uStatus = uStatus;
    }

    public String getUType() {
        return uType;
    }

    public void setUType(String uType) {
        this.uType = uType;
    }

    public String getUIsNotificationSound() {
        return uIsNotificationSound;
    }

    public void setUIsNotificationSound(String uIsNotificationSound) {
        this.uIsNotificationSound = uIsNotificationSound;
    }

    public String getUCreated() {
        return uCreated;
    }

    public void setUCreated(String uCreated) {
        this.uCreated = uCreated;
    }

    public String getUModified() {
        return uModified;
    }

    public void setUModified(String uModified) {
        this.uModified = uModified;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
