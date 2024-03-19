package controller;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import model.Commander;
import model.Map;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Controller para la clase Map.
 */
public class MapController {
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
    public MapController(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.entityManager = entityManagerFactory.createEntityManager();
        this.session = this.entityManager.unwrap(Session.class);
    }
    /**
     * Este Método se trata de uno que se basa en leer un fichero CSV y convertir los datos del mismo fichero en clases
     * dentro del propio Java.
     * @return Devuelve la lista de Maps que extrae del documento CSV
     * @throws FileNotFoundException Excepcion que salta si no se encuentra el fichero
     * @throws SQLException Excepcion
     */
    public ArrayList<Map> readDataFromCSV() throws IOException, SQLException, CsvValidationException {
        CSVReader reader = new CSVReader(new FileReader("src/main/resources/map.csv"));
        String[] data = null;
        reader.readNext();
        ArrayList<Map> maps = new ArrayList<>();
        while ((data = reader.readNext()) != null) {
            String map_name = data[0];
            String creator = data[1];
            int max_player = Integer.parseInt(data[2]);
            String size = data[3];

            Map map = new Map(map_name, creator, max_player, size);
            maps.add(map);

        }
        return maps;
    }
    /**
     * Método para mostrar por pantalla el nombre de todos los mapas existentes dentro de la base de datos.
     */
    public void showMapNames() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            int numNombres = 1;

            // Consulta para obtener los nombres de los comandantes
            Query<String> query = session.createNativeQuery("SELECT map_name FROM map");
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
