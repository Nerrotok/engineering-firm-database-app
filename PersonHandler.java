import java.sql.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class performs methods which handle person information in the database.
 */
public class PersonHandler {

	/**
	 * Method which deletes a person object from the database. It also check if the
	 * person is associated with any projects, and display them, if so, the person
	 * cannot be deleted unless the projects they are associated with are deleted.
	 * 
	 * @param results       object to store query results
	 * @param statement     object to query and update the database
	 * @param userInput     user's input
	 * @param person        person object
	 * @param personHandler person handling object
	 * @param sc            object to get user inputs
	 * @param personType    the type of person being deleted
	 * @param project       project object
	 * @param prompt        object containing prompt methods
	 * @throws SQLException message for if an SQL error occurs
	 */
	public void deletePerson(ResultSet results, Statement statement, String userInput, Person person,
			PersonHandler personHandler, Scanner sc, String personType, Project project, Prompt prompt)
			throws SQLException {

		person.iD = personHandler.getPersonId(sc, results, statement, "customer", userInput);

		// Condition for no persons in database
		if (person.iD.equals("0")) {
			System.out.println("");
		} else {
			if (personHandler.personOnProject(results, statement, person.iD, "customer")) {
				System.out.println("You may not delete this customer as they are associated with a project/s: ");
				project.displayProjectsPersonAssociatedWith(results, statement, "customer", person.iD);
			} else {
				prompt.areYouSureDeletePrompt("customer");
				userInput = prompt.getUserChoiceTwoOptions(userInput, sc);

				if (userInput.equals("1")) {
					personHandler.deletePersonFromDB(statement, person.iD, "customer");
				} else if (userInput.equals("2")) {
					System.out.println("The customer has not been deleted");
					System.out.println("");
				}
			}
		}
	}

	/**
	 * Method to update a person's fields in the database.
	 * 
	 * @param userInput     user's input
	 * @param personType    the type of person being updated
	 * @param personHandler object to handle person objects
	 * @param person        person object
	 * @param sc            object to get user inputs
	 * @param results       object to store queried information
	 * @param statement     object to query and update the database
	 * @throws SQLException message for if an SQL error occurs
	 */
	public void updatePersonField(String userInput, String personType, PersonHandler personHandler, Person person,
			Scanner sc, ResultSet results, Statement statement) throws SQLException {
		// first name
		if (userInput.equals("1")) {
			person.firstName = personHandler.getPersonName(personType, userInput, "first", sc);
			personHandler.updatePersonFieldInfo(statement, true, personType, person.firstName, "first_name", person.iD);
			// Last name
		} else if (userInput.equals("2")) {
			person.lastName = personHandler.getPersonName(personType, userInput, "last", sc);
			personHandler.updatePersonFieldInfo(statement, true, personType, person.lastName, "last_name", person.iD);
			// If email or phone, check the other is not null
			// Phone number
		} else if (userInput.equals("3")) {
			// Check if the email address is present and therefore if the phone number
			// should be mandatory
			if (personHandler.isPhoneOrEmailNull(results, statement, "email_address", personType, person.iD)) {
				person.phoneNumber = personHandler.getPersonPhoneNumberMandatory(personType, userInput, sc);
				personHandler.updatePersonFieldInfo(statement, true, personType, person.phoneNumber, "phone_number",
						person.iD);
			} else {
				person.phoneNumber = personHandler.getPersonPhoneNumber(userInput, sc);
				personHandler.updatePersonFieldInfo(statement, true, personType, person.phoneNumber, "phone_number",
						person.iD);
			}

			// email address
		} else if (userInput.equals("4")) {
			// Check if the phone number is present and therefore if the email address
			// should be mandatory
			if (personHandler.isPhoneOrEmailNull(results, statement, "phone_number", personType, person.iD)) {
				person.emailAddress = personHandler.getPersonEmailAddressMandatory(personType, userInput, sc);
				personHandler.updatePersonFieldInfo(statement, true, "customer", person.emailAddress, "email_address",
						person.iD);
			} else {
				person.emailAddress = personHandler.getPersonEmailAddress("customer", userInput, sc);
				personHandler.updatePersonFieldInfo(statement, true, "customer", person.emailAddress, "email_address",
						person.iD);
			}

			// Street address
		} else if (userInput.equals("5")) {
			person.streetAddress = personHandler.getPersonStreetAddress("customer", userInput, sc);
			personHandler.updatePersonFieldInfo(statement, true, "customer", person.streetAddress, "street_address",
					person.iD);
			// postal code
		} else if (userInput.equals("6")) {
			person.postalCode = personHandler.getPersonPostalCode("customer", userInput, sc);
			personHandler.updatePersonFieldInfo(statement, true, "customer", person.postalCode, "postal_code",
					person.iD);
		}
	}

