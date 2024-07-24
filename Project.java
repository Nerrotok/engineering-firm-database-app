import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * This is the class which keeps project information such name, deadline, total
 * fee etc. using variables (Notably this includes project address information).
 * This class also contains methods which interact with the database when
 * relating to updating project information.
 * 
 * @author Connor Wallace
 * @version 1.00 09 July 2024
 */
public class Project {

	// Class variables for project
	String buildingType = null;
	String totalFee = null;
	String amountPaid = null;
	String deadline = null;
	String name = null;
	String iD = null;
	String finalisationDate = null;
	boolean finalised;

	// Project address variables
	String streetAddress = null;
	String postalCode = null;
	String erfNumber = null;
	String addressId = null;

	/**
	 * Method which assigns values to a project object. Specifically for building
	 * type, total fee, amount paid, and deadline.
	 * 
	 * @param project   project object
	 * @param sc        object to get user inputs
	 * @param userInput user's input
	 * @return updated project object
	 */
	// Method to assign values to a project object (which are not related to the
	// address)
	public Project assignProjectValues(Project project, Scanner sc, String userInput) {
		project.buildingType = project.getBuildingType(userInput, sc);
		project.totalFee = project.getTotalFee(userInput, sc);
		project.amountPaid = project.getAmountPaid(userInput, sc);
		project.deadline = project.getDeadline(userInput, sc);

		return project;
	}

