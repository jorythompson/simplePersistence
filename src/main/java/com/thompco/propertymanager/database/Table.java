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
package com.thompco.propertymanager.database;

/**
 * @author Jordan Thompson <Jordan@ThompCo.com>
 */

public abstract class Table {
    /***
     * This is the ID that all derived Tables require.
     */
    protected Long id;

    /***
     * Construct a basic Table that does not have to be complete.  It is only used to call newInstance()
     */
    protected Table() {
    }

    /***
     * Constructs a table from the database.
     * @param id is the ID in the database.
     */
    protected Table(Long id) {
        this.id = id;
    }

    /***
     * This method copies one Table to another.  It should copy all member variables to the object being returned.
     * The developer is responsible for filling it out.
     * @return Table
     */
    public abstract Table copy();

    /***
     * This method creates a new instance of the derived class.  It only needs to return a copy of a derived object:
     * return new DerivedClass();
     * @return new derived object that doesn't have to be complete - its only used to call newInstance()
     */
    public abstract Table newInstance();
}
