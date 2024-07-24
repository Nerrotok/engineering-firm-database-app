import java.sql.*;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * This class contains methods which handles information regarding project
 * addresses and their interactions with the database.
 */
public class ProjectAddressHandler {

	/**
	 * Method which deletes a project address from the database. It checks if the
	 * project address is associated with a project, if so it will display the
	 * projects associated, otherwise it will ask if the user is sure they want to
	 * delete it. If so, the project address will be deleted from the database.
	 * 
	 * @param results               object to store information queried from the
	 *                              database
	 * @param statement             object to query and update the database
	 * @param project               project object
	 * @param projectAddressHandler object which handles project addresses
	 * @param prompt                object containing prompts and processes user
	 *                              inputs
	 * @param userInput             the user's input
	 * @param sc                    object to get user inputs
	 * @throws SQLException message for if an SQL error occurs
	 */
	public void deleteProjectAddress(ResultSet results, Statement statement, Project project,
			ProjectAddressHandler projectAddressHandler, Prompt prompt, String userInput, Scanner sc)
			throws SQLException {

		if (projectAddressHandler.projectAddressOnProject(results, statement, project.addressId)) {
			System.out.println("You may not delete this project address as it is associate with a project/s: ");
			project.displayProjectsForProjectAddress(results, statement, project.addressId);
		} else {
			prompt.areYouSureDeletePrompt("project address");
			userInput = prompt.getUserChoiceTwoOptions(userInput, sc);

			if (userInput.equals("1")) {
				projectAddressHandler.deleteProjectAddressFromDB(statement, project.addressId);
			} else if (userInput.equals("2")) {
				System.out.println("The project address has not been deleted");
				System.out.println("");
			}
		}
	}

	/**
	 * Method which updates a field in a project address in the database. It takes
	 * in a user input and updates the field based on which option the user chose.
	 * 
	 * @param userInput             user's input
	 * @param projectAddressHandler projectAddress handling object
	 * @param project               project object
	 * @param statement             object to update the database
	 * @param sc                    object to get user inputs
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method to update project street address
	public void updateProjectAddressField(String userInput, ProjectAddressHandler projectAddressHandler,
			Project project, Statement statement, Scanner sc) throws SQLException {

		// Update street address
		if (userInput.equals("1")) {

			project.streetAddress = projectAddressHandler.getProjectStreetAddress(userInput, sc);
			projectAddressHandler.updateProjectAddressInfo(statement, true, project.streetAddress,
					"proj_street_address", project.addressId);

			// Update postal code
		} else if (userInput.equals("2")) {
			project.postalCode = projectAddressHandler.getProjectPostalCode(userInput, sc);
			projectAddressHandler.updateProjectAddressInfo(statement, true, project.postalCode, "proj_postal_code",
					project.addressId);

			// Update ERF number
		} else if (userInput.equals("3")) {
			project.erfNumber = projectAddressHandler.getProjectErfNumber(userInput, sc);
			projectAddressHandler.updateProjectAddressInfo(statement, false, project.erfNumber, "proj_erf_num",
					project.addressId);
		}
	}

	/**
	 * Method which gets project address values from the user, stores the project
	 * address to the database, and returns the project with its new values.
	 * 
	 * @param sc                    object to get user inputs
	 * @param results               object to store query results
	 * @param statement             object to query and update the database
	 * @param projectAddressHandler object to handle project addresses
	 * @param project               project object
	 * @param userInput             user's input
	 * @return updated project object
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method to assign project address values to a project object
	public Project assignProjectAddressValues(Scanner sc, ResultSet results, Statement statement,
			ProjectAddressHandler projectAddressHandler, Project project, String userInput) throws SQLException {
		project.streetAddress = projectAddressHandler.getProjectStreetAddress(userInput, sc);
		project.postalCode = projectAddressHandler.getProjectPostalCode(userInput, sc);
		project.erfNumber = projectAddressHandler.getProjectErfNumber(userInput, sc);
		projectAddressHandler.enterNewProjAddress(statement, project.streetAddress, project.postalCode,
				project.erfNumber);
		project.addressId = projectAddressHandler.getIdLatestProjectAddressEntry(statement, results);

		return project;
	}

	/**
	 * Method which deletes a project address from the database.
	 * 
	 * @param statement        object which interacts with the database
	 * @param projectAddressId id of the project address
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method to delete projectAddress from database
	public void deleteProjectAddressFromDB(Statement statement, String projectAddressId) throws SQLException {

		statement.executeUpdate("DELETE FROM project_addresses WHERE proj_address_id = " + projectAddressId + ";");

		System.out.println("The project address has been deleted");
	}

	/**
	 * Method which returns a boolean value to show whether a project address is
	 * associated with a project based on its id. true is returned if the project
	 * address is associated with a project and false is returned if not.
	 * 
	 * @param results          object which stores information queried from the
	 *                         database
	 * @param statement        object which queries the database
	 * @param projectAddressId the project address's id
	 * @return boolean value
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Check if projectAddress is associated with a project
	public boolean projectAddressOnProject(ResultSet results, Statement statement, String projectAddressId)
			throws SQLException {

		boolean projectAddressOnProject;

		results = statement
				.executeQuery("SELECT * FROM project_info " + "WHERE proj_address_id = " + projectAddressId + ";");

		if (results.isBeforeFirst()) {
			projectAddressOnProject = true;
		} else {
			projectAddressOnProject = false;
		}
		return projectAddressOnProject;
	}

	/**
	 * Method which gets a project's street address from the user.
	 * 
	 * @param userInput the user's input
	 * @param scObj     object used to get user input
	 * @return project address street address
	 */
	// Method to get a street address from the user
	public String getProjectStreetAddress(String userInput, Scanner scObj) {

		// Create variables
		boolean gettingAddress = true;
		String streetAddress = null;

		// While loop for bad inputs
		while (gettingAddress) {

			// Prompt the user for the street address
			System.out.println("Please enter the street address of the project. This is a mandatory field.");
			userInput = scObj.nextLine().strip();
			System.out.println("");

			if (userInput.equals("")) {
				System.out.println("Please enter something, this field cannot be blank.");
			} else {
				streetAddress = userInput;
				gettingAddress = false;
			}
		}
		System.out.println("Recording " + streetAddress + " as the project street address.");
		System.out.println("");
		return streetAddress;
	}

