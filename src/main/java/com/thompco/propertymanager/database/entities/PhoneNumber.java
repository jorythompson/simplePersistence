/* This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version
 *
 * ThompCo disclaims to the fullest extent authorized by law any and all other
 * warranties, whether express or implied, including, without limitation, any
 * implied warranties of title, non-infringement, quiet enjoyment, integration,
 * merchantability or fitness for a particular purpose.
 * You assume responsibility for selecting the software to achieve your
 * intended results, and for the results obtained from your use of the software.
 * You shall bear the entire risk as to the quality and the performance of the
 * software. */
package com.thompco.propertymanager.database.entities;

import com.thompco.propertymanager.database.Table;
import com.thompco.propertymanager.database.helpers.TableAnnotation;

/**
 * @author Jordan Thompson <Jordan@ThompCo.com>
 */
public class PhoneNumber extends Table {

    @TableAnnotation(type = Integer.class)
    private Integer areaCode;
    @TableAnnotation(type = Integer.class)
    private Integer exchange;
    @TableAnnotation(type = Integer.class)
    private Integer extension;
    @TableAnnotation(type = Boolean.class)
    private Boolean isCell;

    public PhoneNumber() {
    }

    public PhoneNumber(int area, int exch, int ext, boolean isCell) throws InvalidPhoneNumberException {
        if (area < 1 || area > 999) {
            throw new InvalidPhoneNumberException(String.format("'%d' is not a valid area code", area));
        }

        if (exch < 1 || exch > 999) {
            throw new InvalidPhoneNumberException(String.format("'%d' is not a valid exchange", area));
        }

        if (ext < 1 || ext > 9999) {
            throw new InvalidPhoneNumberException(String.format("'%d' is not a valid extension", area));
        }

        areaCode = area;
        exchange = exch;
        extension = ext;
        this.isCell = isCell;
    }

    public PhoneNumber(String phoneNumber, boolean isCell) throws InvalidPhoneNumberException {
        String pn = phoneNumber.replaceAll("[^\\d.]", "");

        if (pn.length() == 10) {
            areaCode = Integer.parseInt(pn.substring(0, 3));
            exchange = Integer.parseInt(pn.substring(3, 6));
            extension = Integer.parseInt(pn.substring(6, 10));
            this.isCell = isCell;
        } else {
            throw new InvalidPhoneNumberException(
                    String.format("'%s' is not a valid phone number",
                            phoneNumber));
        }
    }

    @Override
    public String toString() {
        if (areaCode == null || extension == null || exchange == null) {
            return PhoneNumber.class.getSimpleName() + " has not been initiated properly";
        }
        return String.format("(%03d) %03d-%04d %s",
                areaCode, exchange, extension, isCell ? "cell" : "land");
    }

    @Override
    public PhoneNumber copy() {
        try {
            return new PhoneNumber(this.areaCode, this.exchange, this.extension, this.isCell);
        } catch (InvalidPhoneNumberException ignored) {
            return null;
        }
    }

    @Override
    public PhoneNumber newInstance() {
        return new PhoneNumber();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PhoneNumber p)) {
            return false;
        }

        return p.areaCode.equals(areaCode) &&
                p.exchange.equals(exchange) &&
                p.extension.equals(extension) &&
                p.isCell.equals(isCell);
    }

    public static class InvalidPhoneNumberException extends Exception {
        public InvalidPhoneNumberException(String errorMessage) {
            super(errorMessage);
        }
    }
}
