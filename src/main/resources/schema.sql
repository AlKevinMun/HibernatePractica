BEGIN WORK;
SET TRANSACTION READ WRITE;

SET datestyle = YMD;

-- Esborra taules si existien
DROP TABLE map;
DROP TABLE player;
DROP TABLE games;
DROP TABLE commander;

-- Creació de taules
 CREATE TABLE commander
   (
    commander_name VARCHAR(20) PRIMARY KEY not null
   );

CREATE TABLE map
  (
    map_name VARCHAR(50) not null,
    id_map INTEGER PRIMARY KEY not null,
    creator VARCHAR(50) not null,
    max_players   INTEGER not null,
    size   VARCHAR(50) not null

  );

  CREATE TABLE player
    (
      player_name   VARCHAR(50) not null,
      id_player INTEGER PRIMARY KEY not null,
      last_activity VARCHAR(50) not null,
      official_ratting  VARCHAR(20) not null,
      wld   VARCHAR(50) not null,
      commander VARCHAR(20),
      winrate INTEGER not null,
      FOREIGN KEY(commander) REFERENCES commander(commander_name) ON DELETE CASCADE

    );

  CREATE TABLE games
    (
      game_name   VARCHAR(50) not null,
      id_game INTEGER PRIMARY KEY not null,
      id_map INTEGER,
      player1 INTEGER,
      player2 INTEGER,
      FOREIGN KEY(id_map) REFERENCES map(id_map) ON DELETE CASCADE ,
      FOREIGN KEY (player1) REFERENCES player(id_player) ON DELETE CASCADE ,
      FOREIGN KEY (player1) REFERENCES player(id_player) ON DELETE CASCADE
    );

    commit;