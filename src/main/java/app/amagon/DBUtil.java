package app.amagon;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import app.amagon.entities.Customer;

import java.sql.*;
import java.util.Objects;

public class DBUtil {
    private static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static Connection db_connection;
    private static PreparedStatement p_stmt;
    private static ResultSet rs;
    private static Statement statement;
    private static ObservableList<Customer> customerList;

    /*private static EntityManager entityManager;*/

    public static void dbConnect() throws SQLException, ClassNotFoundException{
        String dbPass = "robert1324";
        String dbHost = "localhost";
        String dbPort = "1433";
        String dbName = "DB2_Projekt";
        String dbUser = "robert";
        String connectionURL = "jdbc:sqlserver://" +
                dbHost + ":" + dbPort + ";" + "databaseName=" + dbName + ";" +
                "user=" + dbUser + ";" +
                "password=" + dbPass + ";" + "encrypt=false;trustServerCertificate=true;";
        try{
            Class.forName(JDBC_DRIVER);
        }catch(ClassNotFoundException e){
            System.out.println("SQL Server JDBC Driver fehlt");
            e.printStackTrace();
            throw e;
        }
        try{
            db_connection = DriverManager.getConnection(connectionURL);
        }catch (SQLException e){
            e.printStackTrace();
            throw e;
        }
    }
    public static void dbDisconnect() throws SQLException{
        if (db_connection != null && !db_connection.isClosed()){
            db_connection.close();
            db_connection = null;
        }
    }
    public static Connection getConnection(){
        return db_connection;
    }
    public static void dbExecuteQuery(String sqlStatement) throws SQLException{
        try{
            if (!db_connection.isClosed()) {
                p_stmt = db_connection.prepareStatement(sqlStatement);
                rs = p_stmt.executeQuery();
            }
            else{
                System.out.println("Es besteht keine Verbindung mit der Datenbank");
            }
        }catch (SQLException e){
            System.out.println("Problem beim Ausführen von Query.");
            e.printStackTrace();
            throw e;
        }
        finally {
            if (rs != null) {
                try {
                    rs.close();
                }
                catch(Exception e) {}
            }
            if (p_stmt != null) {
                try {
                    p_stmt.close();
                }
                catch(Exception e) {}
            }
            if (db_connection != null) {
                try {
                    db_connection.close();
                }
                catch(Exception e) {}
            }

        }
    }
    public void initData() throws SQLException {
        getCustomerData();
    }
    private void getCustomerData() throws SQLException {
        customerList = FXCollections.observableArrayList();
        try{
            if (!db_connection.isClosed()) {
                p_stmt = db_connection.prepareStatement("select * from amagon.customer");
                rs = p_stmt.executeQuery();
                while (rs.next()) {
                    Customer customer = new Customer(
                            rs.getInt("customer_id"),
                            rs.getString("surname"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getString("city"));
                    customerList.add(customer);
                }
            }
            else{
                System.out.println("Es besteht keine Verbindung mit der Datenbank");
            }
        }catch (SQLException e){
            System.out.println("Problem beim Ausführen von Query.");
            e.printStackTrace();
            throw e;
        }
        finally {
            if (rs != null) {
                try {
                    rs.close();
                }
                catch(Exception e) { e.printStackTrace(); }
            }
            if (p_stmt != null) {
                try {
                    p_stmt.close();
                }
                catch(Exception e) { e.printStackTrace(); }
            }
            if (db_connection != null) {
                try {
                    db_connection.close();
                }
                catch(Exception e) { e.printStackTrace(); }
            }

        }
    }
    public ObservableList<Customer> getCustomerList() throws SQLException {
        this.getCustomerData();
        return customerList;
    }
    public static int getTotalCustomers() throws SQLException {
        int total = 0;
        try {
            if (!db_connection.isClosed()) {
                p_stmt = db_connection.prepareStatement("select [amagon].[getTotalCustomers]() as total");
                rs = p_stmt.executeQuery();
                while(rs.next()){
                    total = rs.getInt(rs.findColumn("total"));
                }
            } else {
                System.out.println("Es besteht keine Verbindung mit der Datenbank");
            }
        } catch (SQLException e) {
            System.out.println("Problem beim Ausfuehren von Query.");
            e.printStackTrace();
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                }
            }
            if (p_stmt != null) {
                try {
                    p_stmt.close();
                } catch (Exception e) {
                }
            }
            if (db_connection != null) {
                try {
                    db_connection.close();
                } catch (Exception e) {
                }
            }
        }
        return total;
    }
    public static void deleteCustomerByID(String ID) throws SQLException{
        try{
            if (!db_connection.isClosed()) {
                p_stmt = db_connection.prepareStatement("exec [amagon].[deleteByCustomerID] ?");
                p_stmt.setString(1,ID);
                p_stmt.execute();
            }
            else{
                System.out.println("Es besteht keine Verbindung mit der Datenbank");
            }
        }catch (SQLException e){
            System.out.println("Problem beim Ausführen von Query.");
            e.printStackTrace();
            throw e;
        }
        finally {
            if (rs != null) {
                try {
                    rs.close();
                }
                catch(Exception e) {}
            }
            if (p_stmt != null) {
                try {
                    p_stmt.close();
                }
                catch(Exception e) {}
            }
            if (db_connection != null) {
                try {
                    db_connection.close();
                }
                catch(Exception e) {}
            }

        }
    }
    public static void addCustomer(String surname,String name,String address,String city) throws SQLException {
        try{
            if (!db_connection.isClosed()) {
                if (!Objects.equals(surname, "") || !Objects.equals(name, "") || !Objects.equals(address, "") || !Objects.equals(city, "")){
                    p_stmt = db_connection.prepareStatement("exec [amagon].[addCustomer] ?,?,?,?");
                    p_stmt.setString(1,surname);
                    p_stmt.setString(2,name);
                    p_stmt.setString(3,address);
                    p_stmt.setString(4,city);
                    p_stmt.execute();
                }
            }
            else{
                System.out.println("Es besteht keine Verbindung mit der Datenbank");
            }
        }catch (SQLException e){
            System.out.println("Problem beim Ausführen von Query.");
            e.printStackTrace();
            throw e;
        }
        finally {
            if (rs != null) {
                try {
                    rs.close();
                }
                catch(Exception e) {}
            }
            if (p_stmt != null) {
                try {
                    p_stmt.close();
                }
                catch(Exception e) {}
            }
            if (db_connection != null) {
                try {
                    db_connection.close();
                }
                catch(Exception e) {}
            }

        }
    }
}
