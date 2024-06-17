create table if not exists flights
(
    id serial primary key not null,
    airline varchar(255) not null,
    cabin_class char(1) not null,
    departure_airport_code char(3) not null,
    destination_airport_code char(3) not null,
    departure_date timestamp not null,
    arrival_date timestamp not null,
    price_in_pence integer not null,
    seats_available integer not null
);

INSERT INTO flights (airline, cabin_class, departure_airport_code, destination_airport_code, departure_date, arrival_date, price_in_pence, seats_available)
VALUES ('Emirates', 'E', 'LHR', 'DXB', '2024-07-01 08:00:00', '2024-07-10 08:00:00', 55000, 51);
INSERT INTO flights (airline, cabin_class, departure_airport_code, destination_airport_code, departure_date, arrival_date, price_in_pence, seats_available)
VALUES ('British Airways', 'B', 'LHR', 'DXB', '2024-07-01 08:00:00', '2024-07-10 08:00:00', 65000, 41);
INSERT INTO flights (airline, cabin_class, departure_airport_code, destination_airport_code, departure_date, arrival_date, price_in_pence, seats_available)
VALUES ('Turkish Airlines', 'E', 'LHR', 'DXB', '2024-07-01 08:00:00', '2024-07-10 08:00:00', 45000, 11);
INSERT INTO flights (airline, cabin_class, departure_airport_code, destination_airport_code, departure_date, arrival_date, price_in_pence, seats_available)
VALUES ('Lufthansa', 'B', 'LHR', 'DXB', '2024-07-01 08:00:00', '2024-07-10 08:00:00', 75000, 3);
INSERT INTO flights (airline, cabin_class, departure_airport_code, destination_airport_code, departure_date, arrival_date, price_in_pence, seats_available)
VALUES ('KLM', 'E', 'LHR', 'DXB', '2024-07-01 08:00:00', '2024-07-10 08:00:00', 55000, 0);
INSERT INTO flights (airline, cabin_class, departure_airport_code, destination_airport_code, departure_date, arrival_date, price_in_pence, seats_available)
VALUES ('Emirates', 'E', 'LHR', 'DXB', '2024-07-11 08:00:00', '2024-07-20 08:00:00', 55000, 51);
INSERT INTO flights (airline, cabin_class, departure_airport_code, destination_airport_code, departure_date, arrival_date, price_in_pence, seats_available)
VALUES ('British Airways', 'B', 'LHR', 'DXB', '2024-07-11 08:00:00', '2024-07-20 08:00:00', 65000, 41);
INSERT INTO flights (airline, cabin_class, departure_airport_code, destination_airport_code, departure_date, arrival_date, price_in_pence, seats_available)
VALUES ('Turkish Airlines', 'E', 'LHR', 'DXB', '2024-07-11 08:00:00', '2024-07-20 08:00:00', 45000, 11);
