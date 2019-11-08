CREATE TABLE `client` (
  id bigint,
  first_name varchar(20) NOT NULL,
  second_name varchar(20),
  active tinyint(1) NOT NULL,
  birth_date datetime,
  PRIMARY KEY (id)
);

CREATE TABLE `city` ( 
  id bigint,
  name varchar(50) NOT NULL,
  zip_code varchar(5) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE `client_city` (
 client_id bigint,
 city_id bigint,
 PRIMARY key(client_id,city_id),
 FOREIGN KEY (client_id) REFERENCES `client`(id),
 FOREIGN KEY (city_id) REFERENCES `city`(id)
);

 