	/**
	 * Method displays the project name and id for a project that a project address
	 * id is associated with.
	 * 
	 * @param results          the results of a query statement
	 * @param statement        the object used to query the database
	 * @param projectAddressId the id of a project address
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method to display projects associated with a projectAddress
	// Display all projects a projectAddress is associated with
	public void displayProjectsForProjectAddress(ResultSet results, Statement statement, String projectAddressId)
			throws SQLException {

		results = statement
				.executeQuery("SELECT * FROM project_info WHERE proj_address_id = " + projectAddressId + ";");

		while (results.next()) {

			System.out.println("Project Number: " + results.getString("proj_num") + ", Project Name: "
					+ results.getString("proj_name"));
		}
		System.out.println("");
	}

	/**
	 * Method which displays a project that is associated with a person.
	 * 
	 * @param results    the results of a query statement
	 * @param statement  the object used to query the database
	 * @param personType the type of person (customer, architect, contractor)
	 * @param personId   the id of the person
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method to display projects associated with a person
	// Method to display all projects a person is associated with
	public void displayProjectsPersonAssociatedWith(ResultSet results, Statement statement, String personType,
			String personId) throws SQLException {

		results = statement.executeQuery("SELECT * FROM project_info WHERE " + personType + "_id = " + personId + ";");

		while (results.next()) {

			System.out.println("Project Number: " + results.getString("proj_num") + ", Project Name: "
					+ results.getString("proj_name"));
		}
		System.out.println("");
	}

	/**
	 * 
	 * Method which displays the projects in the database and gets the name of an
	 * existing project from the user
	 * 
	 * @param userInput the user's input
	 * @param scObj     scanner object to get user inputs
	 * @param results   results of query statements
	 * @param statement statement used to execute queries
	 * @return the name of the chosen project
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method to search for a project based on name
	// Method to search for project based on name
	public String getProjectNameFromDB(String userInput, Scanner scObj, ResultSet results, Statement statement)
			throws SQLException {

		// Get projects from the database
		results = statement.executeQuery("SELECT * FROM project_info");

		// Create variables
		boolean gettingName = true;
		String name = null;

		// while loop for bad inputs
		while (gettingName) {

			// Prompt the user to select a name
			System.out.println("Please enter the name for the project (this is case sensitive): ");
			userInput = scObj.nextLine().strip();
			System.out.println("");

			// Check if the name exists
			results = statement.executeQuery("SELECT * FROM project_info WHERE proj_name = '" + userInput + "';");

			// Condition for if the name is in the database
			if (results.next()) {
				System.out.println("You have chosen Project: " + userInput);
				name = userInput;
				gettingName = false;

				// Condition for if the name is not in the database
			} else {
				System.out.println("Please enter a valid project name.");
			}
		}
		return name;
	}

	/**
	 * Method which displays the projects in the database and gets the id/project
	 * number of an existing project from the user. If no projects exist, returns an
	 * impossible ID/project number.
	 * 
	 * @param userInput the user's input
	 * @param scObj     scanner object to get user inputs
	 * @param results   results of query statements
	 * @param statement statement used to execute queries
	 * @return the id/project number of the chosen project
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method to get projectId
// Method to search for a project based on ID/project number
	public String getProjectId(String userInput, Scanner scObj, ResultSet results, Statement statement)
			throws SQLException {

		// Get projects from the database
		results = statement.executeQuery("SELECT * FROM project_info");

		// Create variables
		boolean gettingId = true;
		boolean isInt = true;
		String id = null;

		if (!results.isBeforeFirst()) {
			System.out.println("There are no projects in the database");
			id = "0";
		} else {
			// while loop for bad inputs
			while (gettingId) {

				// Prompt the user to select an ID
				System.out.println("Please select a Number for the project: ");
				userInput = scObj.nextLine().strip();
				System.out.println("");

				// Check if the userInput is an integer
				isInt = userInput.chars().allMatch(Character::isDigit);

				if (isInt) {

					// Check if the id exists
					results = statement
							.executeQuery("SELECT proj_num FROM project_info WHERE proj_num = " + userInput + ";");

					// Condition for if the id is in the database
					if (results.next()) {
						System.out.println("You have chosen Project: " + userInput);
						id = userInput;
						gettingId = false;

						// Condition for if the id is not in the database
					} else {
						System.out.println("Please enter a valid project number");
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
	 * Method which returns the choice of which information relating to a project
	 * that the user wants to edit.
	 * 
	 * @param userInput the user's input
	 * @param scObj     scanner object to get user inputs
	 * @return a number representing the user's choice of which filed they want to
	 *         edit
	 */
	// Method to get the user's choice for what they want to edit
// Method to get the user's choice of which project information they want to
	// edit
	public String getUserEditChoice(String userInput, Scanner scObj) {

		// Create variables
		boolean gettingInput = true;

		// Loop to control for bad inputs
		while (gettingInput) {

			System.out.println("Which field would you like to update?");
			userInput = scObj.nextLine().strip();

			switch (userInput) {

			case "1":
				gettingInput = false;
				break;

			case "2":
				gettingInput = false;
				break;

			case "3":
				gettingInput = false;
				break;

			case "4":
				gettingInput = false;
				break;

			case "5":
				gettingInput = false;
				break;

			case "6":
				gettingInput = false;
				break;

			case "7":
				gettingInput = false;
				break;

			case "8":
				gettingInput = false;
				break;

			case "9":
				gettingInput = false;
				break;

			case "10":
				gettingInput = false;
				break;

			case "11":
				gettingInput = false;
				break;
			default:
				System.out.println("Please enter a number 1-11.");
			}
		}
		return userInput;
	};

