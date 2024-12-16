-- <.>
CREATE TABLE issue_status (
  id CHAR(1) PRIMARY KEY,
  name VARCHAR(128) NOT NULL,
  --${commonColumns}  -- <.>
);


CREATE TABLE tracker (
  id CHAR(1) PRIMARY KEY,
  name VARCHAR(128) NOT NULL,
  --${commonColumns}  -- <.>
);


-- <.>
CREATE TABLE issue (
  id SERIAL PRIMARY KEY,
  subject VARCHAR(128) NOT NULL,
  due_date DATE,
  status_id CHAR(1) NOT NULL REFERENCES issue_status,
  tracker_id CHAR(1) NOT NULL REFERENCES tracker,
  description VARCHAR(8192),
  --${commonColumns}
);


CREATE TABLE journal (
  issue_id INT NOT NULL REFERENCES issue,
  seq_no INT NOT NULL,
  notes TEXT,
  --${commonColumns},
  CONSTRAINT journal_pk PRIMARY KEY (issue_id, seq_no)
);