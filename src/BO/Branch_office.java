package BO;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;
import  java.util.Timer;
import java.util.concurrent.TimeoutException;


public class Branch_office extends JFrame implements ActionListener {
    public final static String QUEUE_NAME="add_product";
    public final static String QUEUE_NAME2="delete_product";
    private DefaultTableModel model;
    private JTable table;
    private JTextField productInput;
    private JTextField quantityInput;
    private JTextField costInput;
    public static JTextField deleteId;

    public Branch_office() {
        // Set up the table with random data
        model = new DefaultTableModel();
        table = new JTable(model);
        model.addColumn("id");
        model.addColumn("Product");
        model.addColumn("Quantity");
        model.addColumn("Cost");
        Random random = new Random();
        List< Product> l =  Retrieve_Product.get_products();
        for ( Product p : l
        ) {
            model.addRow(new Object[]{p.id,p.name, p.quantity, p.cost});
        }
        Timer timer = new  Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                model.setRowCount(0);
                List< Product> l = Retrieve_Product.get_products();
                for (Product p : l
                ) {
                    model.addRow(new Object[]{p.id,p.name, p.quantity, p.cost});
                }
                List< Product> l_unsynchronized =Retrieve_Product.get_unsynch_products();
                        ConnectionFactory factory = new ConnectionFactory();

                for (Product p : l_unsynchronized
                ) {

                        factory.setHost("localhost");
                try(
                        Connection connection = factory.newConnection();
                        Channel channel= connection.createChannel();
                ) {
                    channel.queueDeclare(QUEUE_NAME,false,false,false,null);
                    ObjectMapper mapper = new ObjectMapper();
                    String json = mapper.writeValueAsString(p);
                    channel.basicPublish("",QUEUE_NAME,null,json.getBytes());
                    Insert_Product.synch_product(p.id);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (TimeoutException ex) {
                    throw new RuntimeException(ex);
                } ;


                }
            }
        };

        timer.schedule(task, 0,  5 * 1000);
        // Set up the input fields and button
        productInput = new JTextField(10);
        quantityInput = new JTextField(10);
        costInput = new JTextField(10);
        JButton addButton = new JButton("Add");
        deleteId = new JTextField(10);
        JButton delete = new JButton("delete");
        Delete_Button deleteButton=new Delete_Button();
        addButton.addActionListener(this);
        delete.addActionListener(deleteButton);

        // Set up the JFrame with the table, input fields, and button
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Product:"));
        inputPanel.add(productInput);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityInput);
        inputPanel.add(new JLabel("Cost:"));
        inputPanel.add(costInput);
        inputPanel.add(addButton);
        inputPanel.add(new JLabel("id:"));
        inputPanel.add(deleteId);
        inputPanel.add(delete);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.add(new JScrollPane(table), BorderLayout.CENTER);
        contentPane.add(inputPanel, BorderLayout.SOUTH);
        setContentPane(contentPane);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Branch Office  1");
        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Add a new row to the table with the data from the input fields
        String product = productInput.getText();
        int quantity = Integer.parseInt(quantityInput.getText());
        double cost = Double.parseDouble(costInput.getText());
         model.addRow(new Object[]{0,product, quantity, cost});
         Insert_Product.insert_products(product,quantity, (float) cost);
        // Clear the input fields
        productInput.setText("");
        quantityInput.setText("");
        costInput.setText("");
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Branch_office::new);
    }
}
class Delete_Button implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        int id = Integer.parseInt(Branch_office.deleteId.getText());
        Product p = Retrieve_Product.get_product_byID(id);
        Insert_Product.delete_product(id);
        Branch_office.deleteId.setText("");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try(
                Connection connection = factory.newConnection();
                Channel channel= connection.createChannel();
        ) {
            ObjectMapper mapper = new ObjectMapper();
            System.out.println(p.toString());
            String json = mapper.writeValueAsString(p);
            channel.queueDeclare(Branch_office.QUEUE_NAME2,false,false,false,null);
            channel.basicPublish("",Branch_office.QUEUE_NAME2,null,json.getBytes());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (TimeoutException ex) {
            throw new RuntimeException(ex);
        } ;
    }
}