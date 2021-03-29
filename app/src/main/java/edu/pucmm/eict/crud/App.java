/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.pucmm.eict.crud;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jasypt.util.text.StrongTextEncryptor;

import edu.pucmm.eict.crud.logic.cart;
import edu.pucmm.eict.crud.logic.product;
import edu.pucmm.eict.crud.logic.sales;
import edu.pucmm.eict.crud.logic.user;
import edu.pucmm.eict.crud.services.DBstart;
import edu.pucmm.eict.crud.services.productServices;
import edu.pucmm.eict.crud.services.saleServices;
import edu.pucmm.eict.crud.services.userServices;
import io.javalin.Javalin;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    private static String url;
    // indica el modo de operacion para la base de datos.
    private static String modoConexion = "";

    public static void main(String[] args) throws SQLException {

        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/public");
        }).start(getHerokuAssignedPort());

        if (args.length >= 1) {
            modoConexion = args[0];
            System.out.println("Modo de Operacion: " + modoConexion);
        }

        // Iniciando la base de datos.
        if (modoConexion.isEmpty()) {
            DBstart.startDB();
        }

        // Valores de prueba

        product p1 = new product("Product 1", 12.1, 1);
        product p2 = new product("Product 2", 12.2, 2);
        product p3 = new product("Product 3", 12.3, 3);
        product p4 = new product("Product 4", 12.4, 4);
        product p5 = new product("Product 5", 12.5, 5);
        productServices.getInstancia().crear(p1);
        productServices.getInstancia().crear(p2);
        productServices.getInstancia().crear(p3);
        productServices.getInstancia().crear(p4);
        productServices.getInstancia().crear(p5);

        // DEFAULT USER
        // TODO
        // user admin = new user("admin", "admin", "admin");
        // userServices.getInstancia().crear(admin);

        app.before("/", ctx -> {
            if (ctx.cookie("rememberUser") != null) {
                StrongTextEncryptor encryptor = new StrongTextEncryptor();
                encryptor.setPassword("myEncryptionPassword");
                ctx.sessionAttribute("user", encryptor.decrypt(ctx.cookie("rememberUser")));
            }
        });

        app.get("/", ctx -> {
            ctx.redirect("/main");
        });

        app.get("/main", ctx -> {
            Map<String, Object> pModel = new HashMap<>();
            List<product> productsList = productServices.getInstancia().findAll();

            pModel.put("prod", productsList);

            if (ctx.sessionAttribute("cart") == null) {
                pModel.put("amountCart", 0);
            } else {
                int amount = 0;
                cart currentCart = ctx.sessionAttribute("cart");
                for (product prod : currentCart.getProducts()) {
                    amount += prod.getAmount();
                }
                pModel.put("amountCart", amount);
            }
            ctx.render("/templates/list.html", pModel);
        });

        // Carrito
        app.post("/addToCart", ctx -> {
            cart carrito = null;
            Integer cod = ctx.formParam("ID", Integer.class).get();
            Integer cant = ctx.formParam("amount", Integer.class).get();
            product prod = productServices.getInstancia().find(cod);

            if (ctx.sessionAttribute("cart") == null) {
                carrito = new cart(new ArrayList<product>());
            } else {
                carrito = ctx.sessionAttribute("cart");
            }
            product prodToInsert = new product(prod.getID(), prod.getName(), prod.getPrice(), cant);
            productServices.getInstancia().editar(prod).decreaseAmount(cant);
            System.out.println(prodToInsert.getID() + prodToInsert.getName());
            carrito.addToCart(prodToInsert);
            ctx.sessionAttribute("cart", carrito);

            ctx.redirect("/");
        });

        app.get("/cart", ctx -> {

            cart userCart = ctx.sessionAttribute("cart");

            if (userCart == null) {
                userCart = new cart(new ArrayList<product>());
            }

            Map<String, Object> cModel = new HashMap<>();
            cModel.put("prod", userCart.getProducts());
            cModel.put("total", userCart.getTotal());

            ctx.render("/templates/cart.html", cModel);
        });

        app.post("/cart/delete", ctx -> {
            int cod = ctx.formParam("id", Integer.class).get();
            int cant = ctx.formParam("amount", Integer.class).get();
            cart carrito = ctx.sessionAttribute("cart");
            product prod = productServices.getInstancia().find(cod);
            productServices.getInstancia().editar(prod).increaseAmount(cant);
            carrito.removeFromCart(prod);

            ctx.redirect("/cart");
        });

        app.post("/cart/checkout", ctx -> {
            String client = ctx.formParam("client");
            cart carrito = ctx.sessionAttribute("cart");

            sales newSale = new sales(carrito, client, carrito.getTotal(), Calendar.getInstance().getTime().toString());
            saleServices.getInstancia().crear(newSale);

            ctx.sessionAttribute("sales", saleServices.getInstancia().findAll());
            ctx.sessionAttribute("cart", null);

            ctx.redirect("/");
        });

        app.before("/signin", ctx -> {
            String user = ctx.formParam("user");
            String pass = ctx.formParam("pass");
            user aux = userServices.getInstancia().verifyUser(pass, user);

            if (aux == null) {
                ctx.redirect("/401.html");
            } else if (pass.equals(aux.getPassword())) {
                ctx.attribute("currentUser", aux);

                if (ctx.formParam("remember") != null) {
                    StrongTextEncryptor encryptor = new StrongTextEncryptor();
                    encryptor.setPassword("myEncryptionPassword");
                    String userEncryp = encryptor.encrypt(aux.getUser());
                    ctx.cookie("rememberUser", userEncryp, 604800);
                }
            } else {
                ctx.redirect("/401.html");
            }
        });
        app.post("/signin", ctx -> {
            ctx.sessionAttribute("user", ctx.attribute("currentUser"));
            ctx.redirect(url);
        });

        // Ventas
        // Autenticar usuario
        app.before("/sales", ctx -> {
            String currentUser = ctx.sessionAttribute("user");
            if (currentUser == null) {
                url = ctx.req.getRequestURI();
                ctx.redirect("/login.html");
            }
        });
        app.get("/sales", ctx -> {
            // ArrayList<sales> salesS = ctx.sessionAttribute("sales");
            Map<String, Object> sModel = new HashMap<>();

            if (ctx.sessionAttribute("cart") == null) {
                sModel.put("amountCart", 0);
            } else {
                int amount = 0;
                cart currentCart = ctx.sessionAttribute("cart");
                for (product prod : currentCart.getProducts()) {
                    amount += prod.getAmount();
                }
                sModel.put("amountCart", amount);
            }
            sModel.put("sales", saleServices.getInstancia().findAll());

            ctx.render("/templates/sales.html", sModel);
        });

        app.before("/products", ctx -> {
            String currentUser = ctx.sessionAttribute("user");
            if (currentUser == null) {
                url = ctx.req.getRequestURI();
                ctx.redirect("/login.html");
            }
        });
        app.get("/products", ctx -> {
            Map<String, Object> pModel = new HashMap<>();
            pModel.put("prod", productServices.getInstancia().findAll());

            if (ctx.sessionAttribute("cart") == null) {
                pModel.put("amountCart", 0);
            } else {
                int amount = 0;
                cart currentCart = ctx.sessionAttribute("cart");
                for (product prod : currentCart.getProducts()) {
                    amount += prod.getAmount();
                }
                pModel.put("amountCart", amount);
            }
            ctx.render("/templates/products.html", pModel);
        });

        app.get("/products/delete/:id", ctx -> {
            int cod = ctx.pathParam("id", Integer.class).get();
            productServices.getInstancia().eliminar(cod);
            ctx.redirect("/products");
        });

        app.get("/products/edit/:id", ctx -> {
            int cod = ctx.pathParam("id", Integer.class).get();
            product p = productServices.getInstancia().find(cod);
            Map<String, Object> model = new HashMap<>();
            System.out.println(p.getID() + p.getName());
            model.put("prod", p);
            ctx.render("/templates/productEdit.html", model);
        });

        app.post("/products/edit/", ctx -> {
            int cod = ctx.formParam("prodID", Integer.class).get();
            String name = ctx.formParam("prodName");
            Double price = ctx.formParam("prodPrice", Double.class).get();
            int amount = ctx.formParam("prodAmount", Integer.class).get();

            product auxProduct = new product(name, price, amount);
            productServices.getInstancia().editar(auxProduct);
            ctx.redirect("/products");
        });

        app.before("/products/productAdd", ctx -> {
            user currentUser = ctx.sessionAttribute("user");
            if (currentUser == null) {
                url = ctx.req.getRequestURI();
                ctx.redirect("/login.html");
            }
        });

        app.post("/products/productAdd", ctx -> ctx.render("/templates/productAdd.html"));

        app.post("/products/add", ctx -> {
            String name = ctx.formParam("prodName");
            Double price = ctx.formParam("prodPrice", Double.class).get();
            int amount = ctx.formParam("prodAmount", Integer.class).get();
            product auxProduct = new product(name, price, amount);
            productServices.getInstancia().crear(auxProduct);

            ctx.redirect("/products");
        });

    }

    /**
     * Metodo para indicar el puerto en Heroku
     * 
     * @return
     */
    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 7000; // Retorna el puerto por defecto en caso de no estar en Heroku.
    }

    public static String getModoConexion() {
        return modoConexion;
    }
}
