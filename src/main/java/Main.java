import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;

import controller.MainController;
import model.*;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import view.Menu;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {
    static SessionFactory sessionFactoryObj;

    private static SessionFactory buildSessionFactory() {
        try {
            StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml").build();
            Metadata metadata = new MetadataSources(standardRegistry).getMetadataBuilder().build();
            return metadata.getSessionFactoryBuilder().build();
        } catch (HibernateException he) {
            System.out.println("Session Factory creation failure");
            throw he;
        }
    }

    public static EntityManagerFactory createEntityManagerFactory() {
        EntityManagerFactory emf;
        try {
            emf = Persistence.createEntityManagerFactory("JPAMagazines");
        } catch (Throwable ex) {
            System.err.println("Failed to create EntityManagerFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        return emf;
    }

    public static void main(String[] args) throws SQLException, FileNotFoundException {
        ArrayList<Game> partidas = new ArrayList();

        EntityManagerFactory entityManagerFactory = createEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Menu menu = new Menu();
        MainController mainController = new MainController(entityManagerFactory);
        int option;

        do {
            option = menu.mainMenu();
            switch (option) {
                case 1:
                    mainController.showTableNames();
                    break;
                case 2:
                    // Lógica para menu de las consultas
                    break;
                case 3:
                    mainController.showTableNames();
                    System.out.println("¿Que entidad quieres crear?: ");
                    mainController.crearEntidad(mainController.lecturaNombre());
                    break;
                case 4:
                    mainController.showTableNames();
                    System.out.println("¿Que tabla quieres modificar?: ");
                    mainController.editarEntidad(mainController.lecturaNombre());
                    break;
                case 5:

                    switch (menu.menuDelete()){
                        case 1:
                            mainController.showTableNames();
                            mainController.eliminarEntidad(mainController.lecturaNombre());
                            break;
                        case 2:
                            mainController.showTableNames();

                            break;

                        case 3:
                            mainController.showTableNames();
                            break;
                        case 4:
                            System.out.println("Saliendo de este menu");
                            break;
                    }
                    break;
                case 6:
                    mainController.poblarTablas();
                    break;
                case 7:
                    mainController.showTableNames();

                    System.out.println("Selecciona que tabla quieres eliminar de la base de datos (Inserta su nombre): ");

                    mainController.eliminarTabla(mainController.lecturaNombre());
                    break;
                case 8:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida. Inténtelo de nuevo.");
            }

        } while (option != 8);
    }
}