	/**
	 * 
	 * Method which returns the building type of a project using the projectId.
	 * 
	 * @param results   object to store the results of a statement query
	 * @param statement object used to query the database
	 * @param projectId the id/project number of the project the building type
	 *                  belongs to
	 * @return the building type of the project
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method to get the building type of a project
	// Method to return project buildingType based on project id
	public String getCurrentBuildingType(ResultSet results, Statement statement, String projectId) throws SQLException {

		results = statement.executeQuery("SELECT * FROM project_info WHERE proj_num = " + projectId + ";");
		results.next();
		String buildingType = results.getString("building_type");
		return buildingType;
	}

	/**
	 * Method which returns the the last name of a customer associated with a
	 * project.
	 * 
	 * @param results   the object which stores the results of a statement query
	 * @param statement object used to query the database
	 * @param projectId the id of the project the customer is associated with
	 * @return the last name of a customer associated with a project
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method to get the customer's last name from a project
	// Method to return customerLastName based on project id
	public String getCustomerLastNameFromProject(ResultSet results, Statement statement, String projectId)
			throws SQLException {

		results = statement.executeQuery("SELECT * FROM project_info WHERE proj_num = " + projectId + ";");
		results.next();
		String customerId = results.getString("customer_id");

		results = statement.executeQuery("SELECT * FROM customers WHERE customer_id = " + customerId + ";");
		results.next();
		String customerLastName = results.getString("customer_last_name");

		return customerLastName;
	}

	/**
	 * Method which displays a project's information using the project's name to get
	 * the information from the database
	 * 
	 * @param results     results object which stores the results of a statement
	 * @param statement   object used to query the database
	 * @param projectName the name of the project used to query the database for its
	 *                    information
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method to display a project using its name
	// Method to display a project based on name
	public void displayProjectName(ResultSet results, Statement statement, String projectName) throws SQLException {

		// Get information on the project
		results = statement.executeQuery("SELECT * FROM project_info WHERE proj_name = '" + projectName + "';");

		results.next();

		String addressId = results.getString("proj_address_id");
		String architectId = results.getString("architect_id");
		String contractorId = results.getString("contractor_id");
		String customerId = results.getString("customer_id");
		String finalisationStatus = null;

		if (results.getBoolean("finalised")) {
			finalisationStatus = "finalised";
		} else {
			finalisationStatus = "not finalised";
		}

		System.out.println("Project Number: " + results.getString("proj_num"));
		System.out.println("1. Project Name: " + results.getString("proj_name"));
		System.out.println("2. Building Type: " + results.getString("building_type"));
		System.out.println("3. Total Fee: " + results.getString("total_fee"));
		System.out.println("4. Amount Paid: " + results.getString("amount_paid"));
		System.out.println("5. Deadline: " + results.getString("proj_deadline"));
		System.out.println("6. Finalisation Status: " + finalisationStatus);

		if (results.getBoolean("finalised")) {
			System.out.println("7. Date finalised: " + results.getString("date_complete"));

		} else {
			System.out.println("7. Date finalised: null");
		}

		// Get information on the project's address
		results = statement.executeQuery("SELECT * FROM project_addresses WHERE proj_address_id = " + addressId + ";");

		results.next();

		System.out.println("8. Address: " + results.getString("proj_street_address"));

		results = statement.executeQuery("SELECT *" + " FROM architects WHERE architect_id = " + architectId + ";");

		if (results.next()) {
			System.out.println("9. Architect: " + results.getString("architect_first_name") + " "
					+ results.getString("architect_last_name"));
		} else {
			System.out.println("9. Architect: null");
		}
		;

		results = statement.executeQuery("SELECT * FROM contractors WHERE contractor_id = " + contractorId);

		if (results.next()) {
			System.out.println("10. Contractor: " + results.getString("contractor_first_name") + " "
					+ results.getString("contractor_last_name"));
		} else {
			System.out.println("10. Contractor: null");
		}
		;

		results = statement.executeQuery("SELECT * FROM customers WHERE customer_id = " + customerId + ";");

		if (results.next()) {
			System.out.println("11. Customer: " + results.getString("customer_first_name") + " "
					+ results.getString("customer_last_name"));
		} else {
			System.out.println("11. Customer: null");
		}
		;
		System.out.println("");

	}

	/**
	 * Method which displays a project's information using the project id to query
	 * the database for the informations
	 * 
	 * @param results   results object to store results of a query statement
	 * @param statement object used to query the database
	 * @param projectId the id/project number of the project which has its
	 *                  information displayed
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method to display a project using its ID / project number
	// Method to display a project based on Id
	public void displayProjectId(ResultSet results, Statement statement, String projectId) throws SQLException {

		// Get information on the project
		results = statement.executeQuery("SELECT * FROM project_info WHERE proj_num = " + projectId + ";");

		results.next();

		String addressId = results.getString("proj_address_id");
		String architectId = results.getString("architect_id");
		String contractorId = results.getString("contractor_id");
		String customerId = results.getString("customer_id");
		String finalisationStatusMessage = null;

		// Convert boolean to a string message
		if (results.getBoolean("finalised")) {
			finalisationStatusMessage = "finalised";
		} else {
			finalisationStatusMessage = "not finalised";
		}

		// Present
		System.out.println("Project Number: " + results.getString("proj_num"));
		System.out.println("1. Project Name: " + results.getString("proj_name"));
		System.out.println("2. Building Type: " + results.getString("building_type"));
		System.out.println("3. Total Fee: " + results.getString("total_fee"));
		System.out.println("4. Amount Paid: " + results.getString("amount_paid"));
		System.out.println("5. Deadline: " + results.getString("proj_deadline"));
		System.out.println("6. Finalisation Status: " + finalisationStatusMessage);

		// Only get date complete if the project is finalised, otherwise null
		if (results.getBoolean("finalised")) {
			System.out.println("7. Date finalised: " + results.getString("date_complete"));

		} else {
			System.out.println("7. Date finalised: null");
		}

		// Get information on the project's address
		results = statement.executeQuery("SELECT * FROM project_addresses WHERE proj_address_id = " + addressId + ";");

		results.next();

		System.out.println("8. Address: " + results.getString("proj_street_address"));

		results = statement.executeQuery("SELECT * FROM architects WHERE architect_id = " + architectId + ";");

		if (results.next()) {
			System.out.println("9. Architect: " + results.getString("architect_first_name") + " "
					+ results.getString("architect_last_name"));
		} else {
			System.out.println("9. Architect: null");
		}
		;

		results = statement.executeQuery("SELECT * FROM contractors WHERE contractor_id = " + contractorId + ";");

		if (results.next()) {
			System.out.println("10. Contractor: " + results.getString("contractor_first_name") + " "
					+ results.getString("contractor_last_name"));
		} else {
			System.out.println("10. Contractor: null");
		}
		;

		results = statement.executeQuery("SELECT * FROM customers WHERE customer_id = " + customerId + ";");

		if (results.next()) {
			System.out.println("11. Customer: " + results.getString("customer_first_name") + " "
					+ results.getString("customer_last_name"));
		} else {
			System.out.println("11. Customer: null");
		}
		;
		System.out.println("");

	}

	/**
	 * Method which gets a building type from the user and returns it
	 * 
	 * @param userInput the user's input
	 * @param scObj     scanner object to get the user's input
	 * @return a building type
	 */
	// Method to get building type from user
	// Method to get a building type from the user
	public String getBuildingType(String userInput, Scanner scObj) {

		// Create variables
		String buildingType = null;
		boolean enteringBuildingType = true;

		// While loop to control for empty inputs
		while (enteringBuildingType) {

			// Prompt the user to enter a building type
			System.out.println(
					"Please enter the building type (Apartment, House, Offices etc.). This is a mandatory field. ");
			userInput = scObj.nextLine().strip();
			System.out.println("");

			if (userInput.equals("")) {
				System.out.println("Please enter something");
			} else {
				System.out.println("Recording " + userInput + " as building type.");
				buildingType = userInput;
				enteringBuildingType = false;
			}
		}
		System.out.println("");
		return buildingType;
	}

