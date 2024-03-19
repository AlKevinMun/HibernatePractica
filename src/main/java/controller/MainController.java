package controller;

import model.Commander;
import model.Games;
import model.Map;
import model.Player;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.*;

/**
 * Esta clase se trata del MainController. En este controller es donde se encuentra casi toda la logica del programa.
 */
public class MainController {
    /**
     * Atributo requerido para que Hibernate funcione correctamente.
     */
    private EntityManagerFactory entityManagerFactory;
    /**
     * Atributo requerido para que Hibernate funcione correctamente.
     */
    @PersistenceContext
    private final EntityManager entityManager;
    /**
     * Atributo requerido para que Hibernate funcione correctamente.
     */
    private final Session session;
    /**
     * Instancia del commanderController
     */
    CommanderController commanderController;
    /**
     * Instancia del playerController
     */
    PlayerController playerController;
    /**
     * Instancia del mapController
     */
    MapController mapController;
    /**
     * Instancia del gameController
     */
    GameController gameController;

    /**
     * Constructor del controlador donde se instancian todas las clases necesarias para Hibernte, además de crear las instancias con datos de los controllers.
     * @param entityManagerFactory La clase necesaria para poder crear un EntityManager
     */
    public MainController(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.entityManager = entityManagerFactory.createEntityManager();
        this.session = this.entityManager.unwrap(Session.class);

        this.commanderController = new CommanderController(entityManagerFactory);
        this.playerController = new PlayerController(entityManagerFactory);
        this.mapController = new MapController(entityManagerFactory);
        this.gameController = new GameController(entityManagerFactory);
    }

    /**
     * Este Método lo que hace es realizar un select para obtener el nombre de todas las tablas de la base de datos, y mostrar por pantalla los mismos
     */
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
    /**
     * Este Método lo que hace es realizar un select para obtener el nombre de todos los atributos de la tabla que se proporciona
     * @param attName Nombre de la tabla en al que buscar los atributos
     */
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
    /**
     * Método que hace un uso de Hibernate para que a partir de cualquier objeto que se le pase, este lo inserte a la base
     * de datos de forma inteligente, sabiendo en que tabla va, y añadiendo todo sin ningun problema.
     * @param object Cualquier objeto. En este caso se usan objetos como Commander, Player, etc.
     */
    public void insertar(Object object){
        EntityManager em = entityManagerFactory.createEntityManager();
        Session session1 = em.unwrap(Session.class);
        session1.getTransaction().begin();
        session1.save(object);
        session1.getTransaction().commit();
        session1.clear();
        session1.close();
    }
    /**
     * Método que hace un uso de Hibernate para que a partir de cualquier objeto que se le pase, este lo borrara de la base
     * de datos de forma inteligente.
     * @param object Cualquier objeto. En este caso se usan objetos como Commander, Player, etc.
     */
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
    /**
     * Método que hace un uso de Hibernate para que a partir de cualquier objeto que se le pase, este le un update
     *  dentro de datos de forma inteligente.
     * @param object Cualquier objeto. En este caso se usan objetos como Commander, Player, etc.
     */
    public void update(Object object){
        EntityManager em = entityManagerFactory.createEntityManager();
        Session session1 = em.unwrap(Session.class);
        session1.getTransaction().begin();
        session1.update(object);
        session1.getTransaction().commit();
        session1.clear();
        session1.close();
    }
    /**
     * Método para editar una de las entidades de la base de datos. Su funcionamiento es muy simple. Seleccionas que
     * entidad quieres editar y vas rellenando todos los nuevos valores para editar este.
     * @param option Nombre de la tabla donde quieres interactuar
     */
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
            Games juegoExistente = gameController.findGameByName(nombreJuego);
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

