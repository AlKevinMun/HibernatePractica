CREATE TABLE autors
(
  id_autor integer NOT NULL,
  nom character varying(30) NOT NULL,
  any_naixement character(4),
  nacionalitat character varying(12),
  actiu boolean NOT NULL,
  CONSTRAINT pk_autors PRIMARY KEY (id_autor)
);



CREATE TABLE revistes
(
  id_revista serial NOT NULL,
  titol character varying(30) NOT NULL,
  data_publicacio date NOT NULL ,
  CONSTRAINT pk_revistes PRIMARY KEY (id_revista),
  CONSTRAINT uk_titol UNIQUE (titol)
);


CREATE TABLE articles
(
  id_article serial NOT NULL,
  id_revista integer,
  id_autor integer NOT NULL,
  titol character varying(30) NOT NULL,
  data_creacio date ,
  publicable boolean NOT NULL,
  CONSTRAINT pk_articles PRIMARY KEY (id_article),
  CONSTRAINT fk_art_revistes FOREIGN KEY (id_revista)
      REFERENCES revistes (id_revista) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_articles_autors FOREIGN KEY (id_autor)
      REFERENCES autors (id_autor) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_articles UNIQUE (titol)  
);