	/**
	 * Method which gets and returns the postal code of a project address from the
	 * user.
	 * 
	 * @param userInput user's input
	 * @param scObj     object to get user's input
	 * @return project address's postal code
	 */
	// Method to get a postal code from the user
	public String getProjectPostalCode(String userInput, Scanner scObj) {

		// Create variables
		String postalCode = null;
		boolean gettingPostalCode = true;

		// While loop for bad inputs
		while (gettingPostalCode) {

			// Prompt the user
			System.out.println("Please enter a postal code, this is a mandatory field.");
			userInput = scObj.nextLine().strip();
			System.out.println("");

			if (userInput.equals("")) {
				System.out.println("Please enter a postal code.");
			} else {
				System.out.println("Recording " + userInput + " as postal code.");
				System.out.println("");
				postalCode = userInput;
				gettingPostalCode = false;
			}
		}
		return postalCode;
	}

	/**
	 * Method which gets an ERF number for a project address from the user and
	 * returns it.
	 * 
	 * @param userInput the user's input
	 * @param scObj     object which gets the user's input
	 * @return project address's ERF number
	 */
	// Method to get an ERF number from the user
	public String getProjectErfNumber(String userInput, Scanner scObj) {

		// Create variables
		String erfNumber = null;
		boolean gettingErfNumber = true;

		// Set up regex
		String regexInteger = "^[1-9]\\d*$|^0$";
		Pattern integerPattern = Pattern.compile(regexInteger);

		// While loop for bad inputs
		while (gettingErfNumber) {

			// Prompt the user
			System.out.println("Please enter an ERF number," + " if left blank a null value will be recorded.");
			userInput = scObj.nextLine().strip();
			Matcher matcher = integerPattern.matcher(userInput);
			System.out.println("");

			/*
			 * Record null value for ERF number as they are not essential when starting a
			 * project it is however preferable for record keeping and legal issues. Option
			 * for null value is useful if building outside South Africa
			 */
			if (userInput.equals("")) {
				System.out.println("Recording a null value as the ERF number.");
				gettingErfNumber = false;

				// Check for if ERF number is not valid
			} else if (!matcher.matches()) {
				System.out.println("That is not a valid ERF number.");
				// Store valid ERF number
			} else {
				System.out.println("Recording " + userInput + " as the ERF number.");
				erfNumber = userInput;
				gettingErfNumber = false;
			}
		}
		System.out.println("");
		return erfNumber;
	}

