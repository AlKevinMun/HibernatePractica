package controller;

import model.Commander;
import model.Game;
import model.Map;
import model.Player;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.*;

public class MainController {
    private EntityManagerFactory entityManagerFactory;
    @PersistenceContext
    private final EntityManager entityManager;
    private final Session session;
    CommanderController commanderController; // = new CommanderController(entityManagerFactory);
    PlayerController playerController; //7 = new PlayerController(entityManagerFactory);
    MapController mapController;
    GameController gameController;

    public MainController(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.entityManager = entityManagerFactory.createEntityManager();
        this.session = this.entityManager.unwrap(Session.class);

        this.commanderController = new CommanderController(entityManagerFactory);
        this.playerController = new PlayerController(entityManagerFactory);
        this.mapController = new MapController(entityManagerFactory);
        this.gameController = new GameController(entityManagerFactory);
    }


    public void showTableNames() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            int numTablas = 1;

            // Consulta para obtener los nombres de todas las tablas
            Query<String> query = session.createNativeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'");
            List<String> tableNames = query.getResultList();

            // Imprimir los nombres de las tablas
            System.out.println("\nLas tablas en la base de datos son:");
            System.out.println("");
            for (String tableName : tableNames) {
                System.out.println(numTablas+" . " + tableName);
                numTablas++;
            }

