# engineering-firm-database-app

## Description
A program which enters, edits, displays and deletes information from a database.

This was built for a course to display competency with relational databases, MySQL, Java and JDBC.

## Table of contents

1. [**Setting Up**](#setting-up)
   * [MySQL](#mysql)
   * [Java](#java)
2. [**Usage**](#usage)
   * [Enter project](#enter-a-project)
   * [Enter person](#enter-a-person)
   * [Update project](#update-project)
   * [Update person](#update-a-person)
   * [Update project address](#update-project-address)
   * [Search project](#search-project)
   * [Search person](#search-person)
   * [Show unfinished projects](#show-all-unfinished-project)
   * [Show overdue project](#show-all-overdue-projects)
   * [Delete project](#delete-project)
   * [Delete person](#delete-person)
   * [Delete project address](#delete-project-address)
   * [Close Program](#close-program)
 3. [**Credits**](#credits)

## Setting Up

### MySQL

You will need to have MySQL installed on your machine for this program to work. This is the link to get MySQL: https://dev.mysql.com/downloads/mysql/.

Once you have installed MySQL you need to create a database hosted locally and populate it with these commands in the order they are shown (at least the last one needs to be last as it references the other tables).

#### Customer Table

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

#### Contractor Table

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

#### Architect Table

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

#### Project Address Table

CREATE TABLE project_addresses (
 proj_address_id INT NOT NULL AUTO_INCREMENT, 
 proj_street_address varchar(50) NOT NULL,
 proj_postal_code varchar(50) NOT NULL,
 proj_erf_num varchar(20),
 PRIMARY KEY (proj_address_id)
);

#### Projects Table

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
  FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

After this (or before) you will need to create a user to connect to the database through the app and assign them a password. After creating this user, you can set up the Java.

### Java

At line 30 a connection object is created which needs to contain information regarding the database.

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/6a71014d-2f9b-480a-ac66-b79c20c1092d)

You need to write in the port for the localhost where your database is located.
For this example it is 3310.

You need to include the name of the database.
For this example it is PoisePMS.

You need to have the username of the user accessing the database.
For this example it is otheruser.

You need to include the password for that user.
For this example it is swordfish.

After all of this has been done, the program should run. Likely errors preventing the program from running are the database not existing, this user not existing or typos being made.

## Usage

When running the program you will be presented with these options:

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/1a94d272-0162-40df-9adc-5f63b216ff06)

You choose the option you want be entering the number next to the option.
We will discuss each of these options:

### Enter a project

Press one to enter a project

#### Project address

You will then be presented with this information:

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/667f45c2-ac30-4939-b018-ffcebcd558b2)

Unless you have already entered a project address through the console, you must enter a new project address. Otherwise you will get a message stating you need to enter a new one.

You will be prompted to enter the street address of the project (this is the street address of where the project will take place):

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/4e815a7c-7333-458c-8224-31470d5c7e84)

In this example I have entered 8 High street as the address. Note that it is a mandatory field, if it is left empty, you will be prompted again.

After this you will be prompted to enter a postal code. This is also mandatory as it is possible for two places to have the same street address but be in different cities. Therefore, a postal code is required to differenciate the two.

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/54de4f6b-48dc-4fbf-9e3a-32a5ed6cf5ba)

In this example I have entered 1234 as the postal code. Postal codes do not have to follow this format as the format varies from country to country, some use letters and the are different lengths.

Next you will be prompted to enter an ERF number:

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/5c140c67-7e1b-4b7e-9e4d-bc27a2f38aef)

In this example I have entered 4321 as the ERF number. ERF numbers are a South African system which tracks property ownership more distinctly than street addresses. Other countries use other systems to do so. This field can be left blank as it is not essential to determine where a project is and if the project is in a country other than South Africa.

#### Building Type

Next you will be prompted to enter a building type:

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/a715d641-8224-4336-99c7-de234c53419c)

In this example I have entered Parking Complex as the building type. This is a mandatory field as it is assumed knowing what is being built for the project is essential information.

#### Total fee

Next you will be prompted to enter the total fee for the project:

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/96cfd3e6-077e-488c-b5b3-e2687bc1a8f9)

In this example I entered 10 000 000.00 as the total fee for the project. This value can be left empty if a total fee has not been decided or calculated as of yet.

#### Amount paid

Next you will be prompted to enter the amount paid so far:

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/fcfd287b-c352-4042-a244-e5ef55ae6786)

In this example I entered 2 000 000.00 as the amount paid. If you do not enter anything, 0.00 will be recorded as the amount paid.

#### Project Deadline

Next you will be prompted to enter the deadline for the project:

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/e13d8ada-8b54-48ee-a547-33a2d6d213b7)

In this example I have entered the 1st of October 2026 as the date. The format is year-month-day. If you do not enter a date, a null value will be recorded. It is assumed that a deadline has not yet been decided upon.

#### Architect

Next you will be prompted to enter a new architect for the project, choose an architect from the database or leave the field empty: 

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/6b2e2b30-d04c-42e2-9671-8f259e544ae1)

In this example I first did not enter a number, then entered 3 to not enter an architect as this will be covered on entering a person. If you choose 2 and try get one from the database, you will either be presented with the architects in the database, unless it is empty, where you will instead be made to enter a new one, a roundabout way of selecting option 1.

#### Contractor

Next you will be prompted to enter a new contractor, choose a contractor from the database or leave the field empty:

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/c45af8a3-f57f-4b53-933e-bac949d98c2b)

In this example I have chosen to get one from the database and selected the ID of the contractor as ID 1. Martin Martino will be assigned to the project as the contractor. This section is functionally the same as the architect area above as you can leave the field NULL. It is assumed you may not have confirmed the contractor at the time of creating the project.

#### Customer

Next you will be prompted to enter a customer for the project:

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/c156498c-9e69-4b29-b1ce-743c086e4715)

In this example I have again chosen an ID and chose James Baldwin as the customer. This field cannot be left blank, unlike the last two, as a project would need to have a customer in order for it to exist. Entering a person will be covered in the next section.

#### Project name

Finally you will be prompted to enter a name for the project:

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/e5b27e14-ef08-49a1-af23-e843b20ead2f)

if you do not enter a name for the project, one will be generated using the last name of the customer and the type of building the project is for. The program will return to the initial prompt after this.

### Enter a person

#### Choose person type

You can enter a new person to the database by creating a new project and entering new people there or, you can enter a new person by entering 2 from the initial prompt (what will be demonstrated).

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/8f650470-2c0f-4ebf-94e1-9064855270b4)

In this example I have chosed to enter a new person from the initial prompt menu. I have then chosen to enter an architect.

#### Person name

Next you will be prompted to enter the person's first name and after entering this, their last name:

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/1c606710-e490-481c-b5cf-964f35c46034)

Both of these fields are mandatory.

#### Person phone number

Next you will be prompted to enter a phone number: 

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/8cdeef98-fb8d-4994-98a4-a404c4b191d0)

In this example I entered 123 1234 12333 as the phone number. Phone numbers need to be at least 7 characters long and consist of only digits and spcace (international numbers have different lengths). If the phone number field is left blank, you will be required to enter an email address as you should have at least one means of contacting the person.

#### Person email address

Next you will be prompted to enter an email address for the person:

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/d1fa317d-783c-46ed-a766-f0c9402ee85f)

