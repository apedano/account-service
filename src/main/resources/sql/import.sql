create table accountar
(
    id             bigint,
    accountNumber  integer,
    accountStatus  varchar(255),
    balance        float,
    overdraftLimit float,
    customerName   varchar(255),
    customerNumber varchar(255),
    primary key (id)
);

/*Active Record rows */
INSERT INTO accountar(id, accountNumber, accountStatus, balance, customerName,
                      customerNumber)
VALUES (1, 123456789, 0, 550.78, -200.00, 'Debbie Hall', 12345);
INSERT INTO accountar(id, accountNumber, accountStatus, balance, customerName,
                      customerNumber)
VALUES (2, 111222333, 0, 2389.32, -200.00, 'David Tennant', 112211);
INSERT INTO accountar(id, accountNumber, accountStatus, balance, customerName,
                      customerNumber)
VALUES (3, 444666, 0, 3499.12, -200.00, 'Billie Piper', 332233);
INSERT INTO accountar(id, accountNumber, accountStatus, balance, customerName,
                      customerNumber)
VALUES (4, 87878787, 0, 890.54, -200.00, 'Matt Smith', 444434);
INSERT INTO accountar(id, accountNumber, accountStatus, balance, customerName,
                      customerNumber)
VALUES (5, 990880221, 0, 1298.34, -200.00, 'Alex Kingston', 778877);
INSERT INTO accountar(id, accountNumber, accountStatus, balance, customerName,
                      customerNumber)
VALUES (6, 987654321, 0, 781.82, -200.00, 'Tom Baker', 908990);
INSERT INTO accountar(id, accountNumber, accountStatus, balance, customerName,
                      customerNumber)
VALUES (7, 5465, 0, 239.33, -200.00, 'Alex Trebek', 776868);
INSERT INTO accountar(id, accountNumber, accountStatus, balance, customerName,
                      customerNumber)
VALUES (8, 78790, 0, 439.01, -200.00, 'Vanna White', 444222);
INSERT INTO accountar(id, accountNumber, accountStatus, balance, customerName, customerNumber)
VALUES (9, 999999999, 0, 999999999.01, -999999999.01, 'Readiness HealthCheck', 99999999999);


create table accountr
(
    id             bigint,
    accountNumber  integer,
    accountStatus  varchar(255),
    balance        float,
    customerName   varchar(255),
    customerNumber varchar(255),
    primary key (id)
);
/*Repository rows */
INSERT INTO accountr(id, accountNumber, accountStatus, balance, customerName,
                     customerNumber)
VALUES (1, 123456789, 0, 550.78, 'Debbie Hall', 12345);
INSERT INTO accountr(id, accountNumber, accountStatus, balance, customerName,
                     customerNumber)
VALUES (2, 111222333, 0, 2389.32, 'David Tennant', 112211);
INSERT INTO accountr(id, accountNumber, accountStatus, balance, customerName,
                     customerNumber)
VALUES (3, 444666, 0, 3499.12, 'Billie Piper', 332233);
INSERT INTO accountr(id, accountNumber, accountStatus, balance, customerName,
                     customerNumber)
VALUES (4, 87878787, 0, 890.54, 'Matt Smith', 444434);
INSERT INTO accountr(id, accountNumber, accountStatus, balance, customerName,
                     customerNumber)
VALUES (5, 990880221, 0, 1298.34, 'Alex Kingston', 778877);
INSERT INTO accountr(id, accountNumber, accountStatus, balance, customerName,
                     customerNumber)
VALUES (6, 987654321, 0, 781.82, 'Tom Baker', 908990);
INSERT INTO accountr(id, accountNumber, accountStatus, balance, customerName,
                     customerNumber)
VALUES (7, 5465, 0, 239.33, 'Alex Trebek', 776868);
INSERT INTO accountr(id, accountNumber, accountStatus, balance, customerName,
                     customerNumber)
VALUES (8, 78790, 0, 439.01, 'Vanna White', 444222);
-- Account for the readiness probe
INSERT INTO accountr(id, accountNumber, accountStatus, balance, customerName, customerNumber)
VALUES (9, 999999999, 0, 999999999.01, 'Readiness HealthCheck', 99999999999);
