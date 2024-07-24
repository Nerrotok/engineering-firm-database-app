import java.sql.*;
import java.util.Scanner;

/**
 * The Main class begins here where the actual program will run. The program
 * allows the user to add, edit, display and delete information from the
 * associated database. The main class also contains methods which prompt the
 * user and receive user inputs.
 * 
 * @author Connor Wallace
 * @version 1.00 09 July 2024
 */
public class Main {

	/**
	 * The main method where the program runs
	 * 
	 * @param args default value for main
	 */
	public static void main(String[] args) {
		// Try statement for SQL errors
		try {

			// Connect to the database
			// Establish connection to database using jdbc:mysql
			// jdbc:mysql://localhost:[PORT_NUMBER]/[database_name]?allowPublicKeyRetrieval=true&useSSL=false",
			// "[USER_NAME]", "[USER_PASSWORD]"
			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3310/PoisePMS?allowPublicKeyRetrieval=true&useSSL=false", "otheruser",
					"swordfish");
			// Create a direct line to the database for running our queries
			Statement statement = connection.createStatement();
			ResultSet results = null;

			// Create classes
			Prompt prompt = new Prompt();
			Project project = new Project();
			Person customer = new Person();
			Person architect = new Person();
			Person contractor = new Person();
			ProjectAddressHandler projectAddressHandler = new ProjectAddressHandler();
			PersonHandler personHandler = new PersonHandler();

			// Create variables
			// Every time the program runs
			String userInput = null;
			boolean programRunning = true;

			// Create scanner object
			Scanner sc = new Scanner(System.in);

			// Program starts here
			// Loop to keep program running
			while (programRunning) {

				// Ask the user what they want to do
				prompt.initialPrompt();
				userInput = sc.nextLine().strip();
				System.out.println("");

				switch (userInput) {

				// Enter a new project
				case "1":
					enterNewProjectOption(sc, userInput, architect, customer, contractor, results, statement, project,
							projectAddressHandler, prompt, personHandler);
					break;

				// Enter a new person (Customer, architect, or contractor)
				case "2":
					enterNewPersonOption(sc, userInput, architect, customer, contractor, results, statement, prompt,
							personHandler);
					break;

				// Update a project
				case "3":
					updateProjectOption(sc, userInput, architect, customer, contractor, results, statement, project,
							projectAddressHandler, prompt, personHandler);

					break;

				// Update a person (Customer, architect, or contractor)
				case "4":
					updatePersonOption(sc, userInput, architect, customer, contractor, results, statement, prompt,
							personHandler);

					break;

				// Update project address
				case "5":
					updateProjectAddressOption(prompt, statement, results, userInput, sc, project,
							projectAddressHandler);
					break;

				// Search a project (based on name or proj_num)
				case "6":
					searchProjectOption(project, userInput, sc, results, prompt, statement);
					break;

				// Search a person (Customer, architect, or contractor) (based on id)
				case "7":
					searchPersonOption(sc, userInput, prompt, personHandler, customer, architect, contractor, results,
							statement);
					break;

				// See unfinalised projects
				case "8":
					project.showAllUnfinalisedProjects(results, statement);
					break;

				// See overdue projects
				case "9":
					project.showAllOverdueProjects(results, statement);
					break;

				// Delete project
				case "10":
					deleteProjectOption(project, results, statement, userInput, sc, prompt);

					break;

				// Delete person
				case "11":
					deletePersonPromptOption(prompt, userInput, sc, results, customer, architect, contractor, project,
							statement, personHandler);
					break;

				// Delete project_address
				case "12":
					deleteProjectAddressOption(project, projectAddressHandler, userInput, sc, results, statement,
							prompt);
					break;

				// Close the program
				case "0":
					System.out.println("The program is closing");
					System.out.println("");
					programRunning = false;
					break;

				// Default case for incorrect inputs
				default:
					System.out.println("Please enter a digit from 0-12");
					System.out.println("");
				}
			}

			// Close up our connections to the database
			if (results != null) {
				results.close();
			}
			statement.close();
			connection.close();

			// Catch statement for any SQL errors
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method for if the user chooses to enter a new project on the initial prompt.
	 * It will get information regarding the project from the user and enter it into
	 * the database.
	 * 
	 * @param sc                    object to get user inputs
	 * @param userInput             user's input
	 * @param architect             architect person object
	 * @param customer              customer person object
	 * @param contractor            contractor person object
	 * @param results               object which stores queried data
	 * @param statement             object which queries and updates the database
	 * @param project               project object
	 * @param projectAddressHandler object which handles project address information
	 * @param prompt                object containing prompts and checks user inputs
	 * @param personHandler         object which handles person information
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method for if the user chooses to enter a new project
	public static void enterNewProjectOption(Scanner sc, String userInput, Person architect, Person customer,
			Person contractor, ResultSet results, Statement statement, Project project,
			ProjectAddressHandler projectAddressHandler, Prompt prompt, PersonHandler personHandler)
			throws SQLException {
		// Get project address
		// Give option to choose address ID or enter new project address
		prompt.newEntityOrIdPromptTwoOptions("Project Address");
		userInput = prompt.getUserChoiceTwoOptions(userInput, sc);
		System.out.println("For the new Project: ");
		System.out.println("");

		// Enter new projectAddress
		if (userInput.equals("1")) {
			project = projectAddressHandler.assignProjectAddressValues(sc, results, statement, projectAddressHandler,
					project, userInput);

			// choose from database if no options exist, make a new one
		} else if (userInput.equals("2")) {
			project.addressId = projectAddressHandler.getProjectStreetAddressId(userInput, sc, results, statement);
			// condition for no project addresses in the database
			if (project.addressId.equals("0")) {
				System.out.println("You must enter a new one.");
				project = projectAddressHandler.assignProjectAddressValues(sc, results, statement,
						projectAddressHandler, project, userInput);
			}
		}

		// Project fields from user
		project = project.assignProjectValues(project, sc, userInput);
		// Get architect
		// Give option to choose architect ID from database, enter new architect or not
		// select one as as the architect might not be assigned yet
		prompt.newEntityOrIdPromptThreeOptions("Architect");
		userInput = prompt.getUserChoiceThreeOptions(userInput, sc);

		// Enter new architect
		if (userInput.equals("1")) {
			architect = personHandler.assignNewPersonValuesAndEnterToDatabase(architect, "architect", userInput, sc,
					results, statement, personHandler);

			// choose from database
		} else if (userInput.equals("2")) {
			architect.iD = personHandler.getPersonId(sc, results, statement, "architect", userInput);
			// Condition for if there are not architects
			if (architect.iD.equals("0")) {
				System.out.println("You must enter a new one.");
				architect = personHandler.assignNewPersonValuesAndEnterToDatabase(architect, "architect", userInput, sc,
						results, statement, personHandler);
			}

			// Do not select architect
		} else if (userInput.equals("3")) {
			architect.iD = null;
		}

		// Get contractor
		// Give option to choose contractor ID from database, enter new contractor or
		// not select one as as the contractor might not be assigned yet
		prompt.newEntityOrIdPromptThreeOptions("Contractor");
		userInput = prompt.getUserChoiceThreeOptions(userInput, sc);

		// Enter new contractor
		if (userInput.equals("1")) {
			contractor = personHandler.assignNewPersonValuesAndEnterToDatabase(contractor, "contractor", userInput, sc,
					results, statement, personHandler);

			// choose from database
		} else if (userInput.equals("2")) {
			contractor.iD = personHandler.getPersonId(sc, results, statement, "contractor", userInput);
			// Condition for if there are no contractors
			if (contractor.iD.equals("0")) {
				System.out.println("You must enter a new one.");
				contractor = personHandler.assignNewPersonValuesAndEnterToDatabase(contractor, "contractor", userInput,
						sc, results, statement, personHandler);
			}

			// Do not select contractor
		} else if (userInput.equals("3")) {
			contractor.iD = null;
		}

		// Get customer
		// Give option to choose address ID or enter new customer
		prompt.newEntityOrIdPromptTwoOptions("Customer");
		userInput = prompt.getUserChoiceTwoOptions(userInput, sc);

		// Enter new customer
		if (userInput.equals("1")) {
			customer = personHandler.assignNewPersonValuesAndEnterToDatabase(customer, "customer", userInput, sc,
					results, statement, personHandler);

			// choose from database
		} else if (userInput.equals("2")) {
			customer.iD = personHandler.getPersonId(sc, results, statement, "customer", userInput);

			// Condition for if there are no customers
			if (customer.iD.equals("0")) {
				System.out.println("You must enter a new one.");
				customer = personHandler.assignNewPersonValuesAndEnterToDatabase(customer, "customer", userInput, sc,
						results, statement, personHandler);
			} else {
				customer.lastName = personHandler.getCustomerLastNameFromId(results, statement, customer.iD);
			}
		}

		// Get project name
		project.name = project.getProjectName(userInput, sc, customer.lastName, project.buildingType);

		// Enter the project
		project.insertProject(statement, project.name, project.buildingType, project.totalFee, project.amountPaid,
				project.deadline, project.addressId, architect.iD, contractor.iD, customer.iD);
	}

	/**
	 * Method for if the user chooses to enter a new person on the initial prompt.
	 * It will get the user to pick a person type to enter, and then enter that
	 * information.
	 * 
	 * @param sc            object to get user inputs
	 * @param userInput     user's input
	 * @param architect     architect person object
	 * @param customer      customer person object
	 * @param contractor    contractor person object
	 * @param results       object which stores queried data
	 * @param statement     object which queries and updates the database
	 * @param prompt        object containing prompts and checks user inputs
	 * @param personHandler object which handles person information
	 * @throws SQLException message for if an SQL error occurs
	 */
	// Method to enter a new person
	public static void enterNewPersonOption(Scanner sc, String userInput, Person architect, Person customer,
			Person contractor, ResultSet results, Statement statement, Prompt prompt, PersonHandler personHandler)
			throws SQLException {
		prompt.personPrompt();
		userInput = prompt.getUserChoiceThreeOptions(userInput, sc);

		// Enter new Customer
		if (userInput.equals("1")) {
			customer = personHandler.assignNewPersonValuesAndEnterToDatabase(customer, "customer", userInput, sc,
					results, statement, personHandler);
			System.out.println(
					"New customer " + customer.firstName + " " + customer.lastName + " added with ID: " + customer.iD);

			// Enter new Architect
		} else if (userInput.equals("2")) {
			architect = personHandler.assignNewPersonValuesAndEnterToDatabase(architect, "architect", userInput, sc,
					results, statement, personHandler);
			System.out.println("New architect " + architect.firstName + " " + architect.lastName + " added with ID: "
					+ architect.iD);

			// Enter new Contractor
		} else if (userInput.equals("3")) {
			contractor = personHandler.assignNewPersonValuesAndEnterToDatabase(contractor, "contractor", userInput, sc,
					results, statement, personHandler);
			System.out.println("New contractor " + contractor.firstName + " " + contractor.lastName + " added with ID: "
					+ contractor.iD);
		}
	}

	/**
	 * Method for if the user chooses to update project information on the initial
	 * prompt. It will ask the user to choose a project and ask them which field
	 * they want to update.
	 * 
	 * @param sc                    object to get user inputs
	 * @param userInput             user's input
	 * @param architect             architect person object
	 * @param customer              customer person object
	 * @param contractor            contractor person object
	 * @param results               object which stores queried data
	 * @param statement             object which queries and updates the database
	 * @param project               project object
	 * @param projectAddressHandler object which handles project address information
	 * @param prompt                object containing prompts and checks user inputs
	 * @param personHandler         object which handles person information
	 * @throws SQLException message for if an SQL error occurs
	 */
	public static void updateProjectOption(Scanner sc, String userInput, Person architect, Person customer,
			Person contractor, ResultSet results, Statement statement, Project project,
			ProjectAddressHandler projectAddressHandler, Prompt prompt, PersonHandler personHandler)
			throws SQLException {

		if (!project.displayProjects(results, statement)) {

			System.out.println("There are no projects to update");
		} else {
			// User selects project based on ID/project number
			project.iD = project.getProjectId(userInput, sc, results, statement);
			project.displayProjectId(results, statement, project.iD);
			userInput = project.getUserEditChoice(userInput, sc);

			// Update project name
			if (userInput.equals("1")) {
				project.buildingType = project.getCurrentBuildingType(results, statement, project.iD);
				customer.lastName = project.getCustomerLastNameFromProject(results, statement, project.iD);
				project.name = project.getProjectName(userInput, sc, customer.lastName, project.buildingType);
				project.updateFieldForProject(statement, true, "proj_name", project.name, project.iD);

				// Update project building type
			} else if (userInput.equals("2")) {
				project.buildingType = project.getBuildingType(userInput, sc);
				project.updateFieldForProject(statement, true, "building_type", project.buildingType, project.iD);

				// Update total fee
			} else if (userInput.equals("3")) {
				project.totalFee = project.getTotalFee(userInput, sc);
				project.updateFieldForProject(statement, false, "total_fee", project.totalFee, project.iD);

				// Update amount paid
			} else if (userInput.equals("4")) {
				project.amountPaid = project.getAmountPaid(userInput, sc);
				project.updateFieldForProject(statement, false, "amount_paid", project.amountPaid, project.iD);

				// Update project deadline
			} else if (userInput.equals("5")) {
				project.deadline = project.getDeadline(userInput, sc);
				project.updateFieldForProject(statement, true, "proj_deadline", project.deadline, project.iD);

				// Update finalisation status
			} else if (userInput.equals("6")) {
				project.finalised = project.getFinalisationStatus(userInput, sc);

				if (project.finalised) {
					project.finalisationDate = project.getFinalisationDate(userInput, sc);
					project.updateTwoFieldsForProject(statement, false, "finalised", "true", true, "date_complete",
							project.finalisationDate, project.iD);
				} else {
					project.updateTwoFieldsForProject(statement, false, "finalised", "false", false, "date_complete",
							"NULL", project.iD);
				}

				// Update the project date finalised
			} else if (userInput.equals("7")) {
				// Impossible to edit without finalised being true
				project.finalised = project.getFinalisationStatusFromDataBase(results, statement, project.iD);

				if (project.finalised) {
					project.finalisationDate = project.getFinalisationDate(userInput, sc);
					project.updateFieldForProject(statement, true, "date_complete", project.finalisationDate,
							project.iD);
				} else {
					System.out.println("The project needs to be finalised before a date is added.");
					System.out.println();
				}

				// Update project address
			} else if (userInput.equals("8")) {
				// either change address or edit this one
				prompt.editEntityOrIdPromptTwoOptions("Project Address");
				userInput = prompt.getUserChoiceTwoOptions(userInput, sc);

				if (userInput.equals("1")) {
					// Update current address
					project.addressId = project.getIdForOtherTable(results, statement, project.iD, "proj_address_id");
					projectAddressHandler.displayProjectAddressInfo(results, statement, project.addressId);
					userInput = prompt.getUserChoiceThreeOptions(userInput, sc);

					projectAddressHandler.updateProjectAddressField(userInput, projectAddressHandler, project,
							statement, sc);

					// Change id of project address
				} else if (userInput.equals("2")) {
					project.addressId = projectAddressHandler.getProjectStreetAddressId(userInput, sc, results,
							statement);
					project.updateFieldForProject(statement, false, "proj_address_id", project.addressId, project.iD);
				}

				// Update architect for project
			} else if (userInput.equals("9")) {

				// Either change architect or edit this one
				prompt.editEntityOrIdPromptThreeOptions("Architect");
				userInput = prompt.getUserChoiceThreeOptions(userInput, sc);
				if (userInput.equals("1")) {
					// present architect info
					architect.iD = project.getIdForOtherTable(results, statement, project.iD, "architect_id");

					if (architect.iD != null) {
						personHandler.updatePersonField(userInput, "architect", personHandler, architect, sc, results,
								statement);
					} else {
						System.out.println("You cannot update a null value.");
						System.out.println("");
					}

					// If email or phone is chosen, check the other is not null
				} else if (userInput.equals("2")) {
					architect.iD = personHandler.getPersonId(sc, results, statement, "architect", userInput);
					project.updateFieldForProject(statement, false, "architect_id", architect.iD, project.iD);
					// NULL value recording
				} else if (userInput.equals("3")) {
					project.updateFieldForProject(statement, false, "architect_id", "NULL", project.iD);
				}
				// Update contractor
			} else if (userInput.equals("10")) {
				// Either change contractor or edit this one
				prompt.editEntityOrIdPromptThreeOptions("Contractor");
				userInput = prompt.getUserChoiceThreeOptions(userInput, sc);
				if (userInput.equals("1")) {
					// Present info
					contractor.iD = project.getIdForOtherTable(results, statement, project.iD, "contractor_id");

					if (contractor.iD != null) {
						personHandler.presentPersonInformation(results, statement, "contractor", contractor.iD);
						userInput = prompt.getUserChoiceSixOptions(userInput, sc);
						// picks one to change

						personHandler.updatePersonField(userInput, "contractor", personHandler, contractor, sc, results,
								statement);
					} else {
						System.out.println("You cannot update a null value.");
						System.out.println("");
					}
					// If email or phone, check the other is not null

					// store new info

				} else if (userInput.equals("2")) {
					contractor.iD = personHandler.getPersonId(sc, results, statement, "contractor", userInput);
					project.updateFieldForProject(statement, false, "contractor_id", contractor.iD, project.iD);

				} else if (userInput.equals("3")) {
					project.updateFieldForProject(statement, false, "contractor_id", "NULL", project.iD);
				}

				// Update customer
			} else if (userInput.equals("11")) {
				// Either changer customer or edit this one
				prompt.editEntityOrIdPromptTwoOptions("Customer");
				userInput = prompt.getUserChoiceTwoOptions(userInput, sc);

				if (userInput.equals("1")) {
					// Update current
					customer.iD = project.getIdForOtherTable(results, statement, project.iD, "customer_id");
					personHandler.presentPersonInformation(results, statement, "customer", customer.iD);
					userInput = prompt.getUserChoiceSixOptions(userInput, sc);

					personHandler.updatePersonField(userInput, "customer", personHandler, customer, sc, results,
							statement);
					// If email or phone, check the other is not null

				} else if (userInput.equals("2")) {
					// Change id
					customer.iD = personHandler.getPersonId(sc, results, statement, "customer", userInput);
					project.updateFieldForProject(statement, false, "customer_id", customer.iD, project.iD);
				}
			}
		}
	}

	/**
	 * Method for if the user chooses to update person information on the initial
	 * prompt. It will ask the user to choose a person and then a field to update.
	 * It will then update that field.
	 * 
	 * @param sc            object to get user inputs
	 * @param userInput     user's input
	 * @param architect     architect person object
	 * @param customer      customer person object
	 * @param contractor    contractor person object
	 * @param results       object which stores queried data
	 * @param statement     object which queries and updates the database
	 * @param prompt        object containing prompts and checks user inputs
	 * @param personHandler object which handles person information
	 * @throws SQLException message for if an SQL error occurs
	 */
	public static void updatePersonOption(Scanner sc, String userInput, Person architect, Person customer,
			Person contractor, ResultSet results, Statement statement, Prompt prompt, PersonHandler personHandler)
			throws SQLException {
		prompt.personPrompt();
		userInput = prompt.getUserChoiceThreeOptions(userInput, sc);
		// Customer update
		if (userInput.equals("1")) {
			customer.iD = personHandler.getPersonId(sc, results, statement, "customer", userInput);
			// Condition for if there are no customers to update
			if (customer.iD.equals("0")) {
				System.out.println("");
			} else {
				personHandler.presentPersonInformation(results, statement, "customer", customer.iD);
				userInput = prompt.getUserChoiceSixOptions(userInput, sc);

				personHandler.updatePersonField(userInput, "customer", personHandler, customer, sc, results, statement);
			}

			// Architect update
		} else if (userInput.equals("2")) {

			architect.iD = personHandler.getPersonId(sc, results, statement, "architect", userInput);
			// Condition for no architects in database
			if (architect.iD.equals("0")) {
				System.out.println("");
			} else {
				personHandler.updatePersonField(userInput, "architect", personHandler, architect, sc, results,
						statement);
			}

			// Contractor update
		} else if (userInput.equals("3")) {

			contractor.iD = personHandler.getPersonId(sc, results, statement, "contractor", userInput);

			// Condition for no contractors in the database
			if (contractor.iD.equals("0")) {
				System.out.println("");
			} else {
				personHandler.presentPersonInformation(results, statement, "contractor", contractor.iD);
				userInput = prompt.getUserChoiceSixOptions(userInput, sc);
				// picks one to change

				personHandler.updatePersonField(userInput, "contractor", personHandler, contractor, sc, results,
						statement);
			}
		}
	}

	/**
	 * Method for if the user chooses to update a project address. It will ask the
	 * user to choose a project address, then a field and for the user to update it.
	 * 
	 * @param prompt                object containing prompts and checks user inputs
	 * @param statement             object which queries and updates the database
	 * @param results               object which stores queried data
	 * @param userInput             user's input
	 * @param sc                    object to get user inputs
	 * @param project               project object
	 * @param projectAddressHandler object which handles project address information
	 * @throws SQLException message for if an SQL error occurs
	 */
	public static void updateProjectAddressOption(Prompt prompt, Statement statement, ResultSet results,
			String userInput, Scanner sc, Project project, ProjectAddressHandler projectAddressHandler)
			throws SQLException {
		project.addressId = projectAddressHandler.getProjectStreetAddressId(userInput, sc, results, statement);
		// Condition for no project addresses in the database
		if (project.addressId.equals("0")) {
			System.out.println("");
		} else {
			projectAddressHandler.displayProjectAddressInfo(results, statement, project.addressId);
			userInput = prompt.getUserChoiceThreeOptions(userInput, sc);

			projectAddressHandler.updateProjectAddressField(userInput, projectAddressHandler, project, statement, sc);
		}
	}

	/**
	 * Method for if the user chooses to search for a project. It will ask the user
	 * if they want search by project number or name. Then it will present the user
	 * with the projects. The user will be prompted to enter the name or project
	 * number of the project they want, then the information for that project will
	 * be displayed.
	 * 
	 * @param project   project object
	 * @param userInput user's input
	 * @param sc        object to get user inputs
	 * @param results   object which stores queried data
	 * @param prompt    object containing prompts and checks user inputs
	 * @param statement object which queries and updates the database
	 * @throws SQLException message for if an SQL error occurs
	 */
	public static void searchProjectOption(Project project, String userInput, Scanner sc, ResultSet results,
			Prompt prompt, Statement statement) throws SQLException {
		prompt.nameProjNumPrompt();
		userInput = prompt.getUserChoiceTwoOptions(userInput, sc);

		// Search by name
		if (userInput.equals("1")) {

			if (project.displayProjects(results, statement)) {
				project.name = project.getProjectNameFromDB(userInput, sc, results, statement);
				project.displayProjectName(results, statement, project.name);
			} else {
				System.out.println("There are no projects to search");
			}

			// Search by proj_num
		} else if (userInput.equals("2")) {

			if (project.displayProjects(results, statement)) {
				project.iD = project.getProjectId(userInput, sc, results, statement);
				project.displayProjectId(results, statement, project.iD);
			} else {
				System.out.println("There are no projects to search");
			}

		}
	}

	/**
	 * Method for if the user chooses to search for a person on the initial prompt.
	 * The user will be will asked what kind of person they want to search for.
	 * After chooses a type of person, the user will be shown all the people of that
	 * type stored and be prompted to enter an ID. Once they have done so, that
	 * person's information will be displayed.
	 * 
	 * @param sc            object to get user inputs
	 * @param userInput     user's input
	 * @param prompt        object containing prompts and checks user inputs
	 * @param personHandler object which handles person information
	 * @param customer      customer person object
	 * @param architect     architect person object
	 * @param contractor    contractor person object
	 * @param results       object which stores queried data
	 * @param statement     object which queries and updates the database
	 * @throws SQLException message for if an SQL error occurs
	 */
	public static void searchPersonOption(Scanner sc, String userInput, Prompt prompt, PersonHandler personHandler,
			Person customer, Person architect, Person contractor, ResultSet results, Statement statement)
			throws SQLException {
		prompt.personPrompt();
		userInput = prompt.getUserChoiceThreeOptions(userInput, sc);

		// Customer search
		if (userInput.equals("1")) {
			customer.iD = personHandler.getPersonId(sc, results, statement, "customer", userInput);
			// Condition for no customers in the database
			if (customer.iD.equals("0")) {
				System.out.println("");
			} else {
				personHandler.presentPersonInformation(results, statement, "customer", customer.iD);
			}

			// Architect search
		} else if (userInput.equals("2")) {
			architect.iD = personHandler.getPersonId(sc, results, statement, "architect", userInput);
			// Condition for no architects in the database
			if (architect.iD.equals("0")) {
				System.out.println("");
			} else {
				personHandler.presentPersonInformation(results, statement, "architect", architect.iD);
			}

			// Contractor search
		} else if (userInput.equals("3")) {
			contractor.iD = personHandler.getPersonId(sc, results, statement, "contractor", userInput);
			// Condition for no contractors in the database
			if (contractor.iD.equals("0")) {
				System.out.println("");
			} else {
				personHandler.presentPersonInformation(results, statement, "contractor", contractor.iD);
			}

		}
	}

	/**
	 * Method for if the user chooses to delete a project at the initial prompt. The
	 * user will be shown all the project and be asked to enter the project number
	 * of one they want to delete. After doing so, the user will be asked if they
	 * are sure they want to delete this project. If yes, it will be deleted, if no,
	 * it will not.
	 * 
	 * @param project   project object
	 * @param results   object which stores queried data
	 * @param statement object which queries and updates the database
	 * @param userInput user's input
	 * @param sc        object to get user inputs
	 * @param prompt    object containing prompts and checks user inputs
	 * @throws SQLException message for if an SQL error occurs
	 */
	public static void deleteProjectOption(Project project, ResultSet results, Statement statement, String userInput,
			Scanner sc, Prompt prompt) throws SQLException {
		if (project.displayProjects(results, statement)) {
			project.iD = project.getProjectId(userInput, sc, results, statement);
			project.displayProjectId(results, statement, project.iD);
			prompt.areYouSureDeletePrompt("Project");
			userInput = prompt.getUserChoiceTwoOptions(userInput, sc);

			if (userInput.equals("1")) {
				// delete
				project.deleteProject(statement, project.iD);
			} else if (userInput.equals("2")) {
				// don't delete
				System.out.println("The project was not deleted.");
				System.out.println("");
			}

		} else {
			System.out.println("There are no projects to delete.");
		}
	}

	/**
	 * Method for if the user chooses to delete a person from the database. The
	 * person will first be prompted to choose a person type, they will then be
	 * shown all the people of that type and be prompted to enter the ID of the one
	 * to delete. If the person is associated with any projects, the user will be
	 * shown which projects they are associated with and be told they are not
	 * allowed to delete the person as they are associated with one or more
	 * projects. After doing so, the user will be asked if they are sure they want
	 * to delete this person. If yes, it will be deleted, if no, it will not.
	 * 
	 * @param prompt        object containing prompts and checks user inputs
	 * @param userInput     user's input
	 * @param sc            object to get user inputs
	 * @param results       object which stores queried data
	 * @param customer      customer person object
	 * @param architect     architect person object
	 * @param contractor    contractor person object
	 * @param project       project object
	 * @param statement     object which queries and updates the database
	 * @param personHandler object which handles person object information
	 * @throws SQLException message for if an SQL error occurs
	 */
	public static void deletePersonPromptOption(Prompt prompt, String userInput, Scanner sc, ResultSet results,
			Person customer, Person architect, Person contractor, Project project, Statement statement,
			PersonHandler personHandler) throws SQLException {
		prompt.personPrompt();
		userInput = prompt.getUserChoiceThreeOptions(userInput, sc);

		// customer
		if (userInput.equals("1")) {

			customer.iD = personHandler.getPersonId(sc, results, statement, "customer", userInput);

			personHandler.deletePerson(results, statement, userInput, customer, personHandler, sc, "customer", project,
					prompt);

			// architect
		} else if (userInput.equals("2")) {
			architect.iD = personHandler.getPersonId(sc, results, statement, "architect", userInput);
			personHandler.deletePerson(results, statement, userInput, customer, personHandler, sc, "customer", project,
					prompt);

			// Contractor
		} else if (userInput.equals("3")) {
			contractor.iD = personHandler.getPersonId(sc, results, statement, "contractor", userInput);
			personHandler.deletePerson(results, statement, userInput, customer, personHandler, sc, "customer", project,
					prompt);

		}
	}

	/**
	 * Method for if the user chooses to delete a project address from the initial
	 * prompt. The user will be shown a list of the project address in the database
	 * and be prompted to choose one based on ID. After doing so, if the project
	 * address is associated with a project, the user will be told they cannot
	 * delete the project address as it is associated with one or more projects.
	 * They will be show a list of projects that the address is associated with. If
	 * the address is not associated with any projects, the user will be asked if
	 * they are sure they want to delete the address. If yes, it will be deleted, if
	 * no, it will not.
	 * 
	 * @param project               project object
	 * @param projectAddressHandler object which handles project address information
	 * @param userInput             user's input
	 * @param sc                    object to get user inputs
	 * @param results               object which stores queried data
	 * @param statement             object which queries and updates the database
	 * @param prompt                object containing prompts and checks user inputs
	 * @throws SQLException message for if an SQL error occurs
	 */
	public static void deleteProjectAddressOption(Project project, ProjectAddressHandler projectAddressHandler,
			String userInput, Scanner sc, ResultSet results, Statement statement, Prompt prompt) throws SQLException {
		project.addressId = projectAddressHandler.getProjectStreetAddressId(userInput, sc, results, statement);
		// Condition for if there are no project addresses in the database
		if (project.addressId.equals("0")) {
			System.out.println("");
		} else {
			projectAddressHandler.deleteProjectAddress(results, statement, project, projectAddressHandler, prompt,
					userInput, sc);
		}
	}
}
