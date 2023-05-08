package HO;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Insert_Product {
    public static void insert_products(Product product){
        String url = "jdbc:mysql://localhost:3306/head_office";
        String user = "root";
        String password = "";
        String sql = "INSERT INTO product VALUES(?,?,?,?,0,?)";
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
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, latest_id+1);
            pst.setString(2,product.name);
            pst.setInt(3,product.quantity);
            pst.setFloat(4,product.cost);
            pst.setInt(5,product.office);
            pst.executeUpdate();
            System.out.println("A new product has been inserted");

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger("JdbcPrepared.class.getName()");
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        }
    }
    public static void delete_product(Product product){
        String url = "jdbc:mysql://localhost:3306/head_office";
        String user = "root";
        String password = "";

        String sql = "DELETE from product where name=? and quantity=? and cost=? and office=?";

        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement(sql)) {
            System.out.println(product.toString());
            pst.setString(1,product.name);
            pst.setInt(2, product.quantity);
            pst.setFloat(3,product.cost);
            pst.setInt(4,product.office);
            pst.executeUpdate();
            System.out.println("A product has been deleted");

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger("JdbcPrepared.class.getName()");
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        }
    }
}
