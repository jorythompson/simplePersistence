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
public class Address extends Table {

    @TableAnnotation(type = String.class)
    private String streetAddress;
    @TableAnnotation(type = String.class, allowNull = true)
    private String secondStreetAddress;
    @TableAnnotation(type = String.class)
    private String city;
    @TableAnnotation(type = String.class)
    private String state;
    @TableAnnotation(type = Zipcode.class)
    private Zipcode zipcode;

    public Address() {
    }

    public Address(String streetAddress,
                   String secondStreetAddress,
                   String city,
                   String state,
                   Zipcode zipcode) {
        this.streetAddress = streetAddress;
        this.secondStreetAddress = secondStreetAddress;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
    }

    @Override
    public Address copy() {
        return new Address(this.streetAddress, this.secondStreetAddress, this.city, this.state,
                this.zipcode);
    }

    @Override
    public Address newInstance() {
        return new Address();
    }

    @Override
    public String toString() {
        return streetAddress + ", "
                + secondStreetAddress + ", "
                + city + ", "
                + state + " " + zipcode;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Address a)) {
            return false;
        }

        return a.streetAddress.equals(streetAddress) &&
                a.secondStreetAddress.equals(secondStreetAddress) &&
                a.city.equals(city) &&
                a.state.equals(state) &&
                a.zipcode.equals(zipcode);
    }
}
