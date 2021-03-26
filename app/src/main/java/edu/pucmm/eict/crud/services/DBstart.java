package edu.pucmm.eict.crud.services;

import java.sql.SQLException;

import org.h2.tools.Server;

public class DBstart {

    private static DBstart instance;

    /**
     * Retornando la instancia.
     * 
     * @return
     */
    private DBstart() {
    }

    public static DBstart getInstance() {
        if (instance == null) {
            instance = new DBstart();
        }
        return instance;
    }

    public static void startDB() {
        try {
            Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers", "-ifNotExists").start();
            System.out.println("Database started");

            String status = Server.createWebServer("-trace", "-webPort", "9090").start().getStatus();
            System.out.println("__Web Server Status__\n" + status);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