	/**
	 * Method to get the amount of money paid for a project and return it.
	 * 
	 * @param userInput the user's input
	 * @param scObj     scanner object to get user inputs
	 * @return the amount of money paid
	 */
// Method to get the amount paid so far from the user

	// Method to get the amount paid for a project from the user
	public String getAmountPaid(String userInput, Scanner scObj) {

		// Set up regex
		String regexTwoDecimals = "^\\d+\\.\\d{2}$";
		Pattern twoDecimalPlacePattern = Pattern.compile(regexTwoDecimals);
		Matcher matcher = twoDecimalPlacePattern.matcher(userInput);

		// Create variables
		String amountPaid = null;
		boolean enteringAmountPaid = true;

		// While loop to control for bad inputs
		while (enteringAmountPaid) {

			// Prompt the user to enter an amount
			System.out.println("Please enter the amount paid so far, blank inputs will record R 0.00");
			userInput = scObj.nextLine().strip();
			System.out.println("");
			matcher = twoDecimalPlacePattern.matcher(userInput);

			// Check that the input is valid if entered, make it 0.00 if not
			if (userInput.equals("")) {
				System.out.println("Recording R 0.00 as the amount paid.");
				amountPaid = "0.00";
				enteringAmountPaid = false;
			} else if (matcher.matches() && userInput.length() < 15) {
				System.out.println("Recording R " + userInput + " as the amount paid.");
				amountPaid = userInput;
				enteringAmountPaid = false;
			} else {
				System.out.println("Please enter a number with two decimal places and is at most 14 characters");
			}
		}
		System.out.println("");
		return amountPaid;
	}