	/**
	 * Method which displays project addresses from the database. It then prompts
	 * the user to choose one based on ID and will return this ID. If there are no
	 * project_addresses in the database, it will return an impossible ID.
	 * 
	 * @param userInput the user's input
	 * @param scObj     the object to get user inputs
	 * @param results   the object which stores information queried
	 * @param statement the object which queries the database
	 * @return project address ID
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method to get a Street Address ID from the database
	public String getProjectStreetAddressId(String userInput, Scanner scObj, ResultSet results, Statement statement)
			throws SQLException {

		// Get addresses from the database
		results = statement.executeQuery("SELECT * FROM project_addresses");

		// Create variables
		boolean gettingId = true;
		boolean isInt = true;
		String id = null;

		if (!results.isBeforeFirst()) {
			System.out.println("There are no project addresses in the database.");
			id = "0";
		} else {
			// Display the addresses
			System.out.println("Project Addresses in database:");

			while (results.next()) {
				System.out.println("ID: " + results.getString("proj_address_id") + ", Street Address: "
						+ results.getString("proj_street_address") + ", Postal Code: "
						+ results.getString("proj_postal_code") + ", ERF Num: " + results.getString("proj_erf_num"));
			}

			// while loop for bad inputs
			while (gettingId) {

				// Prompt the user to select an ID
				System.out.println("Please select an ID for the Project Address: ");
				userInput = scObj.nextLine().strip();
				System.out.println("");

				// Check if the userInput is an integer
				isInt = userInput.chars().allMatch(Character::isDigit);

				if (isInt) {

					// Check if the id exists
					results = statement.executeQuery("SELECT proj_address_id FROM" + " project_addresses"
							+ " WHERE proj_address_id = " + userInput + ";");

					// Condition for if the id is in the database
					if (results.next()) {
						System.out.println("You have chosen ID: " + userInput);
						id = userInput;
						gettingId = false;

						// Condition for if the id is not in the database
					} else {
						System.out.println("Please enter a valid ID");
					}

					// Condition for if an integer was not entered
				} else {
					System.out.println("Please enter an integer.");
				}
			}
		}
		return id;
	}

	/**
	 * Method which enters a new project address into the database.
	 * 
	 * @param statement     object which inserts the data to the database
	 * @param streetAddress the street address of the project address
	 * @param postalCode    the postal code of the project address
	 * @param erfNum        the ERF number of the project address
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method to enter a new project address to the database
	public void enterNewProjAddress(Statement statement, String streetAddress, String postalCode, String erfNum)
			throws SQLException {

		if (erfNum != null) {
			erfNum = "'" + erfNum + "'";
		}

		statement.executeUpdate("INSERT INTO project_addresses (proj_street_address, proj_postal_code, proj_erf_num)"
				+ "VALUES('" + streetAddress + "', '" + postalCode + "', " + erfNum + ");");

	}

	/**
	 * Method which gets the ID of the last project address entered into the
	 * database.
	 * 
	 * @param statement object which queries the database
	 * @param results   object which stores queried information
	 * @return id of project address
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method to get the id of the latest entry
	public String getIdLatestProjectAddressEntry(Statement statement, ResultSet results) throws SQLException {

		results = statement.executeQuery("SELECT * FROM project_addresses ORDER BY proj_address_id DESC LIMIT 1;");

		results.next();
		String latestId = results.getString("proj_address_id");

		return latestId;
	}

	/**
	 * Method which displays a project address's information.
	 * 
	 * @param results          object which stores queried data
	 * @param statement        object which queries the database
	 * @param projectAddressId project address id
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method to display projectAddress information
	public void displayProjectAddressInfo(ResultSet results, Statement statement, String projectAddressId)
			throws SQLException {

		results = statement.executeQuery(
				"SELECT * FROM project" + "_addresses" + " WHERE proj_address_id = " + projectAddressId + ";");

		results.next();
		System.out.println("1. Street Address: " + results.getString("proj_street_address"));
		System.out.println("2. Postal Code: " + results.getString("proj_postal_code"));
		System.out.println("3. ERF number: " + results.getString("proj_erf_num"));
		System.out.println("");
	}

	/**
	 * Method which updates a project address's information in a single field.
	 * 
	 * @param statement        object which updates the database
	 * @param needsParenthesis whether the infoToAdd needs '' or not
	 * @param infoToAdd        the data to insert to the database
	 * @param fieldToUpdate    the field where the data goes
	 * @param projectAddressId the project address's id
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Update projectAddress information for a single field
	public void updateProjectAddressInfo(Statement statement, boolean needsParenthesis, String infoToAdd,
			String fieldToUpdate, String projectAddressId) throws SQLException {

		if (needsParenthesis) {
			infoToAdd = "'" + infoToAdd + "'";
		}

		statement.executeUpdate("UPDATE project_addresses SET " + fieldToUpdate + " = " + infoToAdd
				+ " WHERE proj_address_id = " + projectAddressId);

		System.out.println("Project Address information has been updated");

	}
}
