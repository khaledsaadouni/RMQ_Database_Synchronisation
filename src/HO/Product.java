package HO;

import java.io.Serializable;

public class Product implements Serializable {
    public int id;
    public String name;
    public int quantity;
    public float cost;
    public boolean synch;

    public int office;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", cost=" + cost +
                ", synch=" + synch +
                ", office=" + office +
                '}';
    }

}
