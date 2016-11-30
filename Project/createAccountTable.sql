DROP TABLE ACCOUNT;
CREATE TABLE ACCOUNT
(
firstName VARCHAR(255) NOT NULL,
lastName VARCHAR(255) NOT NULL,
email VARCHAR(255) NOT NULL,
password VARCHAR(255) NOT NULL,
admin INTEGER NOT NULL
);

ALTER TABLE ACCOUNT
    ADD CONSTRAINT account_email_pk PRIMARY KEY (email);

INSERT INTO ACCOUNT (firstname, lastname, email, password, admin)
    VALUES ('itkstu','student','itkstu@ilstu.edu','student',1);