	/**
	 * Method which gets the total fee for a project from the user and returns it.
	 * 
	 * @param userInput the user's input
	 * @param scObj     scanner object to get user inputs
	 * @return the total fee
	 */
	// Method to get the total fee for a project from the user
	// Method to get the total fee from the user
	public String getTotalFee(String userInput, Scanner scObj) {

		// Set up regex
		String regexTwoDecimals = "^\\d+\\.\\d{2}$";
		Pattern twoDecimalPlacePattern = Pattern.compile(regexTwoDecimals);
		Matcher matcher = twoDecimalPlacePattern.matcher(userInput);

		// Create variables
		boolean enteringTotalFee = true;
		String totalFee = null;

		// While loop for bad inputs
		while (enteringTotalFee) {

			// Prompt the user to enter the total fee
			System.out.println("Please enter the total fee for the project (must include 2 decimal places),"
					+ " you can enter nothing for now: ");
			userInput = scObj.nextLine().strip();
			System.out.println("");
			matcher = twoDecimalPlacePattern.matcher(userInput);

			// Change totalFee to user input, otherwise it will stay null
			if (matcher.matches() && userInput.length() < 15) {
				System.out.println("Recording R " + userInput + " as the amount paid.");
				totalFee = userInput;
				enteringTotalFee = false;

				// Make sure the input is a valid number
			} else if (!userInput.equals("")) {
				System.out.println(
						"Please enter a number with two decimal places and is 14 characters long maximum or nothing.");
			} else {
				System.out.println("Recording a null value.");
				enteringTotalFee = false;
			}
		}

		return totalFee;
	}

	/**
	 * Method to get the finalisation date of a project from the user. Returns the
	 * finalisation date for the project
	 * 
	 * @param userInput the user's input
	 * @param scObj     scanner object to get the user's input
	 * @return the date of finalisation
	 */
// Method to get a project deadline from the user
	// Method to get the finalisation date of the project
	public String getFinalisationDate(String userInput, Scanner scObj) {

		// Create variables
		boolean enteringFinalisationDate = true;
		String finalisationDate = null;

		// Set up today's date
		LocalDate todayDate = LocalDate.now();

		// Set up regex
		String regex = "^\\d{4}-\\d{2}-\\d{2}$";
		Pattern datePattern = Pattern.compile(regex);

		// While loop to control for bad inputs
		while (enteringFinalisationDate) {

			// Prompt user to enter a deadline
			System.out.println("Please enter the finalisation date for the project, this is a mandatory field.");
			System.out.println("Format YYYY-MM-DD");
			userInput = scObj.nextLine().strip();
			Matcher matcher = datePattern.matcher(userInput);
			System.out.println("");

			if (!matcher.matches()) {
				System.out.println("Please enter in the format YYYY-MM-DD.");
			} else if (!checkDate(userInput)) {
				System.out.println("Please enter a valid date.");

				// If we get here the input is the correct format, need to check if it is in the
				// future
			} else {
				LocalDate userDate = LocalDate.parse(userInput);

				if (userDate.isAfter(todayDate)) {
					System.out.println("Please enter a date in the past or today.");
				} else {
					System.out.println("Recording " + userInput + " as the date of finalisation.");
					finalisationDate = userInput;
					enteringFinalisationDate = false;
				}
			}
		}
		return finalisationDate;

	}

