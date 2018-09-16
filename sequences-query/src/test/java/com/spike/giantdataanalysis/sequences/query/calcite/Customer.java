package com.spike.giantdataanalysis.sequences.query.calcite;

import java.math.BigDecimal;
import java.util.Date;

public class Customer {
  private Integer C_ID;// 1
  private Integer C_D_ID;
  private Integer C_W_ID;
  private String C_FIRST;
  private String C_MIDDLE;// 5
  private String C_LAST;
  private String C_STREET_1;
  private String C_STREET_2;
  private String C_CITY;
  private String C_STATE;// 10
  private String C_ZIP;
  private String C_PHONE;
  private Date C_SINCE;
  private String C_CREDIT;
  private BigDecimal C_CREDIT_LIM;// 15
  private BigDecimal C_DISCOUNT;
  private BigDecimal C_BALANCE;
  private BigDecimal C_YTD_PAYMENT;
  private BigDecimal C_PAYMENT_CNT;
  private BigDecimal C_DELIVERY_CNT;// 20
  private String C_DATA;

  public Customer(Integer c_ID, Integer c_D_ID, Integer c_W_ID, String c_FIRST, String c_MIDDLE,
      String c_LAST, String c_STREET_1, String c_STREET_2, String c_CITY, String c_STATE,
      String c_ZIP, String c_PHONE, Date c_SINCE, String c_CREDIT, BigDecimal c_CREDIT_LIM,
      BigDecimal c_DISCOUNT, BigDecimal c_BALANCE, BigDecimal c_YTD_PAYMENT,
      BigDecimal c_PAYMENT_CNT, BigDecimal c_DELIVERY_CNT, String c_DATA) {
    C_ID = c_ID;
    C_D_ID = c_D_ID;
    C_W_ID = c_W_ID;
    C_FIRST = c_FIRST;
    C_MIDDLE = c_MIDDLE;
    C_LAST = c_LAST;
    C_STREET_1 = c_STREET_1;
    C_STREET_2 = c_STREET_2;
    C_CITY = c_CITY;
    C_STATE = c_STATE;
    C_ZIP = c_ZIP;
    C_PHONE = c_PHONE;
    C_SINCE = c_SINCE;
    C_CREDIT = c_CREDIT;
    C_CREDIT_LIM = c_CREDIT_LIM;
    C_DISCOUNT = c_DISCOUNT;
    C_BALANCE = c_BALANCE;
    C_YTD_PAYMENT = c_YTD_PAYMENT;
    C_PAYMENT_CNT = c_PAYMENT_CNT;
    C_DELIVERY_CNT = c_DELIVERY_CNT;
    C_DATA = c_DATA;
  }

  public Integer getC_ID() {
    return C_ID;
  }

  public void setC_ID(Integer c_ID) {
    C_ID = c_ID;
  }

  public Integer getC_D_ID() {
    return C_D_ID;
  }

  public void setC_D_ID(Integer c_D_ID) {
    C_D_ID = c_D_ID;
  }

  public Integer getC_W_ID() {
    return C_W_ID;
  }

  public void setC_W_ID(Integer c_W_ID) {
    C_W_ID = c_W_ID;
  }

  public String getC_FIRST() {
    return C_FIRST;
  }

  public void setC_FIRST(String c_FIRST) {
    C_FIRST = c_FIRST;
  }

  public String getC_MIDDLE() {
    return C_MIDDLE;
  }

  public void setC_MIDDLE(String c_MIDDLE) {
    C_MIDDLE = c_MIDDLE;
  }

  public String getC_LAST() {
    return C_LAST;
  }

  public void setC_LAST(String c_LAST) {
    C_LAST = c_LAST;
  }

  public String getC_STREET_1() {
    return C_STREET_1;
  }

  public void setC_STREET_1(String c_STREET_1) {
    C_STREET_1 = c_STREET_1;
  }

  public String getC_STREET_2() {
    return C_STREET_2;
  }

