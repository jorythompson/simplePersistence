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

import com.thompco.propertymanager.database.Database;
import com.thompco.propertymanager.database.Table;
import com.thompco.propertymanager.database.helpers.TableAnnotation;

import java.sql.SQLException;

/**
 * @author Jordan Thompson <Jordan@ThompCo.com>
 */
public class Role extends Table {

    @TableAnnotation(type = Role.class)
    private final String role;

    public Role() {
        role = "";
    }

    public Role(String role) {
        this.role = role;
    }

    public static void populateData(Database database) throws IllegalAccessException, SQLException {
        database.add(new Role("Tenant"));
        database.add(new Role("Manager"));
        database.add(new Role("Handyman"));
        database.add(new Role("Owner"));
        database.add(new Role("Other"));
    }

    @Override
    public Role copy() {
        return new Role(this.role);
    }

    @Override
    public Role newInstance() {
        return new Role();
    }

    @Override
    public String toString() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Role t)) {
            return false;
        }

        return t.role.equals(role);
    }
}
