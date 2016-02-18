CREATE TABLE IF NOT EXISTS users (
  id         BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name       VARCHAR(50),
  email      VARCHAR(50),
  birth_date DATE
);

CREATE TABLE IF NOT EXISTS events (
  id     BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name   VARCHAR(50),
  price  DOUBLE,
  rating VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS assigned_events (
  id         BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  event_id   BIGINT NOT NULL,
  auditorium VARCHAR(50),
  date_time  TIMESTAMP
);
ALTER TABLE assigned_events ADD FOREIGN KEY (event_id) REFERENCES events (id);

CREATE TABLE IF NOT EXISTS tickets (
  id                BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  assigned_event_id BIGINT NOT NULL,
  user_id           BIGINT NULL,
  final_price       DOUBLE
);
ALTER TABLE tickets ADD FOREIGN KEY (user_id) REFERENCES users (id);

CREATE TABLE IF NOT EXISTS seats (
  id        BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  ticket_id BIGINT NOT NULL,
  seat      INT    NOT NULL
);
ALTER TABLE seats ADD FOREIGN KEY (ticket_id) REFERENCES tickets (id);

CREATE TABLE IF NOT EXISTS lucky_info (
  id        BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  user_id   BIGINT NOT NULL,
  ticket_id BIGINT NOT NULL
);
ALTER TABLE lucky_info ADD FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE lucky_info ADD FOREIGN KEY (ticket_id) REFERENCES tickets (id);

CREATE TABLE IF NOT EXISTS discounts (
  id      BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name    VARCHAR(50),
  user_id BIGINT NOT NULL,
  times   INT
);
ALTER TABLE discounts ADD FOREIGN KEY (user_id) REFERENCES users (id);

CREATE TABLE IF NOT EXISTS events_by_name (
  id       BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  event_id BIGINT NOT NULL,
  times    INT
);
ALTER TABLE events_by_name ADD FOREIGN KEY (event_id) REFERENCES events (id);

CREATE TABLE IF NOT EXISTS events_price (
  id       BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  event_id BIGINT NOT NULL,
  times    INT
);
ALTER TABLE events_price ADD FOREIGN KEY (event_id) REFERENCES events (id);

CREATE TABLE IF NOT EXISTS events_tickets (
  id       BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  event_id BIGINT NOT NULL,
  times    INT
);
ALTER TABLE events_tickets ADD FOREIGN KEY (event_id) REFERENCES events (id);