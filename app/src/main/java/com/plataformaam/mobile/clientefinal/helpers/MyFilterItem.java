package com.plataformaam.mobile.clientefinal.helpers;

/**
 * Created by bernard on 04/01/2015.
 */
public class MyFilterItem {
    private String property;
    private String value;
    private String operator;

    public MyFilterItem() {
    }

    public MyFilterItem(String property, String value, String operator) {
        super();
        this.property = property;
        this.value = value;
        this.operator = operator;
    }


    /**
     * @return the property
     */
    public String getProperty() {
        return property;
    }

    /**
     * @param property the property to set
     */
    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the operator
     */
    public String getOperator() {
        return operator;
    }

    /**
     * @param operator the operator to set
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }



}
