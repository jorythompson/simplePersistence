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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jordan Thompson <Jordan@ThompCo.com>
 */
public class Property extends Table {

    @TableAnnotation(type = Address.class)
    private Address address;
    @TableAnnotation(type = Integer.class)
    private Integer unitCount;
    @TableAnnotation(type = Double.class)
    private Double taxRate;
    @TableAnnotation(type = Double.class)
    private Double rentIncreasePercent;
    @TableAnnotation(type = Double.class)
    private Double lateFee;
    @TableAnnotation(type = Boolean.class)
    private Boolean rentIncludesTax;
    @TableAnnotation(type = Double.class)
    private Double bouncedCheckFee;
    @TableAnnotation(type = Person.class)
    private List<Person> people;

    public Property(Address address,
                    Integer unitCount,
                    Double taxRate,
                    Double rentIncreasePercent,
                    Double lateFee,
                    Boolean rentIncludesTax,
                    Double bouncedCheckFee,
                    List<Person> people) {
        this.address = address;
        this.unitCount = unitCount;
        this.taxRate = taxRate;
        this.rentIncreasePercent = rentIncreasePercent;
        this.lateFee = lateFee;
        this.rentIncludesTax = rentIncludesTax;
        this.bouncedCheckFee = bouncedCheckFee;
        this.people = people;
    }

    public Property() {

    }

    @Override
    public Property newInstance() {
        return new Property();
    }

    @Override
    public Property copy() {
        return new Property(address.copy(), unitCount, taxRate, rentIncreasePercent, lateFee, rentIncludesTax,
                bouncedCheckFee,
                new ArrayList<>(people));
    }

    @Override
    public String toString() {
        return String.format("%s, %d, %2.2f, %2.2f, $%2.2f, %s, $%2.2f",
                address.toString(), unitCount, taxRate,
                rentIncreasePercent,
                lateFee, rentIncludesTax ? "true" : "false", bouncedCheckFee);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Property p)) {
            return false;
        }

        return p.people.equals(people) &&
                p.rentIncludesTax.equals(rentIncludesTax) &&
                p.lateFee.equals(lateFee) &&
                p.taxRate.equals(taxRate) &&
                p.bouncedCheckFee.equals(bouncedCheckFee) &&
                p.rentIncreasePercent.equals(rentIncreasePercent) &&
                p.unitCount.equals(unitCount) &&
                p.address.equals(address);
    }
}
