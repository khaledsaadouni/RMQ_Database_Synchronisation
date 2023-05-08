package BO2;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Insert_Product {
    public static void insert_products(String name,int quantity,float cost){
        String url = "jdbc:mysql://localhost:3306/branch_office2";
        String user = "root";
        String password = "";
        int latest_id=0;
        String query = "SELECT * FROM product ORDER BY id DESC LIMIT 1;";

        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while(rs.next()) {
                latest_id=rs.getInt("id");
                latest_id++;
                System.out.println(latest_id);
            }

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger("error");
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

        String sql = "INSERT INTO product VALUES(?,?,?,?,0,?)";

        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, latest_id);
            pst.setString(2,name);
            pst.setInt(3,quantity);
            pst.setFloat(4,cost);
            pst.setInt(5,2);
            pst.executeUpdate();
            System.out.println("A new product has been inserted");

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger("JdbcPrepared.class.getName()");
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        }
    }
    public static void delete_product(int id){
        String url = "jdbc:mysql://localhost:3306/branch_office2";
        String user = "root";
        String password = "";

        String sql = "DELETE from product where id=?";

        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("A product has been deleted");

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger("JdbcPrepared.class.getName()");
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        }
    }
    public static void synch_product(int id){
        String url = "jdbc:mysql://localhost:3306/branch_office2";
        String user = "root";
        String password = "";

        String sql = "UPDATE product set synch=1 where id=?";

        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("A product has been synchronized");

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger("JdbcPrepared.class.getName()");
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        }
    }
}