In this example I entered davinkii@gmail.com as the email address. This will be a mandatory field if the phone number was left blank.

#### Person street address

Next you will be prompted to enter the street address of the person: 

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/569ece09-8476-4d58-a250-7faaa5286529)

In this example I entered 21 Pisa road as the street address, this is not a mandatory field as it is not essential that you have the street address of the person.

#### Person postal code

Next you will be prompted to enter the postal code of the person:

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/68008c18-0191-4907-ab04-6f632845efef)

In this example I entered 12333 as the postal code. Postal code formats vary from country to country. It is not an essential field as you do not need the address of a person unlinke a project.

The person will then be entered into the database.

## Update project

To update a project, select 3 on the initial menu. You will then be shown the projects in the database with their project number, name and finalisation status (whether they have been completed or not).

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/2853a156-e750-4220-8927-3af6637b758b)

In this example I chose project 9 (the one entered earlier in this document). 

You will then be shown all of the project information and be prompted to edit one of the fields:

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/e0c4cd6b-556a-4aee-a019-cc9988c73300)

In this example I chose to edit the architect as it is empty.

You are then prompted to edit the existing person, choose another from the database or replace the person with a NULL value:

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/0c03f2a5-ccca-4559-82f7-7e0d1fbb8ed5)

In this example I chose to choose an architect from the database to replace this one.

