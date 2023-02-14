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

import com.thompco.propertymanager.database.entities.*;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Jordan Thompson <Jordan@ThompCo.com>
 */
public class PropertyManagerDatabaseTest {

    @Test
    public void testDb() throws ClassNotFoundException, SQLException, IllegalAccessException, Zipcode.InvalidZipcodeException, PhoneNumber.InvalidPhoneNumberException, Database.TableClassNotFoundException, NoSuchFieldException {
        PropertyManagerDatabase db = new PropertyManagerDatabase("test");
        db.dropTables();
        db.initialize();

        Person person1 = new Person("John", "Doe", "John@doe.com",
                new PhoneNumber("1234567890", true),
                new Address("787 Loggerhead Island Way",
                        "",
                        "Satellite Beach",
                        "FL",
                        new Zipcode(32937)),
                new Role("New Person A"));
        Long p1 = db.add(person1);
        Person person2 = new Person("Harry", "Jones", "Harry@doe.com",
                new PhoneNumber("0987654321", false),
                new Address("3245 West New Haven Ave",
                        "",
                        "Melbourne",
                        "FL",
                        new Zipcode(32904)),
                new Role("New Person B"));
        Long p2 = db.add(person2);
        Person person3 = (Person) db.getOne(new Person(), p2);
        assertEquals(person2, person3);
        List<Table> persons = db.getAll(new Person());
        for (Table t : persons) {
            System.out.println(t);
        }
    }
}