            em.close();
            //entityManagerFactory.close(); <-- Esta linea cierra toda la conexión con la base de datos.
        } catch (HibernateException ex) {
            System.out.println("Error al mostrar las tablas: " + ex.getMessage());
        }
    }

    public void showAttributeNames(String attName) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            // Consulta para obtener los nombres de todas las tablas
            Query<String> query = session.createNativeQuery("SELECT column_name FROM information_schema.columns  WHERE table_name = '" + attName+"'");
            List<String> tableNames = query.getResultList();

            // Imprimir los nombres de las tablas
            System.out.println("\nLas tablas en la base de datos son:");
            System.out.println("");
            for (String tableName : tableNames) {
                System.out.println(" - "+tableName);
            }

            em.close();

            System.out.println("No se encontró la tabla " + attName);
        } catch (Exception ex) {
            System.out.println("Error al mostrar los atributos: " + ex.getMessage());
        } finally {
            em.close();
        }
    }

    public void insertar(Object object){
        EntityManager em = entityManagerFactory.createEntityManager();
        Session session1 = em.unwrap(Session.class);
        session1.getTransaction().begin();
        session1.save(object);
        session1.getTransaction().commit();
        session1.clear();
        session1.close();
    }
    public void delete(Object object){
        EntityManager em = entityManagerFactory.createEntityManager();
        Session session1 = em.unwrap(Session.class);
        session1.getTransaction().begin();
        if (!session1.contains(object)) {
            object = session1.merge(object); // Reasociar la entidad con la sesión si está desconectada
        }

        session1.delete(object);
        session1.getTransaction().commit();
        session1.clear();
        session1.close();
    }
    public void update(Object object){
        EntityManager em = entityManagerFactory.createEntityManager();
        Session session1 = em.unwrap(Session.class);
        session1.getTransaction().begin();
        session1.update(object);
        session1.getTransaction().commit();
        session1.clear();
        session1.close();
    }

    public void editarEntidad(String option) {
        Scanner scanner = new Scanner(System.in);
        if (option.equals("commander")) {
            commanderController.showCommanderNames();
            System.out.println("Escribe el nombre del comandante que deseas editar: ");
            String comandanteNombre = scanner.nextLine();
            Commander commanderExistente = playerController.findCommanderByName(comandanteNombre);
            if (commanderExistente != null) {
                System.out.println("Escribe el nuevo nombre para el comandante: ");
                String nuevoNombre = scanner.nextLine();
                commanderExistente.setCommanderName(nuevoNombre);
                update(commanderExistente);
                System.out.println("Comandante editado correctamente.");
            } else {
                System.out.println("Comandante no encontrado.");
            }
        } else if (option.equals("player")) {
            playerController.showPlayerNames();
            System.out.println("Escribe el nombre del jugador que deseas editar: ");
            String jugadorNombre = scanner.nextLine();
            Player jugadorExistente = gameController.findPlayerByName(jugadorNombre);
            if (jugadorExistente != null) {
                showAttributeNames(option);
                System.out.println("Inserta que atributo quieres modificar: ");
                String att = scanner.nextLine();
                if (att.equals("player_name")){
                    System.out.println("Escribe el nuevo nombre de ususario: ");
                    String name = scanner.nextLine();
                    jugadorExistente.setName(name);
                } else if (att.equals("last_activity")) {
                    System.out.println("Inserta el nuevo valor: ");
                    String valor = scanner.nextLine();
                    jugadorExistente.setLastActivity(valor);
                } else if (att.equals("official_rating")) {
                    System.out.println("Inserta el nuevo valor: ");
                    String valor = scanner.nextLine();
                    jugadorExistente.setOfficialRating(valor);
                } else if (att.equals("wld")) {
                    System.out.println("Inserta el nuevo valor: ");
                    String valor = scanner.nextLine();
                    jugadorExistente.setWld(valor);
                } else if (att.equals("winrate")) {
                    System.out.println("Inserta el nuevo valor: ");
                    int valor = scanner.nextInt();
                    scanner.nextLine();
                    jugadorExistente.setWinrate(valor);
                }
                update(jugadorExistente);
                System.out.println("Jugador editado correctamente.");
            } else {
                System.out.println("Jugador no encontrado.");
            }
        } else if (option.equals("map")) {
            mapController.showMapNames();
            System.out.println("Escribe el nombre del mapa que deseas editar: ");
            String nombreMapa = scanner.nextLine();
            Map mapaExistente = gameController.findMapByName(nombreMapa);
            if (mapaExistente != null) {
                showAttributeNames(option);
                System.out.println("Inserta que atributo quieres modificar: ");
                String att = scanner.nextLine();
                if (att.equals("map_name")){
                    System.out.println("Inserta el nuevo nombre del mapa: ");
                    String nuevoNombreMapa = scanner.nextLine();
                    mapaExistente.setMapName(nuevoNombreMapa);
                } else if (att.equals("creator")) {
                    System.out.println("Inserta el nuevo nombre del creador: ");
                    String nuevoNombreCreador = scanner.nextLine();
                    mapaExistente.setCreator(nuevoNombreCreador);
                } else if (att.equals("official_rating")) {
                    System.out.println("Inserta el nuevo máximo de jugadores (número): ");
                    int nuevoMaxPlayers = scanner.nextInt();
                    scanner.nextLine();
                    mapaExistente.setMaxPlayers(nuevoMaxPlayers);
                } else if (att.equals("wld")) {
                    System.out.println("Inserta el nuevo tamaño del mapa (nXn): ");
                    String nuevoSize = scanner.nextLine();
                    mapaExistente.setSize(nuevoSize);
                }

                update(mapaExistente);
                System.out.println("Mapa editado correctamente.");
            } else {
                System.out.println("Mapa no encontrado.");
            }
        } else if (option.equals("games")) {
            gameController.showGamesNames();
            System.out.println("Escribe el nombre del juego que deseas editar: ");
            String nombreJuego = scanner.nextLine();
            Game juegoExistente = gameController.findGameByName(nombreJuego);
            if (juegoExistente != null) {
                System.out.println("Inserta que atributo quieres modificar ('map' para mapa, 'player' para jugador): ");
                String att = scanner.nextLine();
                switch (att) {
                    case "map":
                        mapController.showMapNames();
                        System.out.println("Inserta el nuevo nombre del mapa: ");
                        String nuevoNombreMapa = scanner.nextLine();
                        Map nuevoMapa = gameController.findMapByName(nuevoNombreMapa);
                        if (nuevoMapa != null) {
                            juegoExistente.setMap(nuevoMapa);
                            System.out.println("Mapa editado correctamente.");
                        } else {
                            System.out.println("Mapa no encontrado.");
                        }
                        break;
                    case "player":
                        playerController.showPlayerNames();
                        System.out.println("Inserta el nuevo nombre del jugador: ");
                        String nuevoNombreJugador = scanner.nextLine();
                        Player nuevoJugador = gameController.findPlayerByName(nuevoNombreJugador);
                        if (nuevoJugador != null) {
                            Set<Player> nuevosJugadores = new HashSet<>();
                            nuevosJugadores.add(nuevoJugador);
                            juegoExistente.setPlayers(nuevosJugadores);
                            System.out.println("Jugador editado correctamente.");
                        } else {
                            System.out.println("Jugador no encontrado.");
                        }
                        break;
                    default:
                        System.out.println("Atributo no válido.");
                        break;
                }
                update(juegoExistente);
            } else {
                System.out.println("Juego no encontrado.");
            }
        }
        else {
            System.out.println("Opción no válida.");
        }
    }


    public void eliminarEntidad(String option){
        Scanner scanner = new Scanner(System.in);
        if(option.equals("commander")){
            commanderController.showCommanderNames();
            System.out.println("Inserta el nombre de la entidad que quieres borrar: ");
            String nombre = scanner.nextLine();
            delete(playerController.findCommanderByName(nombre));
        } else if (option.equals("map")) {
            mapController.showMapNames();
            System.out.println("Inserta el nombre de la entidad que quieres borrar: ");
            String nombre = scanner.nextLine();
            delete(gameController.findMapByName(nombre));
        } else if (option.equals("player")) {
            playerController.showPlayerNames();
            System.out.println("Inserta el nombre de la entidad que quieres borrar: ");
            String nombre = scanner.nextLine();
            delete(gameController.findPlayerByName(nombre));
        } else if (option.equals("games")) {
            gameController.showGamesNames();
            System.out.println("Inserta el nombre de la entidad que quieres borrar: ");
            String nombre = scanner.nextLine();
            delete(gameController.findGameByName(nombre));
        }
    }
    public void poblarTablas() throws SQLException, FileNotFoundException {
        try {

            ArrayList<Commander> commanders = commanderController.readDataFromCSV();
            for (int i = 0; i < commanders.size(); i++) {
                insertar(commanders.get(i));
            }
            ArrayList<Player> players = playerController.readDataFromCSV();
            for (int i = 0; i < players.size(); i++) {
                insertar(players.get(i));
            }
            ArrayList<Map> maps = mapController.readDataFromCSV();
            for (int i = 0; i < maps.size(); i++) {
                insertar(maps.get(i));
            }
            ArrayList<Game> games = gameController.readDataFromCSV();
            for (int i = 0; i < games.size(); i++) {
                insertar(games.get(i));
            }

        } catch (Exception e) {
            //session.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public void eliminarTabla(String nombreTabla) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Session session1 = em.unwrap(Session.class);
        try {
            session1.getTransaction().begin();
            session1.doWork(connection -> {
                String sql = "DROP TABLE IF EXISTS " + nombreTabla+" CASCADE";
                connection.createStatement().execute(sql);
            });
            session1.getTransaction().commit();
        } catch (HibernateException e) {
            if (session1.getTransaction() != null && session1.getTransaction().isActive()) {
                session1.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (session1 != null && session1.isOpen()) {
                session1.close();
            }
        }
    }
    
    public void crearEntidad(String option){
        Scanner scanner = new Scanner(System.in);
        if (option.equals("commander")){
            System.out.println("Escribe el nombre para el comandante: ");
            Commander commander = new Commander(lecturaNombre());
            insertar(commander);
        } else if (option.equals("player")) {
            System.out.println("Escribe el nombre del jugador: ");
            String nombre = scanner.nextLine();
            System.out.println("Escribe la fecha de la ultima vez que se conecto (mm/dd/yyyy): ");
            String lastActivity = scanner.nextLine();
            System.out.println("Escribe el rango: ");
            String officialRatting = scanner.nextLine();
            System.out.println("Escribe su WLD(n-n.n): ");
            String wld = scanner.nextLine();
            System.out.println("Escribe su Winrate: ");
            int winrate = scanner.nextInt();
            scanner.nextLine();
            commanderController.showCommanderNames();
            System.out.println("Escribe el nombre del Comandante: ");
            String comName = scanner.nextLine();
            Commander commander = playerController.findCommanderByName(comName);

            System.out.println("Creando el jugador...");
            Player player = new Player(nombre,lastActivity,officialRatting,wld,winrate,commander);
            insertar(player);

        } else if (option.equals("map")) {
            System.out.println("Inserta el nombre del mapa: ");
            String mapName = scanner.nextLine();
            System.out.println("Inserta el nombre del creador: ");
            String creatorName = scanner.nextLine();
            System.out.println("Inserta el maximo de jugadores (numero): ");
            int maxPlayers = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Inserta el tamaño del mapa (nXn): ");
            String size = scanner.nextLine();

            System.out.println("Creando mapa...: ");
            Map map = new Map(mapName,creatorName,maxPlayers,size);
            insertar(map);
            
        } else if (option.equals("games")) {
            System.out.println("Inserta el nombre de la partida:  ");
            String gameName = scanner.nextLine();
            mapController.showMapNames();
            System.out.println("Inserta el nombre del mapa: ");
            String mapName = scanner.nextLine();
            Map map = gameController.findMapByName(mapName);
            playerController.showPlayerNames();
            System.out.println("Inserta el jugador 1: ");
            String player1 = scanner.nextLine();
            playerController.showPlayerNames();
            System.out.println("Inserta el jugador 2: ");
            String player2 = scanner.nextLine();
            Set<Player> playerSet = new HashSet<>();
            playerSet.add(gameController.findPlayerByName(player1));
            playerSet.add(gameController.findPlayerByName(player2));

            Game game = new Game(gameName, map, playerSet);
            insertar(game);
        }
    }

    public String lecturaNombre(){
        Scanner scanner = new Scanner(System.in);
        String palabra = scanner.next();
        return palabra;
    }
}
