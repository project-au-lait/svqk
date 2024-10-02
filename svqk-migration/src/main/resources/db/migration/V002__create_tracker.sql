CREATE TABLE tracker (
  id CHAR(2) PRIMARY KEY,
  name VARCHAR(128) NOT NULL,
  --${commonColumns}
);

CREATE TABLE tracker_issue_status (
  id SERIAL PRIMARY KEY,
  tracker_id CHAR(2) NOT NULL REFERENCES tracker(id),
  issue_status_id CHAR(1) NOT NULL REFERENCES issue_status(id)
);

ALTER TABLE issue
  ADD COLUMN tracker_id CHAR(2) NOT NULL REFERENCES tracker(id);

-- 一時的にtracker_idをNULL許可に変更（削除予定）
ALTER TABLE issue ALTER COLUMN tracker_id DROP NOT NULL;

INSERT INTO tracker (id, name)
VALUES ('1', 'バグ'), ('2', '機能'), ('3', 'サポート');