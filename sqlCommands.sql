CREATE DATABASE PoisePMS;

USE PoisePMS;

CREATE TABLE project_info (
  proj_num int NOT NULL AUTO_INCREMENT,
  proj_name varchar(50) NOT NULL,
  building_type varchar(50) NOT NULL,
  total_fee DECIMAL(14,2),
  amount_paid DECIMAL (14,2) NOT NULL,
  proj_deadline DATE,
  proj_address_id int NOT NULL,
  architect_id int,
  contractor_id int,
  customer_id int NOT NULL, 
  PRIMARY KEY (proj_num),
  FOREIGN KEY (proj_address_id) REFERENCES project_addresses(proj_address_id),
  FOREIGN KEY (architect_id) REFERENCES architects(architect_id),
  FOREIGN KEY (contractor_id) REFERENCES contractors(contractor_id),
  FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
  finalised boolean,
  date_complete DATE
);

CREATE TABLE project_addresses (
 proj_address_id INT NOT NULL AUTO_INCREMENT, 
 proj_street_address varchar(50) NOT NULL,
 proj_postal_code varchar(50) NOT NULL,
 proj_erf_num varchar(20),
 PRIMARY KEY (proj_address_id)
);

CREATE TABLE customers (
  customer_id INT NOT NULL AUTO_INCREMENT,
  customer_first_name varchar(50),
  customer_last_name varchar(50),
  customer_phone_number varchar(50),
  customer_email_address varchar(50),
  customer_street_address varchar(50),
  customer_postal_code varchar(20),
  PRIMARY KEY (customer_id)
);

CREATE TABLE contractors (
  contractor_id INT NOT NULL AUTO_INCREMENT,
  contractor_first_name varchar(50),
  contractor_last_name varchar(50),
  contractor_phone_number varchar(50),
  contractor_email_address varchar(50),
  contractor_street_address varchar(50),
  contractor_postal_code varchar(20),
  PRIMARY KEY (contractor_id)
);

CREATE TABLE architects (
  architect_id INT NOT NULL AUTO_INCREMENT,
  architect_first_name varchar(50),
  architect_last_name varchar(50),
  architect_phone_number varchar(50),
  architect_email_address varchar(50),
  architect_street_address varchar(50),
  architect_postal_code varchar(20),
  PRIMARY KEY (architect_id)
);

INSERT INTO project_addresses (
  proj_street_address,
  proj_postal_code,
  proj_erf_num
)
VALUES ('22 Mandela Drive', 
'7711', 
'179');

INSERT INTO customers (
  customer_first_name,
  customer_last_name,
  customer_phone_number,
  customer_email_address,
  customer_street_address,
  customer_postal_code
)
VALUES (
  'Ricky',
  'Bobby',
  '2626626262',
  'rickyb@nascar.com',
  '1 Victory Lane',
  '2662'
);

INSERT INTO contractors (
  contractor_first_name,
  contractor_last_name,
  contractor_phone_number,
  contractor_email_address,
  contractor_street_address,
  contractor_postal_code
) 
VALUES (
  'Martin',
  'Martino',
  '1322311234',
  'martino@thebigcontractors.com',
  '2 Big Street',
  '1256'
);

INSERT INTO architects (
  architect_first_name,
  architect_last_name,
  architect_phone_number,
  architect_email_address,
  architect_street_address,
  architect_postal_code
)
VALUES (
  'Daniella',
  'Archibold',
  '0987098678',
  'dani@architecting.com',
  '21 Business lane',
  '6768'
);

INSERT INTO project_info (
  proj_name,
  building_type,
  total_fee,
  amount_paid,
  proj_deadline,
  proj_address_id,
  architect_id,
  contractor_id,
  customer_id,
  finalised,
  date_complete
) 
VALUES (
  'Freedom Raceway',
  'Race Track',
  622626.00,
  00.00,
  '2026-01-01',
  1,
  1,
  1,
  1,
  false,
  null
); 

INSERT INTO project_addresses (
  proj_street_address,
  proj_postal_code,
  proj_erf_num
)
VALUES ('47 Sisulu Street', 
'7998', 
'209');

INSERT INTO customers (
  customer_first_name,
  customer_last_name,
  customer_phone_number,
  customer_email_address,
  customer_street_address,
  customer_postal_code
)
VALUES (
  'James',
  'Baldwin',
  '1432435699',
  'jbald@gmail.com',
  '77 Newton Avenue',
  '8857'
);

INSERT INTO contractors (
  contractor_first_name,
  contractor_last_name,
  contractor_phone_number,
  contractor_email_address,
  contractor_street_address,
  contractor_postal_code
) 
VALUES (
  'Daniel',
  'De Marco',
  '1000154269',
  'demarco23@gmail.com',
  '42 Main Street',
  '7878'
);

INSERT INTO architects (
  architect_first_name,
  architect_last_name,
  architect_phone_number,
  architect_email_address,
  architect_street_address,
  architect_postal_code
)
VALUES (
  'Andy',
  'Coetzee ',
  '0988461523',
  'coetza@gmail.com',
  '21 Black Road',
  '7777'
);

INSERT INTO project_info (
  proj_name,
  building_type,
  total_fee,
  amount_paid,
  proj_deadline,
  proj_address_id,
  architect_id,
  contractor_id,
  customer_id,
  finalised,
  date_complete
) 
VALUES (
  'Apartment Baldwin',
  'Apartment',
  10000000.00,
  5000000.00,
  '2025-07-022',
  2,
  2,
  2,
  2,
  true,
  '2024-06-06'
); 