	/**
	 * Method to get the deadline of a project from the user
	 * 
	 * @param userInput the user's input
	 * @param scObj     scanner object to get the user's input
	 * @return the deadline date for a project
	 */
	// Method to get deadline for project
	public String getDeadline(String userInput, Scanner scObj) {

		// Create variables
		boolean enteringDeadline = true;
		String deadline = null;

		// Set up today's date
		LocalDate todayDate = LocalDate.now();

		// Set up regex
		String regex = "^\\d{4}-\\d{2}-\\d{2}$";
		Pattern datePattern = Pattern.compile(regex);

		// While loop to control for bad inputs
		while (enteringDeadline) {

			// Prompt user to enter a deadline
			System.out.println(
					"Please enter the deadline for the project, if input is blank, a null value will be recorded");
			System.out.println("Format YYYY-MM-DD");
			userInput = scObj.nextLine().strip();
			Matcher matcher = datePattern.matcher(userInput);
			System.out.println("");

			if (userInput.equals("")) {
				System.out.println("Recording a null value.");
				enteringDeadline = false;
			} else if (!matcher.matches()) {
				System.out.println("Please enter in the format YYYY-MM-DD.");
			} else if (!checkDate(userInput)) {
				System.out.println("Please enter a valid date.");

				// If we get here the input is the correct format, need to check if it is in the
				// future
			} else {
				LocalDate userDate = LocalDate.parse(userInput);

				if (userDate.isBefore(todayDate) || userDate.isEqual(todayDate)) {
					System.out.println("Please enter a date in the future.");
				} else {
					System.out.println("Recording " + userInput + " as the deadline.");
					deadline = userInput;
					enteringDeadline = false;
				}
			}
		}
		return deadline;
	}

	/**
	 * 
	 * Method to get the name a project from the user. If no name is entered, the
	 * name will be made from the project's customer's last name and the project's
	 * building type. Returns the project name.
	 * 
	 * @param userInput           the user's input
	 * @param scObj               scanner object to get user inputs
	 * @param customerLastName    the last name of the customer associated with the
	 *                            project being named
	 * @param projectBuildingType the building type of the project
	 * @return the name of a project
	 */
	// Method to get project name
	public String getProjectName(String userInput, Scanner scObj, String customerLastName, String projectBuildingType) {

		String projectName = null;

		// prompt the user for an input
		System.out.println("Please enter a name for the project. If the field is left blank, a name will be"
				+ " generated from the building type and the customer's last name (BuildingType LastName)");
		userInput = scObj.nextLine().strip();
		System.out.println("");

		if (userInput.equals("")) {
			projectName = projectBuildingType + " " + customerLastName;
		} else {
			projectName = userInput;
		}

		return projectName;
	}

	/**
	 * Method which prompts the user to set the finalisation status of a project and
	 * returns a boolean based on whether the project is finalised or not.
	 * 
	 * @param userInput the user's input
	 * @param scObj     scanner object used to get the user's input
	 * @return boolean value representing finalisation status
	 */
	// get finalisation status from user
	public boolean getFinalisationStatus(String userInput, Scanner scObj) {

		boolean gettingFinalisationStatus = true;
		boolean finalised = false;

		while (gettingFinalisationStatus) {

			System.out.println("Is the project finalised?");
			System.out.println("1. Record as finalised");
			System.out.println("2. Record as not finalised");
			userInput = scObj.nextLine().strip();
			System.out.println("");

			if (userInput.equals("1")) {
				finalised = true;
				gettingFinalisationStatus = false;

			} else if (userInput.equals("2")) {
				finalised = false;
				gettingFinalisationStatus = false;
			} else {
				System.out.println("Please enter 1 or 2");
			}
		}
		return finalised;
	}

