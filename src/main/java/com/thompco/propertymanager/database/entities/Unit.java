package com.thompco.propertymanager.database.entities;

import com.thompco.propertymanager.database.Table;
import com.thompco.propertymanager.database.helpers.TableAnnotation;

public class Unit extends Table {
    @TableAnnotation(type = Property.class)
    private Property property;

    public Unit() {
    }

    public Unit(Property property) {
        this.property = property;
    }

    @Override
    public Table copy() {
        return new Unit(property);
    }

    @Override
    public Table newInstance() {
        return new Unit();
    }
}
