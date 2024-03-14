package controller;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import model.Commander;


import model.Player;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jboss.jandex.Main;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
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

public class PlayerController {
    EntityManagerFactory entityManagerFactory;
    private final EntityManager entityManager;
    private final Session session;


    public PlayerController(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.entityManager = entityManagerFactory.createEntityManager();
        this.session = this.entityManager.unwrap(Session.class);
    }


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
