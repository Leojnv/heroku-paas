package edu.pucmm.eict.crud.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.pucmm.eict.crud.services.DBservices;

public class controller {
    private ArrayList<user> users;
    private ArrayList<sales> sales;
    private ArrayList<product> products;
    private ArrayList<cart> carts;

    public controller() {
        this.users = new ArrayList<user>();
        this.sales = new ArrayList<sales>();
        this.products = new ArrayList<product>();
        this.carts = new ArrayList<cart>();
    }

    public ArrayList<user> getUsers() {
        return users;
    }

    public ArrayList<sales> getSales() {
        return sales;
    }

    public ArrayList<product> getProducts() {
        return products;
    }

    public ArrayList<cart> getCarts() {
        return carts;
    }

    /**
     * Modificar para la base de datos
     * 
     * @throws SQLException
     */
    public void addUser(String user, String name, String password) throws SQLException {
        // this.users.add(new user(user, name, password));

        String query = "INSERT INTO USERS(USERNAME, NAME, PASSWORD) values(?,?,?);";

        Connection db = DBservices.connectDB();
        PreparedStatement st = db.prepareStatement(query);

        st.setString(1, user);
        st.setString(2, name);
        st.setString(3, password);

        st.execute();

        st.close();
        db.close();
    }

    public void addProductToInvoice(int idSale, cart c) throws SQLException {
        String sqlQuery = "INSERT INTO PRODUCTSSALE(ID_SALE, ID_PRODUCT, AMOUNT) values(?,?,?);";
        Connection db = DBservices.connectDB();
        PreparedStatement st = null;
        for (product p : c.getProducts()) {
            st = db.prepareStatement(sqlQuery);
            st.setInt(1, idSale);
            st.setInt(2, p.getID());
            st.setInt(3, p.getAmount());

            st.execute();
        }
        st.close();

        db.close();
    }

    public void addSale(int id, cart cart, String user, Double total, String date) throws SQLException {
        // sales s = new sales(id, cart, user, total, date);
        // this.sales.add(s);
        String query = "INSERT INTO SALES(CLIENTNAME, DATE, PRICE) values(?,?,?);";

        Connection db = DBservices.connectDB();
        PreparedStatement st = db.prepareStatement(query);

        st.setString(1, user);
        st.setString(2, date);
        st.setDouble(3, total);

        st.execute();
        st.close();
        db.close();
        addProductToInvoice(id, cart);
    }

    public void addProduct(String name, Double price, Integer amount) throws SQLException {
        // this.products.add(new product(i, name, price, amount));
        String query = "INSERT INTO PRODUCTS(NAME, PRICE, AMOUNT) values(?,?,?);";

        Connection db = DBservices.connectDB();
        PreparedStatement st = db.prepareStatement(query);

        st.setString(1, name);
        st.setDouble(2, price);
        st.setInt(3, amount);

        st.execute();

        st.close();
        db.close();
    }

    public void removeProduct(int ID) throws SQLException {
        // this.products.remove(searchProduct(ID));

        String query = "DELETE FROM PRODUCTS WHERE ID=?;";

        Connection db = DBservices.connectDB();
        PreparedStatement st = db.prepareStatement(query);

        st.setInt(1, ID);

        st.execute();

        st.close();
        db.close();
    }

    public void modifyProduct(int ID, String name, Double price, Integer amount) throws SQLException {
        int index = searchProduct(ID);
        // this.products.get(index).setName(name);
        // this.products.get(index).setPrice(price);
        // this.products.get(index).setAmount(amount);
        String query = "UPDATE PRODUCTS SET NAME=?, PRICE=?, AMOUNT=? WHERE ID=?;";
        Connection db = DBservices.connectDB();
        PreparedStatement st = db.prepareStatement(query);

        st.setString(1, name);
        st.setDouble(2, price);
        st.setInt(3, amount);
        st.setInt(4, index);

        st.execute();

        st.close();
        db.close();
    }

    // EL carrito no lo tengo que añadir a la base de datos porque tengo la tabla
    // que relaciona los items con una factura en la base de datos
    public void addCart(ArrayList<product> products) {
        this.carts.add(new cart(products));
    }

    public void removeCart(int ID) {
        this.products.remove(searchCart(ID));
    }

    public Integer searchProduct(int ID) throws SQLException {

        boolean found = false;
        int index = 0;
        while (!found && index < this.products.size()) {
            if (this.products.get(index).getID() == ID) {
                found = true;
            }
            index++;
        }
        return index - 1;
    };