You will then be prompted with the architects in the database and be asked to choose one:

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/9bcd5806-02f3-4c18-9530-1dbe6a593268)

In this case, there is only Da Vinci from earlier in the document. You enter the ID (8 for Da Vinci) of the person and they will be added to the project.

#### Finalising projects

When choosing a field, if you choose the finalisation status field (option 6). You will be shown this:

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/78998d32-4556-4fc2-8920-7b64ec9e79ab)

If you choose 1, you will be prompted to enter a finalisation date:

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/41c0a958-18bf-44ca-a7c6-3523b4129016)

After entering the date, the finalisation status and date will be updated for the project.

If you choose 2 here:

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/78998d32-4556-4fc2-8920-7b64ec9e79ab)

The finalisation status will be set to not finalised for the project and the finalisation date will be removed as there can be no date of finalisation if the project is not finalised.

### Update a person

Select 4 on the initial prompt screen or choose to edit a person while updating a project. If you choose to update a person directly you will be prompted:

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/362e871e-6295-4441-af31-fe7adabf7d1d)

First choose the type of person, (architect in this case), then the ID of the person (8) and you will be shown their information.

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/9f957d63-b51d-4f3f-8fee-359d31618c97)

For this example we will edit the postal code:

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/e1c6c909-a18a-489d-8229-9d20bfdc2e53)

After entering this the person will be updated. Note, if you update the email address or phone number to be null, the other will become a mandatory field. If there is no phone number, you will not be able to remove the email address, only update it and vice versa.

### Update project address

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/aa3eb2ed-47bd-406a-9641-d066bfdce538)

In this example an ERF number is updated (erf numbers cannot be 1 character) as shown be error handling message. First you choose the project address based on ID, then choose the field you want to update. Street address and postal code are still mandatory.

## Search project

If you choose to search for a project you will be shown this prompt:

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/2f17690c-db0a-4905-8a6f-97347a0bcaeb)

If you choose to search by name, the project will be shown and you will have to type in the name of one to dispplay its information. 

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/323844f8-1dd3-4ef7-bfc1-0255ee3cc5e4)

Otherwise, if you choose to search by project number, you will have to type in the project number of a project to display its information.

## Search Person

After choosing to search for a person you will be prompted to choose a type of person to search:

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/0f322da5-0653-4fd2-8411-d1780ba5676f)

After this the details of the people in the database will be shown. You can enter the ID of one to show their information:

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/6a8cd103-02bc-430b-94c6-dd224e872ed1)

## Show all unfinished project

If you enter 8 on the initial prompt, a list of all the unfinished projects will be displayed with their project number, name and deadline on display:

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/974e7f9f-8809-484a-a988-6026193ff28e)

## Show all overdue projects

If you enter 9 on the initial prompt, a list of projects where the deadline is in the past will be shown: 

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/7fd55297-575f-447e-8160-fc87f4a8e99a)

The project number, name and deadline will all be shown of all projects that are not finalised and are past the deadline.

## Delete project

If you choose to delete a project, you will be presented with all of the projects in the database: 

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/e1be404d-15aa-4ba1-89f1-95e938f5064f)

After which you must enter the number of the project you want to delete, the project information will be shown and you will be asked again whether you are sure you want to delete the project or not. 

If you say yes, the project will be deleted, if you say no, it will not.

## Delete person

The only difference between delete person and delete project is that you cannot delete a person if they are associated with a project. If you try to delete someone who is associated with one or more projects, a list of the projects that person is associated with will be shown:

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/60831570-8892-47a9-86a5-2778f714e613)

Otherwise you will get a confirmation message and enter yes or no to delete a person from the database.

## Delete project address

The only difference between delete project address and delete project is that you cannot delete a project address if they are associated with a project. If you try to delete a project address that is associated with one or more projects, a list of the projects that project address is associated with will be shown:

![image](https://github.com/Nerrotok/engineering-firm-database-app/assets/140401659/ec877299-788b-485b-b38b-0c2c5616add3)


Otherwise you will get a confirmation message and enter yes or no to delete a project address from the database.

## Close program

This option closes the program.

# Credits

Currently, only I am working on this project.