	/**
	 * Method which gets the finalisation status of a project in the database and
	 * returns a boolean valuer representing the status.
	 * 
	 * @param results   object which stores the results of a query statement
	 * @param statement object used to query the database
	 * @param projectId ID/project number of the project being queried
	 * @return boolean value for finalisation status
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method to get finalisation status based on id of project
	public boolean getFinalisationStatusFromDataBase(ResultSet results, Statement statement, String projectId)
			throws SQLException {

		boolean finalisationStatus;

		results = statement.executeQuery("SELECT * FROM" + " project_info WHERE proj_num = " + projectId + ";");

		results.next();
		finalisationStatus = results.getBoolean("finalised");
		return finalisationStatus;
	}

	/**
	 * Method to enter a new project into the database
	 * 
	 * @param statement           object which inserts the data to the database
	 * @param projectName         name of the project
	 * @param projectBuildingType building type of the project
	 * @param projectFee          total fee of the project
	 * @param amountPaid          money paid for the project
	 * @param projectDeadline     deadline for the project
	 * @param projectAddressId    the id of the project's address
	 * @param architectId         id of architect associated with the project
	 * @param contractorId        id of the contractor associated with the project
	 * @param customerId          id of the customer associated with the project
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method to insert the project in the database
	public void insertProject(Statement statement, String projectName, String projectBuildingType, String projectFee,
			String amountPaid, String projectDeadline, String projectAddressId, String architectId, String contractorId,
			String customerId) throws SQLException {

		// Condition for if projectDeadline is filled in
		if (projectDeadline != null) {
			projectDeadline = "'" + projectDeadline + "'";
		}

		// Insert the customer
		statement.executeUpdate("INSERT INTO project_info (proj_name, building_type, total_fee, "
				+ "amount_paid, proj_deadline, proj_address_id, architect_id, contractor_id,"
				+ "customer_id, finalised, date_complete) VALUES('" + projectName + "' , '" + projectBuildingType
				+ "' ," + projectFee + ", " + amountPaid + ", " + projectDeadline + ", " + projectAddressId + ", "
				+ architectId + ", " + contractorId + ", " + customerId + ", false, NULL);");

		System.out.println("Project " + projectName + " was entered into the database.");
		System.out.println("");
	}

	/**
	 * Method to display the project number, name and finalisation status of the
	 * projects in the database. Returns false if there are no projects in the
	 * database and returns true if there are.
	 * 
	 * @param results   object to store statement query results
	 * @param statement object used to query the database
	 * @return boolean value
	 * @throws SQLException message for if an SQL error occurs
	 */
// Method to show all projects
	public boolean displayProjects(ResultSet results, Statement statement) throws SQLException {

		String finalised = null;

		results = statement.executeQuery("SELECT * FROM project_info");

		if (!results.isBeforeFirst()) {
			return false;
		} else {
			while (results.next()) {

				if (results.getBoolean("finalised")) {
					finalised = "finalised";
				} else {
					finalised = "not finalised";
				}

				System.out.println("Project Number: " + results.getString("proj_num") + ", "
						+ results.getString("proj_name") + ", " + finalised);
			}
			return true;
		}
	}

	/**
	 * Method to update a field for a project in the database.
	 * 
	 * @param statement        object used to update the database
	 * @param needsParenthesis whether to input need '' or not
	 * @param fieldToUpdate    the field being updated
	 * @param dataToInsert     the data being inserted
	 * @param projectId        the id/project number
	 * @throws SQLException message for if an SQL error occurs
	 */
// Method to insert fields where the project will be updated
	public void updateFieldForProject(Statement statement, boolean needsParenthesis, String fieldToUpdate,
			String dataToInsert, String projectId) throws SQLException {

		if (dataToInsert == null) {
			needsParenthesis = false;
		}

		if (needsParenthesis) {
			dataToInsert = "'" + dataToInsert + "'";
		}

		statement.executeUpdate("UPDATE project_info SET " + fieldToUpdate + " = " + dataToInsert + " WHERE proj_num = "
				+ projectId + ";");

		System.out.println("Project " + fieldToUpdate + " information has been updated.");
		System.out.println("");
	}

