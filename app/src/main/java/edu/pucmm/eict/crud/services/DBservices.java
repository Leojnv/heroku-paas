package edu.pucmm.eict.crud.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaQuery;

import org.h2.tools.Server;

/**
 * DBservices
 * 
 * @param <T>
 */
public class DBservices<T> {

    private final static String url = "jdbc:h2:tcp://localhost/~/myDB"; // Modo Server...
    private final static String driver = "org.h2.Driver";
    // Variable utilizada para gestionar la conexión a la DB
    private static Connection DBconnection = null;
    // Variable para gestionar el servidor DB
    private static Server DBserver;

    /**
     * Metodo para el registro de driver de conexión y conexión con la base de datos
     */
    public static Connection connectDB() {
        try {
            Class.forName(driver);
            DBconnection = DriverManager.getConnection(url);
            System.out.println("__La base de datos se ha conectado correctamente__");
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("__Se ha producido un error en la conexión de la base de datos__\n");
            ex.printStackTrace();
        }
        return DBconnection;
    }

    public static void DBstop() {
        DBserver.shutdown();
    }

    // ORM

    private static EntityManagerFactory emf;
    private Class<T> claseEntidad;

    public DBservices(Class<T> claseEntidad) {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("MiUnidadPersistencia");
        }
        this.claseEntidad = claseEntidad;

    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     *
     * @param entidad
     */
    public T crear(T entidad) throws IllegalArgumentException, EntityExistsException, PersistenceException {
        EntityManager em = getEntityManager();

        try {

            em.getTransaction().begin();
            em.persist(entidad);
            em.getTransaction().commit();

        } finally {
            em.close();
        }
        return entidad;
    }

    /**
     *
     * @param entidad
     */
    public T editar(T entidad) throws PersistenceException {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            em.merge(entidad);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return entidad;
    }

    /**
     *
     * @param entidadId
     */
    public boolean eliminar(Object entidadId) throws PersistenceException {
        boolean ok = false;
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            T entidad = em.find(claseEntidad, entidadId);
            em.remove(entidad);
            em.getTransaction().commit();
            ok = true;
        } finally {
            em.close();
        }
        return ok;
    }

    /**
     *
     * @param id
     * @return
     */
    public T find(Object id) throws PersistenceException {
        EntityManager em = getEntityManager();
        try {
            return em.find(claseEntidad, id);
        } finally {
            em.close();
        }
    }

    /**
     *
     * @return
     */
    public List<T> findAll() throws PersistenceException {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(claseEntidad);
            criteriaQuery.select(criteriaQuery.from(claseEntidad));
            return em.createQuery(criteriaQuery).getResultList();
        } finally {
            em.close();
        }
    }
}