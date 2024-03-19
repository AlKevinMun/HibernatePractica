package controller;

import model.Commander;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jboss.jandex.Main;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controller para la clase Commander.
 */
public class CommanderController {
    /**
     * Atributo requerido para que Hibernate funcione correctamente.
     */
    EntityManagerFactory entityManagerFactory;
    /**
     * Atributo requerido para que Hibernate funcione correctamente.
     */
    private final EntityManager entityManager;
    /**
     * Atributo requerido para que Hibernate funcione correctamente.
     */
    private final Session session;

    /**
     * Constructor del controlador donde se instancian todas las clases necesarias para Hibernte
     * @param entityManagerFactory La clase necesaria para poder crear un EntityManager-
     */
    public CommanderController(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.entityManager = entityManagerFactory.createEntityManager();
        this.session = this.entityManager.unwrap(Session.class);
    }

    /**
     * Este Método se trata de uno que se basa en leer un fichero CSV y convertir los datos del mismo fichero en clases
     * dentro del propio Java.
     * @return Devuelve la lista de commandantes que extrae del documento CSV
     * @throws FileNotFoundException Excepcion que salta si no se encuentra el fichero
     * @throws SQLException Excepcion
     */
    public ArrayList<Commander> readDataFromCSV() throws FileNotFoundException, SQLException {
        Scanner scanner= new Scanner(new File("src/main/resources/player.csv"));
        ArrayList<String> data = new ArrayList<>();
        ArrayList<String> finalData = new ArrayList<>();
        ArrayList<Commander> commanders = new ArrayList<>();
        int i = 0;
        while(scanner.hasNext()){
            data.add(scanner.nextLine());
            i++;
        }
        scanner.close();
        for (int j = 0; j < data.size(); j++) {
            System.out.println(data.get(j));
        }
        String[] separador1 = data.get(1).replaceAll(",","").split(" ");
        for (int j = 0; j < separador1.length; j++) {
            if (j%3==0){
                if (j==0) {
                    Pattern patron = Pattern.compile("\"\\[([^\"\\]]+)");
                    Matcher matcher = patron.matcher(separador1[j]);
                    if (matcher.find()) {
                        String resultado = matcher.group(1);
                        finalData.add(resultado);
                    }
                }
                else finalData.add(separador1[j]);
            }
        }
        Collections.sort(finalData);

        for (int j = 0; j < finalData.size(); j++) {
            Commander commander = new Commander(finalData.get(j));
            commanders.add(commander);
        }
        return commanders;
    }

    /**
     * Método para mostrar por pantalla el nombre de todos los commandantes existentes dentro de la base de datos.
     */
    public void showCommanderNames() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            int numNombres = 1;

            // Consulta para obtener los nombres de los comandantes
            Query<String> query = session.createNativeQuery("SELECT commander_name FROM commander");
            List<String> tableNames = query.getResultList();

            // Imprimir los nombres de los comandantes
            System.out.println("\nLas tablas en la base de datos son:");
            System.out.println("");
            for (String tableName : tableNames) {
                System.out.println(numNombres+" . " + tableName);
                numNombres++;
            }

            em.close();
            //entityManagerFactory.close(); <-- Esta linea cierra toda la conexión con la base de datos.
        } catch (HibernateException ex) {
            System.out.println("Error al mostrar las tablas: " + ex.getMessage());
        }
    }
}