    public Integer searchProductFromDB(int ID) throws SQLException {
        /*
         * boolean found = false; int index = 0; while (!found && index <
         * this.products.size()) { if (this.products.get(index).getID() == ID) { found =
         * true; } index++; } return index - 1;
         */

        String query = "SELECT ID FROM PRODUCTS ORDER BY ID ASC";
        Connection db = DBservices.connectDB();
        PreparedStatement st = db.prepareStatement(query);
        ResultSet rs = st.executeQuery();
        int dbID = 0; // Representa el ID del producto
        int index = 0;
        while (rs.next()) {
            dbID = rs.getInt("ID");
            if (dbID == ID) {
                return index; // Returna el índice si lo encuentra
            }
            index++;
        }
        return null; // Retorna -1 si no lo encuentra

    };

    public int searchCart(int ID) {
        boolean found = false;
        int index = 0;
        while (!found && index < this.carts.size()) {
            if (this.carts.get(index).getID() == ID) {
                found = true;
            }
            index++;
        }
        return index - 1;

    };

    public user searchUser(String u) throws SQLException {

        /*
         * boolean found = false; int index = 0; user p = null; while (!found && index <
         * this.users.size()) { if (this.users.get(index).getUser().equalsIgnoreCase(u))
         * { found = true; p = this.users.get(index); } index++; } return p;
         */

        String query = "SELECT * FROM USERS ORDER BY USERNAME DESC";
        Connection db = DBservices.connectDB();
        PreparedStatement st = db.prepareStatement(query);
        ResultSet rs = st.executeQuery();
        String user = "";
        while (rs.next()) {
            user = rs.getString("USERNAME");
            if (user.equalsIgnoreCase(u)) {
                String name = rs.getString("NAME");
                String pass = rs.getString("PASSWORD");
                return new user(user, name, pass); // Retorna el usuario
            }

        }
        return null; // Retorna null si no lo encuentra
    };

    public void getProductsFromDB() throws SQLException {
        this.products.clear();

        String query = "SELECT * FROM PRODUCTS;";
        Connection db = DBservices.connectDB();
        PreparedStatement st = db.prepareStatement(query);
        ResultSet rs = st.executeQuery();

        Integer id = 0;
        String name = "";
        Double price = 0.0;
        Integer amount = 0;

        while (rs.next()) {
            id = rs.getInt("ID");
            name = rs.getString("NAME");
            price = rs.getDouble("PRICE");
            amount = rs.getInt("AMOUNT");

            // No utilizo el método addProduct porque estaría intentando duplicar en la base
            // de datos
            this.products.add(new product(id, name, price, amount));
        }

        st.close();
        db.close();

    }

    public void getUsersFromDB() throws SQLException {
        this.users.clear();

        String query = "SELECT * FROM USERS;";
        Connection db = DBservices.connectDB();
        PreparedStatement st = db.prepareStatement(query);
        ResultSet rs = st.executeQuery();

        String name = "";
        String user = "";
        String password = "";

        while (rs.next()) {
            name = rs.getString("NAME");
            user = rs.getString("USERNAME");
            password = rs.getString("PASSWORD");

            this.users.add(new user(user, name, password));
        }

        st.close();
        db.close();
    }

    public void getSalesFromDB() throws SQLException {
        this.sales.clear();

        String querySales = "SELECT * FROM SALES;";
        String queryProducts = "SELECT * FROM PRODUCTSSALE WHERE ID_SALE=?";
        Connection db = DBservices.connectDB();
        PreparedStatement st = db.prepareStatement(querySales);
        ResultSet rs = st.executeQuery();

        // For Sale
        Integer idsale = 0;
        String clientname = "";
        String date = "";
        Double total = 0.0;

        // For product

        Integer id = 0;
        Integer amount = 0;
        cart auxCart = new cart(new ArrayList<product>());

        while (rs.next()) {
            idsale = rs.getInt("ID");
            clientname = rs.getString("CLIENTNAME");
            date = rs.getString("DATE");
            total = rs.getDouble("PRICE");

            PreparedStatement stProducts = db.prepareStatement(queryProducts);
            stProducts.setInt(1, idsale);
            ResultSet rsProducts = stProducts.executeQuery();

            while (rsProducts.next()) {
                id = rsProducts.getInt("ID_PRODUCT");
                product auxP = this.products.get(searchProduct(id));
                product productToInsert = new product(auxP.getID(), auxP.getName(), auxP.getPrice(), amount);
                auxCart.addToCart(productToInsert);

            }
            stProducts.close();
            this.sales.add(new sales(idsale, auxCart, clientname, total, date));
        }
        st.close();
        db.close();
    }

    public Integer searchLastSaleIndex() throws SQLException {
        String query = "SELECT ID FROM SALES ORDER BY ID DESC LIMIT 1;";
        Connection db = DBservices.connectDB();
        PreparedStatement st = db.prepareStatement(query);
        ResultSet rs = st.executeQuery();
        int id = 0;

        while (rs.next()) {
            id = rs.getInt("ID");
        }
        st.close();
        db.close();

        return id;
    }
}
