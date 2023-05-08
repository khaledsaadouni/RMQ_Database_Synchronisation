package BO;

import BO.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Retrieve_Product {
    public static List< Product> get_products(){
        List< Product> l=new ArrayList< Product>();
        String url = "jdbc:mysql://localhost:3306/branch_office1";
        String user = "root";
        String password = "";

        String query = "SELECT * FROM product";

        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while(rs.next()) {
                Product product = new Product();
                product.id=rs.getInt("id");
                product.name=rs.getString("name");
                product.quantity=rs.getInt("quantity");
                product.cost=rs.getFloat("cost");
                l.add(product);
            }

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger("error");
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return l;
    }
    public static  Product  get_product_byID(int id){
        Product l=new  Product();
        String url = "jdbc:mysql://localhost:3306/branch_office1";
        String user = "root";
        String password = "";

        String query = "SELECT * FROM product WHERE id="+id;

        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while(rs.next()) {
                Product product = new Product();
                product.id=rs.getInt("id");
                product.name=rs.getString("name");
                product.quantity=rs.getInt("quantity");
                product.cost=rs.getFloat("cost");
                product.cost=rs.getFloat("cost");
                product.office=rs.getInt("office");
                return product;
            }

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger("error");
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return l;
    }
    public static List< Product> get_unsynch_products(){
        List< Product> l=new ArrayList< Product>();
        String url = "jdbc:mysql://localhost:3306/branch_office1";
        String user = "root";
        String password = "";

        String query = "SELECT * FROM product WHERE synch=0";

        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while(rs.next()) {
                Product product = new Product();
                product.id=rs.getInt("id");
                product.name=rs.getString("name");
                product.quantity=rs.getInt("quantity");
                product.cost=rs.getFloat("cost");
                product.office=rs.getInt("office");
                l.add(product);
            }

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger("error");
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return l;
    }
}