	/**
	 * Method which gets a person's information, adds them to the database and
	 * returns a person object with the same information.
	 * 
	 * @param person        person object
	 * @param personType    type of person being created
	 * @param userInput     user's input
	 * @param sc            object to get user inputs
	 * @param results       object to store queried information
	 * @param statement     object to query and update the database
	 * @param personHandler object to handle person objects
	 * @return person object with the information added
	 * @throws SQLException message for if an SQL error occurs
	 */
	public Person assignNewPersonValuesAndEnterToDatabase(Person person, String personType, String userInput,
			Scanner sc, ResultSet results, Statement statement, PersonHandler personHandler) throws SQLException {
		person.firstName = personHandler.getPersonName(personType, userInput, "first", sc);
		person.lastName = personHandler.getPersonName(personType, userInput, "last", sc);
		person.phoneNumber = personHandler.getPersonPhoneNumber(userInput, sc);
		if (person.phoneNumber != null) {
			person.emailAddress = personHandler.getPersonEmailAddress(personType, userInput, sc);
		} else {
			person.emailAddress = personHandler.getPersonEmailAddressMandatory(personType, userInput, sc);
		}
		person.streetAddress = personHandler.getPersonStreetAddress(personType, userInput, sc);
		person.postalCode = personHandler.getPersonPostalCode(personType, userInput, sc);
		personHandler.enterNewPerson(statement, personType, person.firstName, person.lastName, person.phoneNumber,
				person.emailAddress, person.streetAddress, person.postalCode);
		person.iD = personHandler.getIdLatestPersonEntry(statement, results, personType);

		return person;
	}

	/**
	 * Method which gets a customer's last name based on their ID
	 * 
	 * @param results    object to store information queried
	 * @param statement  object to query the database
	 * @param customerId customer's id
	 * @return customer's surname
	 * @throws SQLException message for if an SQL error occurs
	 */
	public String getCustomerLastNameFromId(ResultSet results, Statement statement, String customerId)
			throws SQLException {

		results = statement.executeQuery("SELECT * FROM customers WHERE customer_id = " + customerId + ";");
		results.next();
		String customerLastName = results.getString("customer_last_name");

		return customerLastName;
	}

