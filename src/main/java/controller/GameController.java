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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * Controller para la clase Game.
 */
public class GameController {
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
    public GameController(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.entityManager = entityManagerFactory.createEntityManager();
        this.session = this.entityManager.unwrap(Session.class);
    }

    /**
     * Este Método se trata de uno que se basa en leer un fichero CSV y convertir los datos del mismo fichero en clases
     * dentro del propio Java.
     * @return Devuelve la lista de games que extrae del documento CSV
     * @throws FileNotFoundException Excepcion que salta si no se encuentra el fichero
     * @throws SQLException Excepcion
     */
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

    /**
     * Método para que a partir del nombre de un mapa, buscarlo dentro de los mapas y si existe pasarlo.
     * @param mapName Nombre del mapa que se busca
     * @return clase Map
     */
    public Map findMapByName(String mapName) {
        try {
            return entityManager.createQuery("SELECT c FROM Map c WHERE c.mapName = :name", Map.class)
                    .setParameter("name", mapName)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Método para que a partir del nombre de un player, buscarlo en un game, y si esta, devolverlo
     * @param playerName Nomber del player
     * @return clase Player
     */
    public Player findPlayerByName(String playerName) {
        try {
            return entityManager.createQuery("SELECT c FROM Player c WHERE c.name = :name", Player.class)
                    .setParameter("name", playerName)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Método para que a partir del nombre de un game, buscarlo, y si esta, devolverlo
     * @param gameName Nomber del game
     * @return clase Game
     */
    public Games findGameByName(String gameName) {
        try {
            return entityManager.createQuery("SELECT c FROM Game c WHERE c.name = :name", Games.class)
                    .setParameter("name", gameName)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Método para que a partir de un mapa, buscar los juegos donde se usa este
     * @param map Clase mapa que se está buscando
     * @return Devuelve el juego donde se encuentra.
     */
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
    /**
     * Método para que a partir de un player, buscar los juegos donde se usa este
     * @param player Clase player que se está buscando
     * @return Devuelve el juego donde se encuentra.
     */
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
    /**
     * Método para mostrar por pantalla el nombre de todos los games existentes dentro de la base de datos.
     */
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
