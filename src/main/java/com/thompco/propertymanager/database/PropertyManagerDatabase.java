package com.thompco.propertymanager.database;

import com.thompco.propertymanager.database.entities.*;

import java.sql.SQLException;

public class PropertyManagerDatabase extends Database {

    public PropertyManagerDatabase(String filename) {

        super(new Table[]{
                        new Role(),
                        new PhoneNumber(),
                        new Address(),
                        new Zipcode(),
                        new Person(),
                        new Property(),
                        new Unit(),
                        new Person()},
                filename);
    }

    public void initialize() throws SQLException, ClassNotFoundException, IllegalAccessException {
        super.initialize();
        Role.populateData(this);
    }
}