	/**
	 * Method which deletes a person from a database based on their id.
	 *
	 * @param statement  the object used to delete the person
	 * @param personId   the person's id
	 * @param personType the type of person being deleted
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method to delete person from database
	public void deletePersonFromDB(Statement statement, String personId, String personType) throws SQLException {

		statement.executeUpdate("DELETE FROM " + personType + "s WHERE " + personType + "_id = " + personId + ";");

		System.out.println("The " + personType + " has been deleted");
	}

	/**
	 * Method which returns a boolean value showing whether a person is associated
	 * with a project or not. Returns true if they are and false if they are not.
	 * 
	 * @param results    object that stores query information from the database
	 * @param statement  object used to query the database
	 * @param personId   the id of the person
	 * @param personType the type of the person
	 * @return boolean value
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method to check if person is on a project
	public boolean personOnProject(ResultSet results, Statement statement, String personId, String personType)
			throws SQLException {

		boolean personOnProject;

		results = statement
				.executeQuery("SELECT * FROM project_info " + "WHERE " + personType + "_id = " + personId + ";");

		if (results.isBeforeFirst()) {
			personOnProject = true;
		} else {
			personOnProject = false;
		}

		return personOnProject;
	}

	/**
	 * Method which displays all the persons of a type and prompts the user to
	 * choose one by entering the id of one. If there are no IDs available, an
	 * impossible one will be returned instead.
	 * 
	 * @param scObj      object to get user inputs
	 * @param results    object which stores information from the database
	 * @param statement  object used to query information from the database
	 * @param personType the type of person
	 * @param userInput  the user's input
	 * @return person's id
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method to get an Person ID from the user
	public String getPersonId(Scanner scObj, ResultSet results, Statement statement, String personType,
			String userInput) throws SQLException {

		// Create variables
		boolean gettingId = true;
		boolean isInt = true;
		String id = null;

		// Get persons from the database
		results = statement.executeQuery("SELECT * FROM " + personType + "s;");

		if (!results.isBeforeFirst()) {
			System.out.println("There are no " + personType + "s in the database.");
			id = "0";
		} else {
			// Display the persons
			System.out.println(personType + "s in database: ");

			while (results.next()) {
				System.out.println("ID: " + results.getString(personType + "_id") + ", Name: "
						+ results.getString(personType + "_first_name") + " "
						+ results.getString(personType + "_last_name") + ", Phone Number: "
						+ results.getString(personType + "_phone_number") + ", Email: "
						+ results.getString(personType + "_email_address"));
			}
			System.out.println("");

			// while loop for bad inputs
			while (gettingId) {

				// Prompt the user to select an ID
				System.out.println("Please select an ID for the " + personType);
				userInput = scObj.nextLine().strip();
				System.out.println("");

				// Check if the userInput is an integer
				isInt = userInput.chars().allMatch(Character::isDigit);

				if (isInt && !userInput.equals("")) {

					// Check if the id exists
					results = statement.executeQuery("SELECT " + personType + "_id " + "FROM " + personType + "s WHERE "
							+ personType + "_id = " + userInput + ";");

					// Condition for if the id is in the database
					if (results.next()) {
						System.out.println("You have chosen ID: " + userInput);
						id = userInput;
						gettingId = false;

						// Condition for if the id is not in the database
					} else {
						System.out.println("Please enter a valid ID");
					}

				} else {
					System.out.println("Please enter an integer.");
				}
			}
		}
		System.out.println("");
		return id;
	}

	/**
	 * Method which gets and returns a person's name from the user. It gets a first
	 * name or last name in this program.
	 * 
	 * @param personType    the type of person
	 * @param userInput     the user's input
	 * @param nameSpecifier the kind of name of the person
	 * @param scObj         the object to get user inputs
	 * @return person's name
	 */
	// Method to get person first name
	public String getPersonName(String personType, String userInput, String nameSpecifier, Scanner scObj) {

		// Create variables
		boolean gettingName = true;
		String name = null;

		// While loop for bad inputs
		while (gettingName) {

			// Prompt the user for the persons
			System.out.println("Please enter the " + nameSpecifier + " name of the " + personType + ".");
			userInput = scObj.nextLine().strip();
			System.out.println("");

			if (userInput.equals("")) {
				System.out.println("Please enter something, this field cannot be blank.");
			} else {
				System.out.println("Recording " + userInput + " as the " + nameSpecifier + " name.");
				name = userInput;
				gettingName = false;
			}
		}
		System.out.println("");
		return name;
	}

