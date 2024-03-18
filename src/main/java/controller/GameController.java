package controller;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import model.Games;
import model.Map;
import model.Player;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameController {

    EntityManagerFactory entityManagerFactory;
    private final EntityManager entityManager;
    private final Session session;



    public GameController(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.entityManager = entityManagerFactory.createEntityManager();
        this.session = this.entityManager.unwrap(Session.class);
    }

    public ArrayList<Games> readDataFromCSV() throws IOException, SQLException, CsvValidationException {
        CSVReader reader = new CSVReader(new FileReader("src/main/resources/games.csv"));
        String[] data = null;
        reader.readNext();
        ArrayList<Games> games = new ArrayList<>();
        while ((data = reader.readNext()) !=null){
            String game_name = data[0];
            String map_name = data[1];
            String players[] = data[2].split(",");
            String player1 = players[0];
            String player2 = players[1];



            Map map = findMapByName(map_name);

            Set<Player> playerSet = new HashSet<>();
            for (int i = 0; i < players.length; i++) {
                Player player = findPlayerByName(players[i]);
                playerSet.add(player);
            }

            if (map == null ) {
                // Manejar el caso donde no se encontró un Commander en la base de datos
                // Por ejemplo, puedes lanzar una excepción o crear un nuevo Commander
                throw new RuntimeException("No se encontró un comandante con el nombre: " + map_name);
            }

            Games game = new Games(game_name, map, playerSet);
            games.add(game);

        }
        return games;
    }

    public Map findMapByName(String mapName) {
        try {
            return entityManager.createQuery("SELECT c FROM Map c WHERE c.mapName = :name", Map.class)
                    .setParameter("name", mapName)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Player findPlayerByName(String playerName) {
        try {
            return entityManager.createQuery("SELECT c FROM Player c WHERE c.name = :name", Player.class)
                    .setParameter("name", playerName)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    public Games findGameByName(String gameName) {
        try {
            return entityManager.createQuery("SELECT c FROM Game c WHERE c.name = :name", Games.class)
                    .setParameter("name", gameName)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Games> findGamesByMap(Map map) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Games> criteriaQuery = criteriaBuilder.createQuery(Games.class);
            Root<Games> root = criteriaQuery.from(Games.class);
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("map"), map));
            TypedQuery<Games> query = entityManager.createQuery(criteriaQuery);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

    public List<Games> findGamesByPlayer(Player player) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Games> criteriaQuery = criteriaBuilder.createQuery(Games.class);
            Root<Games> root = criteriaQuery.from(Games.class);
            root.fetch("players"); // Cargar explícitamente la colección de jugadores
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.join("players"), player));
            TypedQuery<Games> query = entityManager.createQuery(criteriaQuery);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

    public void showGamesNames() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            int numNombres = 1;

            // Consulta para obtener los nombres de los comandantes
            Query<String> query = session.createNativeQuery("SELECT game_name FROM games");
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
