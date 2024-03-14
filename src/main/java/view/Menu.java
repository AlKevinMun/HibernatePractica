package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Table;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class Menu {

    private int option;

    public Menu() {
        super();
    }

    public int mainMenu() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        do {
            System.out.println("\nMENU PRINCIPAL\n");
            System.out.println("1. Mostrar tablas"); // - FINALIZADO -
            System.out.println("2. Realizar consultas"); // SubMenu para las consultas
            System.out.println("3. Crear datos"); // - FINALIZADO -
            System.out.println("4. Modificar datos"); // - FINALIZADO -
            System.out.println("5. Borrar datos"); // SubMenu para borrar datos
            System.out.println("6. Poblar tablas"); // - FINALIZADO -
            System.out.println("7. Borrar tablas"); // - FINALIZADO -
            System.out.println("8. Salir"); // - FINALIZADO -
            System.out.print("Escoge una opción: ");
            try {
                option = Integer.parseInt(br.readLine());
            } catch (NumberFormatException | IOException e) {
                System.out.println("Valor no válido");
                e.printStackTrace();
            }
        } while (option < 1 || option > 8);
        return option;
    }

    /*
    public int selectTable() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\nSELECCIONA UNA TABLA\n");
        System.out.println("1. Commander");
        System.out.println("2. Map");
        System.out.println("3. Player");
        System.out.println("4. Games");
        System.out.println("5. Salir");
        System.out.print("Escoge una opción: ");
        option = Integer.parseInt(br.readLine());
        return option;
    }
     */

    public int menuDelete(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        do {
            System.out.println("\nSUBMENU BORRAR DATOS\n");
            System.out.println("1. Eliminar una entidad"); //
            System.out.println("2. Eliminar un registro"); //
            System.out.println("3. Eliminar conjunto registros"); //
            System.out.println("4. Salir"); // - FINALIZADO -
            System.out.print("Escoge una opción: ");
            try {
                option = Integer.parseInt(br.readLine());
            } catch (NumberFormatException | IOException e) {
                System.out.println("Valor no válido");
                e.printStackTrace();
            }
        } while (option < 1 || option > 4);
        return option;
    }



}