  public void setC_STREET_2(String c_STREET_2) {
    C_STREET_2 = c_STREET_2;
  }

  public String getC_CITY() {
    return C_CITY;
  }

  public void setC_CITY(String c_CITY) {
    C_CITY = c_CITY;
  }

  public String getC_STATE() {
    return C_STATE;
  }

  public void setC_STATE(String c_STATE) {
    C_STATE = c_STATE;
  }

  public String getC_ZIP() {
    return C_ZIP;
  }

  public void setC_ZIP(String c_ZIP) {
    C_ZIP = c_ZIP;
  }

  public String getC_PHONE() {
    return C_PHONE;
  }

  public void setC_PHONE(String c_PHONE) {
    C_PHONE = c_PHONE;
  }

  public Date getC_SINCE() {
    return C_SINCE;
  }

  public void setC_SINCE(Date c_SINCE) {
    C_SINCE = c_SINCE;
  }

  public String getC_CREDIT() {
    return C_CREDIT;
  }

  public void setC_CREDIT(String c_CREDIT) {
    C_CREDIT = c_CREDIT;
  }

  public BigDecimal getC_CREDIT_LIM() {
    return C_CREDIT_LIM;
  }

  public void setC_CREDIT_LIM(BigDecimal c_CREDIT_LIM) {
    C_CREDIT_LIM = c_CREDIT_LIM;
  }

  public BigDecimal getC_DISCOUNT() {
    return C_DISCOUNT;
  }

  public void setC_DISCOUNT(BigDecimal c_DISCOUNT) {
    C_DISCOUNT = c_DISCOUNT;
  }

  public BigDecimal getC_BALANCE() {
    return C_BALANCE;
  }

  public void setC_BALANCE(BigDecimal c_BALANCE) {
    C_BALANCE = c_BALANCE;
  }

  public BigDecimal getC_YTD_PAYMENT() {
    return C_YTD_PAYMENT;
  }

  public void setC_YTD_PAYMENT(BigDecimal c_YTD_PAYMENT) {
    C_YTD_PAYMENT = c_YTD_PAYMENT;
  }

  public BigDecimal getC_PAYMENT_CNT() {
    return C_PAYMENT_CNT;
  }

  public void setC_PAYMENT_CNT(BigDecimal c_PAYMENT_CNT) {
    C_PAYMENT_CNT = c_PAYMENT_CNT;
  }

  public BigDecimal getC_DELIVERY_CNT() {
    return C_DELIVERY_CNT;
  }

  public void setC_DELIVERY_CNT(BigDecimal c_DELIVERY_CNT) {
    C_DELIVERY_CNT = c_DELIVERY_CNT;
  }

  public String getC_DATA() {
    return C_DATA;
  }

  public void setC_DATA(String c_DATA) {
    C_DATA = c_DATA;
  }

  @Override
  public String toString() {
    return "Customer [C_ID=" + C_ID + ", C_D_ID=" + C_D_ID + ", C_W_ID=" + C_W_ID + ", C_FIRST="
        + C_FIRST + ", C_MIDDLE=" + C_MIDDLE + ", C_LAST=" + C_LAST + ", C_STREET_1=" + C_STREET_1
        + ", C_STREET_2=" + C_STREET_2 + ", C_CITY=" + C_CITY + ", C_STATE=" + C_STATE + ", C_ZIP="
        + C_ZIP + ", C_PHONE=" + C_PHONE + ", C_SINCE=" + C_SINCE + ", C_CREDIT=" + C_CREDIT
        + ", C_CREDIT_LIM=" + C_CREDIT_LIM + ", C_DISCOUNT=" + C_DISCOUNT + ", C_BALANCE="
        + C_BALANCE + ", C_YTD_PAYMENT=" + C_YTD_PAYMENT + ", C_PAYMENT_CNT=" + C_PAYMENT_CNT
        + ", C_DELIVERY_CNT=" + C_DELIVERY_CNT + ", C_DATA=" + C_DATA + "]";
  }

}