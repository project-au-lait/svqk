CREATE TABLE hello (
    id INT PRIMARY KEY,
    message VARCHAR(255) NOT NULL
);


INSERT INTO hello (id, message)
VALUES (1, 'World');


CREATE TABLE world (
    id INT PRIMARY KEY,
    message VARCHAR(255) NOT NULL
);


INSERT INTO world (id, message)
VALUES (1, 'Hello');