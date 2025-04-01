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


CREATE table hello_world (
    id INT,
    seq_no INT,
    message VARCHAR(255) NOT NULL,
    --${commonColumns},
    PRIMARY KEY (id, seq_no)
);


INSERT INTO hello_world (id, seq_no, message)
VALUES (1, 1, 'Hello World');