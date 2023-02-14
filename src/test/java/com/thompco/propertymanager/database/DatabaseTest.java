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

import org.junit.Test;

/**
 * @author Jordan Thompson <Jordan@ThompCo.com>
 */
public class DatabaseTest {

    @Test
    public void testCleanSql() {
        String sql = "CREATE TABLE IF NOT EXISTS PersonType (typeString String , id INTEGER PRIMARY KEY NOT NULL)";
        String cleanedSql = Database.cleanSql(sql);
    }
}