	/**
	 * Method to update two fields at once for a project in a database
	 * 
	 * @param statement              object used to update the database
	 * @param firstNeedsParenthesis  whether the first data input needs ''
	 * @param firstFieldToUpdate     the field of the first data point
	 * @param firstDataToInsert      first data to insert
	 * @param secondNeedsParenthesis whether the second data input needs ''
	 * @param secondFieldToUpdate    the field of the second data point
	 * @param secondDataToInsert     second data to insert
	 * @param projectId              the id/project number
	 * @throws SQLException message for if an SQL error occurs
	 */
	public void updateTwoFieldsForProject(Statement statement, boolean firstNeedsParenthesis, String firstFieldToUpdate,
			String firstDataToInsert, boolean secondNeedsParenthesis, String secondFieldToUpdate,
			String secondDataToInsert, String projectId) throws SQLException {

		if (firstNeedsParenthesis) {
			firstDataToInsert = "'" + firstDataToInsert + "'";
		}

		if (secondNeedsParenthesis) {
			secondDataToInsert = "'" + secondDataToInsert + "'";
		}

		statement.executeUpdate("UPDATE project_info SET " + firstFieldToUpdate + " = " + firstDataToInsert + ", "
				+ secondFieldToUpdate + " = " + secondDataToInsert + " WHERE proj_num = " + projectId + ";");

		System.out.println(
				"Project " + firstFieldToUpdate + " and " + secondFieldToUpdate + " information has been updated.");
		System.out.println("");
	}

	/**
	 * Method to get and return the id of a person or project address from a
	 * project.
	 * 
	 * @param results      the object to store the results of a query
	 * @param statement    object to query the database
	 * @param projectId    the id/project number
	 * @param idToGetField the field of the id the method returns
	 * @return the id of the object in the field
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method to get id which references another table
	public String getIdForOtherTable(ResultSet results, Statement statement, String projectId, String idToGetField)
			throws SQLException {

		String idOfField = null;

		// Get information on the project
		results = statement.executeQuery("SELECT * FROM project_info WHERE proj_num = " + projectId + ";");

		// Get id from specific field
		results.next();
		idOfField = results.getString(idToGetField);

		return idOfField;
	}

	/**
	 * Method to delete a project from the database based on the id/project number.
	 * 
	 * @param statement object used to delete the project from the database
	 * @param projectId the id/project number
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method to delete a project
	public void deleteProject(Statement statement, String projectId) throws SQLException {

		statement.executeUpdate("DELETE FROM project_info WHERE proj_num = " + projectId + ";");

		System.out.println("The project with project number " + projectId + " has been deleted from the database.");
		System.out.println("");

	}

	/**
	 * Method to display all projects where the due date has passed. Displays name,
	 * project number, and deadline.
	 * 
	 * @param results   object to store query results
	 * @param statement object to query the database
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method to show all overdue projects
	public void showAllOverdueProjects(ResultSet results, Statement statement) throws SQLException {

		// Get today's date
		LocalDate todayDate = LocalDate.now();
		LocalDate projectDate;

		results = statement.executeQuery("SELECT * FROM project_info WHERE finalised = false;");

		System.out.println("These projects are overdue:");

		while (results.next()) {

			if (results.getString("proj_deadline") != null) {

				projectDate = LocalDate.parse(results.getString("proj_deadline"));

				if (projectDate.isBefore(todayDate)) {
					System.out.println("Project Number: " + results.getString("proj_num") + ", Project Name: "
							+ results.getString("proj_name") + ", Deadline: " + results.getString("proj_deadline"));
				}
			}
		}
		System.out.println("");
	}

	/**
	 * Method which displays all projects that are not finalised. Displays name,
	 * project number and deadline.
	 * 
	 * @param results   object which stores query results
	 * @param statement object which queries the database
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method to show all unfinalised projects
	public void showAllUnfinalisedProjects(ResultSet results, Statement statement) throws SQLException {

		results = statement.executeQuery("SELECT * FROM project_info WHERE finalised = false;");

		System.out.println("Unfinalised projects:");
		System.out.println("");

		while (results.next()) {
			System.out.println("Project Number: " + results.getString("proj_num") + ", Project Name: "
					+ results.getString("proj_name") + ", Deadline: " + results.getString("proj_deadline"));
		}

		System.out.println("");
	}

	/**
	 * Method which returns a boolean value to show whether a user input can be
	 * parsed as a date. Returns true if it can, returns false if it can't
	 * 
	 * @param userInput user's input
	 * @return boolean value
	 */
	// Method to check if a date is an actual date
	public static boolean checkDate(String userInput) {

		try {
			LocalDate parsedDate = LocalDate.parse(userInput);
			return true;
		} catch (DateTimeParseException e) {
			return false;
		}
	}
}