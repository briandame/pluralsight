DROP TABLE question IF EXISTS;
CREATE TABLE question  (
  id SERIAL NOT NULL PRIMARY KEY,
  question TEXT NOT NULL,
  answer BIGINT NOT NULL,
  distractors TEXT,
  operator CHAR(1),
  status SMALLINT NOT NULL DEFAULT 1,
  created_at TIMESTAMP NOT NULL DEFAULT now(),
  updated_at TIMESTAMP  NOT NULL DEFAULT now()
);

DROP INDEX IF EXISTS operator_idx;
CREATE INDEX operator_idx ON question(operator);