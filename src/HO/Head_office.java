package HO;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Head_office {

    private final static String QUEUE_NAME="add_product";
    private final static String QUEUE_NAME2="delete_product";

    public final static String QUEUE_NAME21="add_product2";
    public final static String QUEUE_NAME22="delete_product2";
    private JFrame frame;
    private JTable table;

    public static void main(String[] args) throws IOException, TimeoutException {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Head_office window = new Head_office();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        ConnectionFactory facotry = new ConnectionFactory();
        facotry.setHost("localhost");
        Connection connection = facotry.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.queueDeclare(QUEUE_NAME2, false, false, false, null);
        channel.queueDeclare(QUEUE_NAME21, false, false, false, null);
        channel.queueDeclare(QUEUE_NAME22, false, false, false, null);
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            ObjectMapper mapper = new ObjectMapper();
            Product product = mapper.readValue(message, Product.class);
            Insert_Product.insert_products(product);
        };
        channel.basicConsume(QUEUE_NAME, true, "", deliverCallback, consumerTag -> {

        });

        DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            ObjectMapper mapper = new ObjectMapper();
            Product product = mapper.readValue(message, Product.class);
            System.out.println(product.toString());
            Insert_Product.delete_product(product);
        };

        channel.basicConsume(QUEUE_NAME2, true, "", deliverCallback2, consumerTag2 -> {
        });
        DeliverCallback deliverCallback21 = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(message);
            ObjectMapper mapper = new ObjectMapper();
            Product product = mapper.readValue(message, Product.class);
            Insert_Product.insert_products(product);
        };
        channel.basicConsume(QUEUE_NAME21, true, "", deliverCallback21, consumerTag -> {

        });

        DeliverCallback deliverCallback22 = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(message);
            ObjectMapper mapper = new ObjectMapper();
            Product product = mapper.readValue(message, Product.class);
            System.out.println(product.toString());
            Insert_Product.delete_product(product);
        };

        channel.basicConsume(QUEUE_NAME22, true, "", deliverCallback22, consumerTag2 -> {
        });
    }
    public Head_office() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout(0, 0));
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Name");
        model.addColumn("Quantity");
        model.addColumn("Cost");
        model.addColumn("Office");
        table = new JTable(model);
        List<Product> l = Retrieve_Product.get_products();
        for (Product p : l
        ) {
            model.addRow(new Object[]{p.name, p.quantity, p.cost,p.office});
        }
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                model.setRowCount(0);
                List<Product> l = Retrieve_Product.get_products();
                for (Product p : l
                ) {
                    model.addRow(new Object[]{p.name, p.quantity, p.cost,p.office});
                }
            }
        };
        timer.schedule(task, 0,  5 * 1000);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
    }
}
