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
public class Person extends Table {
    @TableAnnotation(type = PhoneNumber.class)
    public List<PhoneNumber> phoneNumbers = new ArrayList<>();
    @TableAnnotation(type = String.class)
    private String firstName;
    @TableAnnotation(type = String.class, allowNull = true)
    private String lastName;
    @TableAnnotation(type = String.class, allowNull = true)
    private String emailAddress;
    @TableAnnotation(type = Address.class, allowNull = true)
    private List<Address> addresses = new ArrayList<>();
    @TableAnnotation(type = Role.class)
    private List<Role> roles = new ArrayList<>();

    public Person() {
    }

    public Person(String firstName,
                  String lastName,
                  String emailAddress,
                  PhoneNumber phoneNumber,
                  Address address,
                  Role role) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.addresses.add(address);
        this.emailAddress = emailAddress;
        this.phoneNumbers.add(phoneNumber);
        this.roles.add(role);
    }

    @Override
    public Person copy() {
        Person p = new Person();
        p.firstName = firstName;
        p.lastName = lastName;
        p.emailAddress = emailAddress;
        p.phoneNumbers = new ArrayList<>(phoneNumbers);
        p.addresses = new ArrayList<>(addresses);
        p.roles = new ArrayList<>(roles);
        return p;
    }

    @Override
    public Person newInstance() {
        return new Person();
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + ", " + emailAddress + ", " + phoneNumbers + ", " + addresses + ", " + roles;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Person p)) {
            return false;
        }

        return p.firstName.equals(firstName) &&
                p.lastName.equals(lastName) &&
                p.roles.equals(roles) &&
                p.addresses.equals(addresses) &&
                p.phoneNumbers.equals(phoneNumbers) &&
                p.emailAddress.equals(emailAddress);
    }
}
