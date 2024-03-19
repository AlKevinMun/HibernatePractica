package controller;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import model.Commander;


import model.Player;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jboss.jandex.Main;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
 * Controller para la clase Player.
 */
public class PlayerController {
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
    public PlayerController(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.entityManager = entityManagerFactory.createEntityManager();
        this.session = this.entityManager.unwrap(Session.class);
    }

    /**
     * Este método se trata de uno que se basa en leer un fichero CSV y convertir los datos del mismo fichero en clases
     * dentro del propio Java.
     * @return Devuelve la lista de Players que extrae del documento CSV
     * @throws FileNotFoundException Excepcion que salta si no se encuentra el fichero
     * @throws SQLException Excepcion
     */
    public ArrayList<Player> readDataFromCSV() throws IOException, SQLException, CsvValidationException {
        CSVReader reader = new CSVReader(new FileReader("src/main/resources/player.csv"));
        String[] data = null;
        reader.readNext();
        ArrayList<Player> players = new ArrayList<>();
        while ((data = reader.readNext()) != null) {
            String player_name = data[0];
            String last_activity = data[1];
            String official_ratting = data[2];
            String WLD = data[3];
            String[] temporal = data[4].replaceAll(",", "").split(" ");
            String commanderName = temporal[0].replaceFirst("\\[", "").replaceFirst("]", "");
            int winrate = 0;
            Pattern patron = Pattern.compile("\\d+");
            Matcher matcher = patron.matcher(temporal[2]);

            if (matcher.find()) {
                String temp = matcher.group();
                winrate = Integer.parseInt(temp);
            }

            System.out.println(commanderName);

            Commander commander = findCommanderByName(commanderName);

            if (commander == null) {
                // Manejar el caso donde no se encontró un Commander en la base de datos
                // Por ejemplo, puedes lanzar una excepción o crear un nuevo Commander
                throw new RuntimeException("No se encontró un comandante con el nombre: " + commanderName);
            }

            Player player = new Player(player_name, last_activity, official_ratting, WLD,winrate,commander);
            players.add(player);
        }
        return players;
    }

    /**
     * Método para que a partir del nombre de un comandante, buscarlo dentro de los players y si existe pasarlo.
     * @param commanderName Nombre del commandante que se busca
     * @return clase commander
     */
    public Commander findCommanderByName(String commanderName) {
        try {
            return entityManager.createQuery("SELECT c FROM Commander c WHERE c.commanderName = :name", Commander.class)
                    .setParameter("name", commanderName)
                    .getSingleResult();
        } catch (NoResultException e) {
            // Manejar el caso donde no se encontró un Commander en la base de datos
            return null;
        }
    }

    /**
     * Método para que a partir de un comandante, encontrar que player hace uso de este.
     * @param commander Objeto commandante que se va a buscar
     * @return Devuelve la querry con el resultado.
     */
    public List<Player> findPlayersByCommander(Commander commander) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Player> criteriaQuery = criteriaBuilder.createQuery(Player.class);
            Root<Player> root = criteriaQuery.from(Player.class);
            Join<Player, Commander> commanderJoin = root.join("commander");
            criteriaQuery.select(root).where(criteriaBuilder.equal(commanderJoin.get("commanderName"), commander.getCommanderName()));
            TypedQuery<Player> query = entityManager.createQuery(criteriaQuery);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }
    /**
     * Método para mostrar por pantalla el nombre de todos los players existentes dentro de la base de datos.
     */
    public void showPlayerNames() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            int numNombres = 1;

            // Consulta para obtener los nombres de los comandantes
            Query<String> query = session.createNativeQuery("SELECT player_name FROM player");
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
