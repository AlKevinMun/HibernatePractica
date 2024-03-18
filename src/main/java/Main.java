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

/**
 * Clase Main. La que se ejecuta al iniciar el programa.
 */
public class Main {
    /**
     * Objeto que gestiona todo lo que tiene que ver con el Hibernate.
     * @return Devuelve el objeto EntityManagerFactory
     */
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

        EntityManagerFactory entityManagerFactory = createEntityManagerFactory();

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

                    switch (menu.menuQuerry()){
                        case 1:
                            mainController.showTableNames();
                            System.out.println("Inserta la tabla de la que quieres ver los valores: ");
                            mainController.selectFromTable(mainController.lecturaNombre());
                            break;
                        case 2:
                            mainController.showTableNames();
                            System.out.println("Inserta la tabla de la que quieres ver los valores: ");
                            String table = mainController.lecturaNombre();
                            System.out.println(table.toLowerCase());
                            mainController.showAttributeNames(table.toLowerCase());
                            System.out.println("Inserta la columna en la que quieres buscar:  ");
                            String colum = mainController.lecturaNombre();
                            System.out.println("Inserta el texto que quieres buscar: ");
                            String text = mainController.lecturaNombre();
                            mainController.selectFromTable(table,colum,text);
                            break;
                        case 3:
                            mainController.showTableNames();
                            System.out.println("Inserta la tabla de la que quieres ver los valores: ");
                            String table1 = mainController.lecturaNombre();
                            System.out.println(table1.toLowerCase());
                            mainController.showAttributeNames(table1.toLowerCase());
                            System.out.println("Inserta la columna en la que quieres buscar:  ");
                            String colum1 = mainController.lecturaNombre();
                            menu.showConditions();
                            System.out.println("Inserta la condicion con la que quieres buscar (Poner los simbolos entre parentesis): ");
                            String cond = mainController.lecturaNombre();
                            System.out.println("Inserta el valor a buscar: ");
                            int value = Integer.parseInt(mainController.lecturaNombre());
                            mainController.selectFromTable(table1,colum1,cond, value);
                            break;
                        case 4:
                            mainController.showTableNames();
                            System.out.println("Inserta la tabla de la que quieres ver los valores: ");
                            String table2 = mainController.lecturaNombre();
                            System.out.println(table2.toLowerCase());
                            System.out.println("Inserta la id que quieres buscar: ");
                            int id = Integer.parseInt(mainController.lecturaNombre());
                            mainController.selectByIdFromTable(table2,id);
                            break;
                        case 5:
                            System.out.println("Saliendo de este menu");
                            break;
                    }

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
                            mainController.eliminarRegistro(mainController.lecturaNombre());
                            break;
                        case 3:
                            mainController.showTableNames();
                            mainController.eliminarRegistro(mainController.lecturaNombre());
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