	/**
	 * Method which gets and returns a phone number for a person from the user.
	 * 
	 * @param userInput the user's input
	 * @param scObj     object used to get the user's input
	 * @return person's phone number
	 */
	// Method to get phone number
	public String getPersonPhoneNumber(String userInput, Scanner scObj) {

		// set-up regex for phone number
		String regexNumbersAndSpaces = "^[0-9 ]+$";
		Pattern numbersAndSpacesPattern = Pattern.compile(regexNumbersAndSpaces);
		Matcher matcher = numbersAndSpacesPattern.matcher(userInput);

		// Create variables
		boolean enteringPhoneNumber = true;
		String phoneNumber = null;

		// While loop to control for bad inputs
		while (enteringPhoneNumber) {

			// Prompt the user to enter a phone
			System.out.println("Please enter the phone number," + " blank inputs will record a null value");
			System.out.println("If you do not enter a phone" + " number, you will have to enter an email");
			userInput = scObj.nextLine().strip();
			System.out.println("");
			matcher = numbersAndSpacesPattern.matcher(userInput);

			// Check that the input is valid if entered
			if (userInput.equals("")) {
				System.out.println("Recording a NULL value" + " for the phone number.");
				System.out.println("The email address is" + " now a mandatory field.");
				enteringPhoneNumber = false;
			} else if (matcher.matches() && userInput.length() < 19 && userInput.length() > 6) {
				System.out.println("Recording " + userInput + " as the phone number");
				phoneNumber = userInput;
				enteringPhoneNumber = false;
			} else {
				System.out.println(
						"Please enter a phone number with only numeric digits and being at least 7 characters.");
			}
		}
		return phoneNumber;
	}

	/**
	 * Method which gets and returns a phone number for a person from the user. It
	 * is a mandatory field in this method.
	 * 
	 * @param personType the type of person
	 * @param userInput  the user's input
	 * @param scObj      object used to get the user's input
	 * @return person's phone number
	 */
	public String getPersonPhoneNumberMandatory(String personType, String userInput, Scanner scObj) {

		// set-up regex for phone number
		String regexNumbersAndSpaces = "^[0-9 ]+$";
		Pattern numbersAndSpacesPattern = Pattern.compile(regexNumbersAndSpaces);
		Matcher matcher = numbersAndSpacesPattern.matcher(userInput);

		// Create variables
		boolean enteringPhoneNumber = true;
		String phoneNumber = null;

		// While loop to control for bad inputs
		while (enteringPhoneNumber) {

			// Prompt the user to enter a phone
			System.out.println("Please enter the phone number," + " this is a mandatory field as there is"
					+ " no email address for this " + personType + ".");
			userInput = scObj.nextLine().strip();
			System.out.println("");
			matcher = numbersAndSpacesPattern.matcher(userInput);

			// Check that the input is valid if entered
			if (userInput.equals("")) {
				System.out.println("Please enter a phone number");
			} else if (matcher.matches() && userInput.length() < 19 && userInput.length() > 6) {
				System.out.println("Recording " + userInput + " as the phone number");
				phoneNumber = userInput;
				enteringPhoneNumber = false;
			} else {
				System.out.println("Please enter a phone number with only numeric digits");
			}
		}
		return phoneNumber;
	}

	/**
	 * Method which gets a person's email address from the user and returns it. It
	 * is a mandatory field in this instance.
	 * 
	 * @param personType type of person
	 * @param userInput  the user's input
	 * @param scObj      object to get the user's input
	 * @return person's email address
	 */
	// Method to enter email address (mandatory)
	public String getPersonEmailAddressMandatory(String personType, String userInput, Scanner scObj) {

		// Set up regex for email
		String regexEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
		Pattern emailPattern = Pattern.compile(regexEmail);
		Matcher matcher = emailPattern.matcher(userInput);

		// Create variables
		boolean enteringEmailAddress = true;
		String emailAddress = null;

		// While loop to control for bad inputs
		while (enteringEmailAddress) {

			// Prompt to enter an email address
			System.out.println("Please enter an email, this is a mandatory field");
			;
			userInput = scObj.nextLine().strip();
			System.out.println("");
			matcher = emailPattern.matcher(userInput);

			// Conditions to check that the email is valid
			if (matcher.matches() && userInput.length() < 19 && userInput.length() > 5) {
				System.out.println("Recording " + userInput + " as the " + personType + " email.");
				emailAddress = userInput;
				enteringEmailAddress = false;
			} else {
				System.out.println("That is not a valid email address");
			}
		}
		System.out.println("");
		return emailAddress;
	}

