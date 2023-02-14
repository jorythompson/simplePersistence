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

import com.thompco.propertymanager.database.helpers.TableAnnotation;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jordan Thompson <Jordan@ThompCo.com>
 */
public class Database {
    private static final String ID_COL = "id";
    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());
    private static Database THE_DATABASE;
    private final Table[] tables;
    String fileName;

    public Database(Table[] tables, String filename) {
        this.tables = tables;
        this.fileName = filename;

        if (THE_DATABASE == null) {
            THE_DATABASE = this;
        }
    }

    public static String cleanSql(String sql) {
        return sql.replace(" String", " VARCHAR(255)").
                replace(",)", ")").
                replace("\n,)", ")").
                replace(",\n)", ")").
                replace(", FROM", " FROM");
    }

    private Connection getConnection() throws SQLException {
        try {
            String url = String.format("jdbc:sqlite:%s.propman", fileName);
            Connection connection = DriverManager.getConnection(url);
            LOGGER.log(Level.DEBUG, "Connection established");
            return connection;
        } catch (Exception ex) {
            LOGGER.log(Level.FATAL, null, ex);
            throw ex;
        }
    }

    public void createTable(Table table) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");

        String create = "CREATE TABLE IF NOT EXISTS " + table.getClass().getSimpleName();
        StringBuilder cols = new StringBuilder();

        // member variables
        for (Field field : table.getClass().getDeclaredFields()) {
            String name = field.getName();
            String colType = field.getType().getSimpleName();
            String allowNull = "";

            if (Table.class.isAssignableFrom(field.getType())) {
                cols.append(name).append(" INTEGER NOT NULL,\n");
            } else if (field.getType() == List.class) {
                String sql = "CREATE TABLE IF NOT EXISTS " + joinTableName(table, field.getName()) + "(" +
                        table.getClass().getSimpleName() + " LONG NOT NULL, " +
                        field.getName() + " LONG NOT NULL)";
                executeSql(sql);
            } else {
                TableAnnotation tableAnnotation = field.getAnnotation(TableAnnotation.class);
                if (!tableAnnotation.allowNull()) {
                    allowNull = "NOT NULL\n";
                }

                cols.append(name).append(" ").append(colType).append(" ").append(allowNull).append(", ");
            }
        }

        cols.append(ID_COL).append(" INTEGER PRIMARY KEY NOT NULL");
        String sql = String.format("%s (%s)", create, cols);
        executeSql(sql);
    }

    public String joinTableName(Table table, String memberName) {
        return table.getClass().getSimpleName() + "_" + memberName;
    }

    public Table getOne(Table table, String where) throws SQLException, IllegalAccessException, TableClassNotFoundException, ClassNotFoundException {
        return get(table, where).get(0);
    }

    public Table getOne(Table table, Long id) throws SQLException, IllegalAccessException, TableClassNotFoundException, ClassNotFoundException {
        return get(table, "id = " + id).get(0);
    }

    public List<Table> getAll(Table table) throws SQLException, IllegalAccessException, TableClassNotFoundException, ClassNotFoundException {
        return get(table, "");
    }

    public List<Table> get(Table table, String where) throws SQLException, IllegalAccessException, TableClassNotFoundException, ClassNotFoundException {
        StringBuilder cols = new StringBuilder();
        List<Table> tables = new ArrayList<>();
        String whereClause = "";

        if (!where.isEmpty()) {
            whereClause = "WHERE " + where;
        }

        for (Field field : table.getClass().getDeclaredFields()) {
            if (field.getAnnotation(TableAnnotation.class) == null) {
                throw (new RuntimeException("\"" + table.getClass().getSimpleName() + "." + field.getName() + "\" is not annotataed."));
            }
            if (field.getType() != List.class) {
                cols.append(field.getName()).append(",");
            }
        }

        cols.append(ID_COL);
        String sql = "SELECT " + cols + " FROM " + table.getClass().getSimpleName() + " " + whereClause;

        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        try {
            ResultSet rs = statement.executeQuery(cleanSql(sql));

            while (rs.next()) {
                Table newTable = table.newInstance();
                tables.add(newTable);
                newTable.id = rs.getLong(ID_COL);

                for (Field field : newTable.getClass().getDeclaredFields()) {
                    String name = field.getName();
                    Class<?> type = field.getType();
                    field.setAccessible(true);
                    Object v = null;

                    if (Table.class.isAssignableFrom(type)) {
                        // Single Table member
                        v = getOne(getTable(name).newInstance(), ID_COL + " = " + rs.getLong(name));
                    } else if (type == Integer.class) {
                        v = rs.getInt(name);
                    } else if (type == Float.class) {
                        v = rs.getFloat(name);
                    } else if (type == Double.class) {
                        v = rs.getDouble(name);
                    } else if (type == Boolean.class) {
                        v = rs.getBoolean(name);
                    } else if (type == String.class) {
                        v = rs.getString(name);
                    } else if (type == List.class) {
                        // List<Table>
                        TableAnnotation tableAnnotation = field.getAnnotation(TableAnnotation.class);
                        List<Table> ts = getMemberTables(newTable, field, connection);
                        List<Table> fld = (List) field.get(newTable);
                        fld.addAll(ts);
                        System.out.println();
                    } else {
                        throw new RuntimeException("Type not handled for column \"" + name + "\" in table \"" + table.getClass().getSimpleName() + "\"");
                    }

                    if (v != null) {
                        field.set(newTable, v);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.FATAL, sql, e);
            throw (e);
        }

        connection.close();
        return tables;
    }

    private ArrayList<Table> getMemberTables(Table table, Field field, Connection connection) throws SQLException, TableClassNotFoundException, IllegalAccessException, ClassNotFoundException {
        ArrayList<Table> tables = new ArrayList<>();
        TableAnnotation tableAnnotation = field.getAnnotation(TableAnnotation.class);
        Table memberTable = getTable(tableAnnotation.type().getSimpleName());
        String sql = "SELECT " +
                table.getClass().getSimpleName() + ", " + field.getName() +
                " FROM " + joinTableName(table, field.getName()) +
                " WHERE " + table.getClass().getSimpleName() + " = " + table.id;
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(cleanSql(sql));


        while (rs.next()) {
            Table t = memberTable.newInstance();
            Long id = rs.getLong(field.getName());
            Table t2 = getOne(t, id);
            tables.add(t2);
        }
        return tables;
    }

    // TODO may consider adding another add that takes an already-open connection and don't close it and put it back the way it was originally
    public long add(Table table) throws IllegalAccessException, SQLException {
        String sql = "INSERT INTO " + table.getClass().getSimpleName();
        StringBuilder cols = new StringBuilder();
        StringBuilder values = new StringBuilder();
        boolean arraysExist = false;

        for (Field field : table.getClass().getDeclaredFields()) {
            if (field.getType() != List.class) {
                cols.append(field.getName()).append(",");
            }
            field.setAccessible(true);

            if (field.getType() == String.class) {
                values.append("\"").append(field.get(table)).append("\",");
            } else if (field.getType() == Double.class) {
                values.append(field.get(table).toString()).append(",");
            } else if (field.getType() == Integer.class) {
                values.append(field.get(table).toString()).append(",");
            } else if (field.getType() == Boolean.class) {
                values.append(field.get(table).toString()).append(",");
            } else if (field.getType() == List.class) {
                arraysExist = true;
            } else if (Table.class.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                Table value = (Table) field.get(table);
                long fieldId = add(value);
                values.append(fieldId).append(",");
            } else {
                throw new RuntimeException("Unable to convert column-value for " +
                        table.getClass().getSimpleName() + "." + field.getName() + " as " + field.getType().getSimpleName());
            }
        }

        sql += " (" + cols + ") VALUES (" + values + ")";
        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        try {
            executeSql(sql);
        } catch (SQLException e) {
            System.out.println(sql);
            throw (e);
        }
        ResultSet rs = statement.executeQuery(
                "SELECT MAX(" + ID_COL + ") AS LAST_ID FROM " + table.getClass().getSimpleName());
        long lastId;
        if (rs.next()) {
            lastId = rs.getLong("LAST_ID");
            table.id = lastId;
        } else {
            lastId = 0;
        }

        statement.close();

        if (arraysExist) {
            for (Field field : table.getClass().getDeclaredFields()) {
                field.setAccessible(true);

                if (field.getType() == List.class && field.get(table) != null) {
                    ArrayList<Table> objects = (ArrayList<Table>) field.get(table);
                    for (Table object : objects) {
                        if (object.id == null) {
                            add(object);
                        }
                    }

                    connection = getConnection();
                    statement = connection.createStatement();
                    connection.setAutoCommit(false);

                    for (Table t : (ArrayList<Table>) field.get(table)) {
                        sql = "INSERT INTO " + joinTableName(table, field.getName()) +
                                " (" + table.getClass().getSimpleName() + ", " + field.getName() + ")" +
                                " VALUES (" + lastId + ", " + t.id + ")";
                        statement.executeUpdate(sql);
                    }
                    connection.commit();
                    connection.close();
                }
            }
        }

        return lastId;
    }

    public void dropTable(Table table) {
        try {
            for (Field field : table.getClass().getDeclaredFields()) {
                if (field.getType() != List.class) {
                    String sql = "DROP TABLE IF EXISTS " + joinTableName(table, field.getType().getSimpleName());
                    executeSql(sql);
                }
            }

            String sql = "DROP TABLE IF EXISTS " + table.getClass().getSimpleName();
            executeSql(sql);
            for (Field field : table.getClass().getDeclaredFields()) {
                if (field.getType() == List.class) {
                    sql = "DROP TABLE IF EXISTS " + joinTableName(table, field.getName());
                    executeSql(sql);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.FATAL, null, ex);
        }
    }

    public void executeSql(String sql) throws SQLException {

        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            String cleaned_sql = cleanSql(sql);
            statement.execute(cleaned_sql);
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, sql, e);
            throw (e);
        }
    }

    public void initialize() throws SQLException, ClassNotFoundException, IllegalAccessException {
        for (Table t : tables) {
            createTable(t);
        }
    }

    public void dropTables() {
        for (Table t : tables) {
            dropTable(t);
        }
    }

    private Table getTable(String name) throws TableClassNotFoundException {
        for (Table t : tables) {
            if (t.getClass().getSimpleName().toLowerCase().contains(name.toLowerCase())) {
                return t;
            }
        }
        throw new TableClassNotFoundException(name);
    }

    static class TableClassNotFoundException extends Exception {
        public TableClassNotFoundException(String tableName) {
            super("Table \"" + tableName + "\" does not exist!");
        }
    }
}
