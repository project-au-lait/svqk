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
    int_id INT,
    char_id VARCHAR(128),
    bool_id BOOLEAN,
    date_id DATE,
    timestamp_id TIMESTAMP,
    text_id VARCHAR(129),
    int_col INT,
    char_col VARCHAR(128),
    bool_col BOOLEAN,
    date_col DATE,
    timestamp_col TIMESTAMP,
    text_col VARCHAR(129),
    --${commonColumns},
    PRIMARY KEY (
        int_id,
        char_id,
        bool_id,
        date_id,
        timestamp_id,
        text_id
    )
);


INSERT INTO hello_world (
        int_id,
        char_id,
        bool_id,
        date_id,
        timestamp_id,
        text_id,
        int_col,
        char_col,
        bool_col,
        date_col,
        timestamp_col,
        text_col
    )
VALUES (
        1,
        '1',
        TRUE,
        '2025-03-31',
        '2025-03-31 00:00:00',
        'Hello World',
        1,
        '1',
        TRUE,
        '2025-03-31',
        '2025-03-31 00:00:00',
        'Hello World'
    );