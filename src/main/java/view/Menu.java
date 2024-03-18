package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Clase del menu donde se encuentran todos los menus con datos estaticos.
 */
public class Menu {
    /**
     * Variable donde se almacena la elección del usuario
     */
    private int option;

    public Menu() {
        super();
    }

    /**
     * Metodo donde se muestra el menu principal del programa.
     * @return Devuelve el valor que el usuario a seleccionado
     */
    public int mainMenu() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        do {
            System.out.println("\nMENU PRINCIPAL\n");
            System.out.println("1. Mostrar tablas"); // - FINALIZADO -
            System.out.println("2. Realizar consultas"); // SubMenu para las consultas
            System.out.println("3. Crear datos"); // - FINALIZADO -
            System.out.println("4. Modificar datos"); // - FINALIZADO -
            System.out.println("5. Borrar datos"); // - FINALIZADO -
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

    /**
     * Metodo donde se muestra el sub-menu de los deletes.
     * @return Devuelve el valor que el usuario a seleccionado
     */
    public int menuDelete(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        do {
            System.out.println("\nSUBMENU BORRAR DATOS\n");
            System.out.println("1. Eliminar una entidad"); // - FINALIZADO - (Hay casos no funcionales)
            System.out.println("2. Eliminar un registro"); // - FINALIZADO -
            System.out.println("3. Eliminar conjunto registros"); // - FINALIZADO -
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
    /**
     * Metodo donde se muestra el sub-menu de los selects.
     * @return Devuelve el valor que el usuario a seleccionado
     */
    public int menuQuerry(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        do {
            System.out.println("\nSUBMENU CONSULTAR DATOS\n");
            System.out.println("1. Consulta toda una tabla"); // - FINALIZADO -
            System.out.println("2. Consulta texto concreto"); // -
            System.out.println("3. Consulta condicion"); // -
            System.out.println("4. Consulta elemento especifico"); // -
            System.out.println("5. Salir"); // - FINALIZADO -
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
    /**
     * Metodo donde se muestran todas las condiciones a seleccionar.
     */
    public void showConditions() {
        System.out.println("1. Igual (=)");
        System.out.println("2. No igual (!=)");
        System.out.println("3. Menor que (<)");
        System.out.println("4. Menor o igual que (<=)");
        System.out.println("5. Mayor que (>)");
        System.out.println("6. Mayor o igual que (>=)");
    }

}