	/**
	 * Method which gets and returns the email address of a person from the user. It
	 * is not a mandatory field in this instance.
	 * 
	 * @param personType type of person
	 * @param userInput  the user's input
	 * @param scObj      object used to get the user's input
	 * @return person's email address
	 */
	// Method to enter email address (not mandatory)
	public String getPersonEmailAddress(String personType, String userInput, Scanner scObj) {

		// Set up regex for email
		String regexEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
		Pattern emailPattern = Pattern.compile(regexEmail);
		Matcher matcher = emailPattern.matcher(userInput);

		// Create variables
		boolean enteringEmailAddress = true;
		String emailAddress = null;

		// While loop to control for bad inputs
		while (enteringEmailAddress) {

			// Prompt to enter an email address
			System.out.println("Please enter an email," + " if left blank, a null" + " input will be recorded");
			;
			userInput = scObj.nextLine().strip();
			System.out.println("");
			matcher = emailPattern.matcher(userInput);

			// Conditions to check that the email is valid
			if (matcher.matches() && userInput.length() < 19) {
				System.out.println("Recording " + userInput + " as the " + personType + " email.");
				emailAddress = userInput;
				enteringEmailAddress = false;

			} else if (userInput.equals("")) {
				System.out.println("Recording a null" + " input as the email address");
				enteringEmailAddress = false;

			} else {
				System.out.println("That is not a" + " valid email address");
			}
		}
		System.out.println("");
		return emailAddress;
	}

	/**
	 * Method to get and return a person's street address from the user.
	 * 
	 * @param personType the type of person
	 * @param userInput  the user's input
	 * @param scObj      the object to get the user's input
	 * @return person's street address
	 */
	// Method to get a street address from the user
	public String getPersonStreetAddress(String personType, String userInput, Scanner scObj) {

		// Create variables
		boolean gettingAddress = true;
		String streetAddress = null;

		// While loop for bad inputs
		while (gettingAddress) {

			// Prompt the user for the street address
			System.out.println("Please enter the street address of the " + personType + "."
					+ " A blank input will record a null value");
			userInput = scObj.nextLine().strip();
			System.out.println("");

			if (userInput.equals("")) {
				System.out.println("Recording a null value for the address");
				gettingAddress = false;
			} else {
				System.out.println("Recording " + userInput + " as the street address");
				streetAddress = userInput;
				gettingAddress = false;
			}
		}
		System.out.println("");
		return streetAddress;
	}

	/**
	 * Method which gets and returns the postal code of a person from the user. Very
	 * few checks on format as international postal codes vary greatly.
	 * 
	 * @param personType type of person
	 * @param userInput  the user's input
	 * @param scObj      object which gets the user's input
	 * @return person's postal code
	 */
	// Get person postal code
	public String getPersonPostalCode(String personType, String userInput, Scanner scObj) {

		// Create variables
		String postalCode = null;
		boolean gettingPostalCode = true;

		// While loop for bad inputs
		while (gettingPostalCode) {

			// Prompt the user
			System.out.println("Please enter a postal code," + " this will record a null value if left blank");
			userInput = scObj.nextLine().strip();
			System.out.println("");

			if (userInput.equals("")) {
				System.out.println("Recording a null " + "value for the " + personType + " postal code.");
				gettingPostalCode = false;
			} else {
				System.out.println("Recording " + userInput + " as postal code.");
				System.out.println("");
				postalCode = userInput;
				gettingPostalCode = false;
			}
		}
		System.out.println("");
		return postalCode;
	}

