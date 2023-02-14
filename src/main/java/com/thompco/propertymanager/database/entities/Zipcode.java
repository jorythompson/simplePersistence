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
public class Zipcode extends Table {

    @TableAnnotation(type = String.class)
    private String zipcode = "";

    public Zipcode() {
        //columns.put("myZipcode", new ColumnConstraints(false));
    }

    public Zipcode(int code) throws InvalidZipcodeException {
        super();

        if (code < 1 || code > 99999) {
            throw new InvalidZipcodeException(String.format("%d is not a valid zipcode",
                    code));
        }
        zipcode = String.format("%d", code);
    }

    public Zipcode(String code) throws InvalidZipcodeException {
        this(Integer.parseInt(code));
    }

    @Override
    public String toString() {
        return zipcode;
    }

    @Override
    public Zipcode copy() {
        try {
            return new Zipcode(zipcode);
        } catch (InvalidZipcodeException e) {
            return null;
        }
    }

    @Override
    public Zipcode newInstance() {
        return new Zipcode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Zipcode z)) {
            return false;
        }

        return z.zipcode.equals(zipcode);
    }

    public static class InvalidZipcodeException extends Exception {

        public InvalidZipcodeException(String errorMessage) {
            super(errorMessage);
        }
    }
}
