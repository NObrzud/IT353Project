DROP TABLE ACCOUNT;
CREATE TABLE ACCOUNT
(
firstName VARCHAR(255) NOT NULL,
lastName VARCHAR(255) NOT NULL,
email VARCHAR(255) NOT NULL PRIMARY KEY,
password VARCHAR(255) NOT NULL,
admin INTEGER NOT NULL
);

INSERT INTO ACCOUNT (firstname, lastname, email, password, admin)
    VALUES ('itkstu','student','itkstu@ilstu.edu','student',1);

INSERT INTO ACCOUNT (firstname, lastname, email, password, admin)
    VALUES ('Nick','Obrzud','njobrzu@ilstu.edu','abc',0);

INSERT INTO ACCOUNT (firstname, lastname, email, password, admin)
    VALUES ('Charlie','Spalevic','ccspale@ilstu.edu','abc',0);