	/**
	 * Method which enters a person into the database.
	 * 
	 * @param statement     the object which enters the person into the database
	 * @param personType    the type of person
	 * @param firstName     person's first name
	 * @param lastName      person's last name
	 * @param phoneNumber   person's phone number
	 * @param email         person's email address
	 * @param streetAddress person's street address
	 * @param postalCode    person's postal code
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Enter a new Person object
	public void enterNewPerson(Statement statement, String personType, String firstName, String lastName,
			String phoneNumber, String email, String streetAddress, String postalCode) throws SQLException {

		// Conditions for if these fields are filled in
		if (phoneNumber != null) {
			phoneNumber = "'" + phoneNumber + "'";
		}

		if (email != null) {
			email = "'" + email + "'";
		}

		if (streetAddress != null) {
			streetAddress = "'" + streetAddress + "'";
		}

		if (postalCode != null) {
			postalCode = "'" + postalCode + "'";
		}

		// Insert the person
		statement.executeUpdate("INSERT INTO " + personType + "s (" + personType + "_first_name, " + personType
				+ "_last_name, " + personType + "_phone_number, " + personType + "_email_address, " + personType
				+ "_street_address, " + personType + "_postal_code) VALUES('" + firstName + "' , '" + lastName + "' ,"
				+ phoneNumber + ", " + email + ", " + streetAddress + ", " + postalCode + ");");

		System.out.println(firstName + " " + lastName + " was entered into " + personType + "s.");
		System.out.println("");
	}

	/**
	 * Method which gets the id of the last person entered into a database of a
	 * certain type.
	 * 
	 * @param statement  object which queries the database
	 * @param results    object which stores the queried information
	 * @param personType person's type
	 * @return person's id
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method to get the id of the latest entry
	public String getIdLatestPersonEntry(Statement statement, ResultSet results, String personType)
			throws SQLException {

		results = statement
				.executeQuery("SELECT * FROM " + personType + "s ORDER BY " + personType + "_id DESC LIMIT 1;");

		results.next();
		String latestId = results.getString(personType + "_id");

		return latestId;
	}

	/**
	 * Method which presents a person's information.
	 * 
	 * @param results    object which stores queried informations
	 * @param statement  object which queries the database
	 * @param personType the person's type
	 * @param personId   the person's id
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method to prompt user to change a person's information
	public void presentPersonInformation(ResultSet results, Statement statement, String personType, String personId)
			throws SQLException {

		results = statement
				.executeQuery("SELECT * FROM " + personType + "s WHERE " + personType + "_id = " + personId + ";");

		results.next();

		System.out.println("1. First name: " + results.getString(personType + "_first_name"));
		System.out.println("2. Last name: " + results.getString(personType + "_last_name"));
		System.out.println("3. Phone number: " + results.getString(personType + "_phone_number"));
		System.out.println("4. Email: " + results.getString(personType + "_email_address"));
		System.out.println("5. Street address: " + results.getString(personType + "_street_address"));
		System.out.println("6. Postal code: " + results.getString(personType + "_postal_code"));
		System.out.println("");
	}

	/**
	 * Method which checks whether a person's phone number or email address are
	 * recorded as null in the database. Returns a boolean value, false if the field
	 * is full and true if the field is null.
	 * 
	 * @param results      object which stores queried information
	 * @param statement    object which queries the database
	 * @param fieldToCheck the field being checked
	 * @param personType   the person's type
	 * @param personId     the person's id
	 * @return boolean value
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method to check if a person's phone number or email address are NULL
	public boolean isPhoneOrEmailNull(ResultSet results, Statement statement, String fieldToCheck, String personType,
			String personId) throws SQLException {

		results = statement.executeQuery(
				"SELECT * FROM " + personType + "s " + " WHERE " + personType + "_" + fieldToCheck + " IS NULL;");

		if (results.isBeforeFirst()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Method which updates a person's information on a particular field in the
	 * database.
	 * 
	 * @param statement        object used to update the database
	 * @param needsParenthesis determines whether dataToInsert needs ''
	 * @param personType       person's type
	 * @param dataToInsert     the data being inserted
	 * @param fieldToUpdate    the field being updated
	 * @param personId         the person's id
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method to add new information to a person's table
	public void updatePersonFieldInfo(Statement statement, boolean needsParenthesis, String personType,
			String dataToInsert, String fieldToUpdate, String personId) throws SQLException {

		if (dataToInsert == null) {
			needsParenthesis = false;
		}

		if (needsParenthesis) {
			dataToInsert = "'" + dataToInsert + "'";
		}

		statement.executeUpdate("UPDATE " + personType + "s " + "SET " + personType + "_" + fieldToUpdate + " = "
				+ dataToInsert + " WHERE " + personType + "_id = " + personId + ";");

		System.out.println(personType + " updated with ID: " + personId);
		System.out.println("");
	}

}