    /**
     * Método para eliminar toda una entidad de la base de datos. Su funcionamiento es un tanto complejo dependiendo de
     * la tabla origen de la entidad. Lo que hace es leer el nombre de la entidad a borrar, y si existe, borra primero
     * todas las referencias que tiene en otras tablas, y tras eso la borra de su tabla.
     * @param option Nombre de la tabla donde quieres interactuar
     */
    public void eliminarEntidad(String option) {
        Scanner scanner = new Scanner(System.in);

        if(option.equals("commander")) {
            commanderController.showCommanderNames();
            System.out.println("Inserta el nombre del comandante que deseas borrar: ");
            String commanderName = scanner.nextLine();
            Commander commander = playerController.findCommanderByName(commanderName);

            // Buscar y eliminar todas las referencias al comandante en la tabla player
            List<Player> playersReferencingCommander = playerController.findPlayersByCommander(commander);
            for (Player player : playersReferencingCommander) {
                player.setCommander(null); // Eliminar la referencia al comandante en el jugador
                update(player); // Actualizar el jugador
            }

            // Ahora puedes eliminar el comandante
            delete(commander);
        } else if (option.equals("map")) {
            mapController.showMapNames();
            System.out.println("Inserta el nombre de la entidad que quieres borrar: ");
            String nombre = scanner.nextLine();
            Map map = gameController.findMapByName(nombre);
            List<Games> gamesReferencingMap = gameController.findGamesByMap(map);
            if (gamesReferencingMap.isEmpty()) {
                delete(map);
            } else {
                for (Games games : gamesReferencingMap) {
                    games.setMap(null);
                    update(games);
                }
                delete(map);
            }
        } else if (option.equals("player")) {
            playerController.showPlayerNames();
            System.out.println("Inserta el nombre del jugador que deseas borrar: ");
            String playerName = scanner.nextLine();
            Player player = gameController.findPlayerByName(playerName);

            // Buscar y eliminar todas las referencias al jugador en la tabla game_players
            List<Games> gamesReferencingPlayer = gameController.findGamesByPlayer(player);
            for (Games games : gamesReferencingPlayer) {
                games.getPlayers().remove(player); // Eliminar al jugador de la lista de jugadores del juego
                update(games); // Actualizar el juego
            }

            // Ahora puedes eliminar al jugador
            delete(player);
        } else if (option.equals("games")) {
            gameController.showGamesNames();
            System.out.println("Inserta el nombre del juego que deseas borrar: ");
            String gameName = scanner.nextLine();
            Games games = gameController.findGameByName(gameName);

            Map map = games.getMap();
            if (map != null) {
                List<Games> gamesReferencingMap = gameController.findGamesByMap(map);
                for (Games referencingGames : gamesReferencingMap) {
                    referencingGames.setMap(null); // Eliminar la referencia al mapa en el juego
                    update(referencingGames); // Actualizar el juego
                }
            }

            // Eliminar las filas de game_players que corresponden a la partida a eliminar
            javax.persistence.Query query = entityManager.createQuery("DELETE FROM GamePlayer gp WHERE gp.game.id = :gameId");
            query.setParameter("gameId", games.getId());
            int rowsAffected = query.executeUpdate();
            System.out.println(rowsAffected + " filas eliminadas de game_players.");

            // Finalmente, eliminar el juego
            delete(games);
        }
    }
    /**
     * Método para eliminar un registro concreto en al base de datos. No es tan complejo como la entidad. Lo primero que
     * haces al igual que con eliminar la entidad es elegir sobre que elemento quieres realizar el borrado. Tras eso
     * Eliges el atributo de la entidad, y este lo que hace es dejarlo en blanco.
     * @param option Nombre de la tabla donde quieres interactuar
     */
    public void eliminarRegistro(String option){
        Scanner scanner = new Scanner(System.in);
        showAttributeNames(option);
        System.out.println("Inserta el registro a eliminar: ");
        String att = scanner.nextLine();
        if (option.equals("commander")){
            commanderController.showCommanderNames();
            System.out.println("En cual quieres eliminar el registro: ");
            String reg = scanner.nextLine();
            Commander commanderExistente = playerController.findCommanderByName(reg);
            if (att.equals("commander_name")) {
                commanderExistente.setCommanderName("");
            }
            update(commanderExistente);
        } else if (option.equals("map")) {
            mapController.showMapNames();
            System.out.println("En cual quieres eliminar el registro: ");
            String reg = scanner.nextLine();
            Map map = gameController.findMapByName(reg);
            if (att.equals("map_name")) {
                map.setMapName("");
            } else if (att.equals("creator")) {
                map.setCreator("");
            } else if (att.equals("max_players")) {
                map.setMaxPlayers(0);
            } else if (att.equals("size")) {
                map.setSize("");
            }
            update(map);
        } else if (option.equals("player")) {
            playerController.showPlayerNames();
            System.out.println("En cual quieres eliminar el registro: ");
            String reg = scanner.nextLine();
            Player player = gameController.findPlayerByName(reg);
            if (att.equals("player_name")) {
                player.setName("");
            } else if (att.equals("last_activity")) {
                player.setLastActivity("");
            } else if (att.equals("official_rating")) {
                player.setOfficialRating("");
            } else if (att.equals("wld")) {
                player.setWld("");
            } else if (att.equals("winrate")) {
                player.setWinrate(0);
            }
            update(player);
        } else if (option.equals("games")) {
            gameController.showGamesNames();
            System.out.println("En cual quieres eliminar el registro: ");
            String reg = scanner.nextLine();
            Games gamesExistente = gameController.findGameByName(reg);
            if (att.equals("game_name")) {
                gamesExistente.setName("");
            } else if (att.equals("map")) {
                gamesExistente.setMap(null);
            } else if (att.equals("players")) {
                // Para eliminar todos los jugadores de un juego, puedes iterar sobre el conjunto y eliminarlos
                // o simplemente establecer el conjunto a un nuevo HashSet vacío.
                gamesExistente.setPlayers(new HashSet<>());
            }
            update(gamesExistente);
        }
    }
    /**
     * Método para realizar una querry de toda la tabla que se le proporcione.
     * @param tableName Nombre de la tabla en la que realizar el select.
     */
    public void selectFromTable(String tableName) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Session session1 = em.unwrap(Session.class);
        try {
            session1.getTransaction().begin();
            String hql = "FROM " + tableName; // Aquí asumimos que los nombres de las clases de entidad son iguales a los nombres de las tablas en la base de datos
            Query query = session1.createQuery(hql);
            List<Object> result = query.getResultList();
            if (!result.isEmpty()) {
                System.out.println("Registros en la tabla " + tableName + ":");
                for (Object obj : result) {
                    System.out.println(obj.toString());
                }
            } else {
                System.out.println("No se encontraron registros en la tabla " + tableName);
            }
            session1.getTransaction().commit();
        } catch (Exception e) {
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
    /**
     * Método para realizar una querry en la que se tenga un texto concreto sobre una tabla.
     * @param tableName Nombre de la tabla en al que se realiza en select
     * @param columnName Nombre de la columna en la que se va a buscar
     * @param searchText Texto a buscar dentro de la columna
     */
    public void selectFromTable(String tableName, String columnName, String searchText) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Session session1 = em.unwrap(Session.class);
        try {
            session1.getTransaction().begin();
            String hql = "FROM " + tableName + " WHERE " + columnName + " LIKE :searchText";
            Query query = session1.createQuery(hql);
            query.setParameter("searchText", "%" + searchText + "%");
            List<Object> result = query.getResultList();
            if (!result.isEmpty()) {
                System.out.println("Registros en la tabla " + tableName + " que contienen '" + searchText + "':");
                for (Object obj : result) {
                    System.out.println(obj.toString());
                }
            } else {
                System.out.println("No se encontraron registros en la tabla " + tableName + " que contienen '" + searchText + "'");
            }
            session1.getTransaction().commit();
        } catch (Exception e) {
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
    /**
     * Método para realizar una querry basada en una condicion proporcionada por el usuario sobre una tabla.
     * @param tableName Nombre de la tabla en la que se realizara el select
     * @param columnName Nombre de la columna en la que se va a buscar
     * @param condition La condicion de busqueda
     * @param searchValue El valor que se está buscando
     */
    public void selectFromTable(String tableName, String columnName, String condition, Object searchValue) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Session session1 = em.unwrap(Session.class);
        try {
            session1.getTransaction().begin();
            String hql = "FROM " + tableName + " WHERE " + columnName + " " + condition + " :searchValue";
            Query query = session1.createQuery(hql);
            query.setParameter("searchValue", searchValue);
            List<Object> result = query.getResultList();
            if (!result.isEmpty()) {
                System.out.println("Registros en la tabla " + tableName + " donde " + columnName + " " + condition + " '" + searchValue + "':");
                for (Object obj : result) {
                    System.out.println(obj.toString());
                }
            } else {
                System.out.println("No se encontraron registros en la tabla " + tableName + " donde " + columnName + " " + condition + " '" + searchValue + "'");
            }
            session1.getTransaction().commit();
        } catch (Exception e) {
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
    /**
     * Método para realizar una querry basada en un ID sobre una tabla
     * @param tableName Nombre de la tabla en al que se realiza el select
     * @param id El ID que se está buscando dentro de la tabla
     */
    public void selectByIdFromTable(String tableName, int id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Session session1 = em.unwrap(Session.class);
        try {
            session1.getTransaction().begin();
            Object entity = session1.get(Class.forName("model." + tableName), id);
            if (entity != null) {
                System.out.println("Registro en la tabla " + tableName + " con id " + id + ":");
                System.out.println(entity.toString());
            } else {
                System.out.println("No se encontró el registro en la tabla " + tableName + " con id " + id);
            }
            session1.getTransaction().commit();
        } catch (Exception e) {
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

    /**
     * Método donde se len todos los ficheros CSV y se insertan dentro de la base de datos los datos que se encuentran en
     * estos ficheros
     * @throws SQLException
     * @throws FileNotFoundException
     */
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
            ArrayList<Games> games = gameController.readDataFromCSV();
            for (int i = 0; i < games.size(); i++) {
                insertar(games.get(i));
            }

        } catch (Exception e) {
            //session.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    /**
     * Método para borrar toda una tabla de la base de datos, ya tenga o no datos.
     * @param nombreTabla Nombre de la tabla a ser borrada
     */
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

    /**
     * Método para crear toda una entidad de cero
     * @param option Nombre de la tabla en al que crear la entidad
     */
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

            Games games = new Games(gameName, map, playerSet);
            insertar(games);
        }
    }
    /**
     * Método para realizar la lectura de un String
     * @return Devuelve la palabra que ha sido leida.
     */
    public String lecturaNombre(){
        Scanner scanner = new Scanner(System.in);
        String palabra = scanner.next();
        return palabra;
    }
}
