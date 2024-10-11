-- <.>
CREATE TABLE issue_status (
  id CHAR(1) PRIMARY KEY,
  name VARCHAR(128) NOT NULL,
  --${commonColumns}  -- <.>
);


INSERT INTO issue_status (id, name) -- <.>
VALUES('1', 'New'),
  ('2', 'In Progress'),
  ('3', 'Closed');


CREATE TABLE tracker (
  id CHAR(1) PRIMARY KEY,
  name VARCHAR(128) NOT NULL,
  --${commonColumns}  -- <.>
);


INSERT INTO tracker (id, name)
VALUES ('1', 'Feature'),
  ('2', 'Bug'),
  ('3', 'Support');


-- <.>
CREATE TABLE issue (
  id SERIAL PRIMARY KEY,
  subject VARCHAR(128) NOT NULL,
  due_date DATE,
  status_id CHAR(1) NOT NULL REFERENCES issue_status,
  tracker_id CHAR(1) NOT NULL REFERENCES tracker,
  description VARCHAR(8192),
  --${commonColumns}
)