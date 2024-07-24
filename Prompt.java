import java.util.Scanner;

/**
 * The prompt class contains all the prompts for the user that are called on
 * repeatedly in the program, it also contains methods to get user decisions.
 */
public class Prompt {

	// First prompt the user receives
	/**
	 * This is the initial prompt that is displayed when the user opens the program.
	 * It contains all the operations they may want the program to do including
	 * closing it.
	 */
	public void initialPrompt() {
		System.out.println("What would you like to do?:");
		System.out.println("1. Enter a new project");
		System.out.println("2. Enter a new customer, architect, or contractor");
		System.out.println("3. Update a project");
		System.out.println("4. Update a customer, architect, or contractor");
		System.out.println("5. Update a project address");
		System.out.println("6. Search for a project");
		System.out.println("7. Search for a customer, architect, or contractor");
		System.out.println("8. Show all unfinished projects");
		System.out.println("9. Show all overdue projects");
		System.out.println("10. Delete a project");
		System.out.println("11. Delete a person");
		System.out.println("12. Delete a project address");
		System.out.println("0. Close the program");
		System.out.println("Enter a number: ");
		System.out.println("");
	}

	/**
	 * This method prompts the user to state whether they are sure they want to
	 * delete an entity from a table with the options yes and no where 1 is yes and
	 * 2 is no.
	 * 
	 * @param entity name of entity being deleted
	 */
	public void areYouSureDeletePrompt(String entity) {

		System.out.println("Are you sure you want to delete this " + entity + "?");
		System.out.println("1. Yes");
		System.out.println("2. No");
		System.out.println("");
	}

	/**
	 * This prompts the user to choose between a customer, architect or contractor
	 * It is used when the user wants to delete, update or enter a new person to the
	 * database. 1 is customer, 2 is architect and 3 is contractor.
	 */
	// Prompt for the type of person the user wants
	public void personPrompt() {
		System.out.println("Customer, Architect, or Contractor");
		System.out.println("1. Customer");
		System.out.println("2. Architect");
		System.out.println("3. Contractor");
		System.out.println("Enter a number: ");
		System.out.println("");
	}

	/**
	 * This method prompts the user to choose between searching for a project based
	 * on project number or name
	 */
	// Prompt for the search method
	public void nameProjNumPrompt() {
		System.out.println("Do you want to search by name or project number?");
		System.out.println("1. Name");
		System.out.println("2. Project number");
		System.out.println("Enter a number: ");
		System.out.println("");
	}

	/**
	 * This method prompts the user to choose between editing an entity or choosing
	 * from another in the database. This is used when editing a project's customer
	 * or project address as they may not be null.
	 * 
	 * @param entityBeingAskedAbout Entity to be edited or switched
	 */
	public void editEntityOrIdPromptTwoOptions(String entityBeingAskedAbout) {
		System.out.println(
				"Would you like to edit this " + entityBeingAskedAbout + " or swap with one from the database?");
		System.out.println("1. Edit this one");
		System.out.println("2. Choose one from the database");
		System.out.println("Enter a number: ");
		System.out.println("");
	}

	/**
	 * This method prompts the used to choose between editing an entity, choosing
	 * from another in the database, or setting the id to null. This is used for
	 * architects and contractors as they may be null values (Maybe an architect
	 * left for some reason).
	 * 
	 * @param entityBeingAskedAbout Entity to be edited, switched, or set to null
	 */
	// Prompt to edit entity or choose a different one, or leave blank 3 options
	public void editEntityOrIdPromptThreeOptions(String entityBeingAskedAbout) {
		System.out
				.println("Would you like to edit a/an " + entityBeingAskedAbout + " or choose one from the database?");
		System.out.println("1. Edit this one");
		System.out.println("2. Choose one from the database");
		System.out.println("3. Replace with NULL value");
		System.out.println("Enter a number: ");
		System.out.println("");

	}

	/**
	 * Method prompts the user to either enter a new entity or select an entity.
	 * This is used when the user creates a project and they must fill in either a
	 * new entity such as a project address or customer, or they must enter a new
	 * one.
	 * 
	 * @param entityBeingAskedAbout Entity to be entered and added to the project
	 */
	// Prompt the user to choose whether to enter a new entity
	// select one from the database
	public void newEntityOrIdPromptTwoOptions(String entityBeingAskedAbout) {

		System.out.println(
				"Would you like to enter a new " + entityBeingAskedAbout + " or choose one from the database?");
		System.out.println("1. Enter a new one");
		System.out.println("2. Choose one from the database");
		System.out.println("Enter a number: ");
		System.out.println("");
	}

	/**
	 * Method prompts the user to enter a new entity, select an ID, or leave the
	 * field null. This is used when creating a project and an architect or
	 * contractor is to be entered. They can be null as at the time of recording the
	 * data, a contractor or architect might not be chosen yet
	 * 
	 * @param entityBeingAskedAbout Entity to be entered or left null on the project
	 */
	// Prompt the user to choose whether
	public void newEntityOrIdPromptThreeOptions(String entityBeingAskedAbout) {

		System.out.println(
				"Would you like to enter a new " + entityBeingAskedAbout + " or choose one from the database?");
		System.out.println("1. Enter a new one");
		System.out.println("2. Choose one from the database");
		System.out.println("3. Do not enter (if this option is chosen, a null value will be recorded as the "
				+ entityBeingAskedAbout + "ID)");
		System.out.println("Enter a number: ");
		System.out.println("");
	}

	/**
	 * Method which gets a user's response to a prompt where there are three options
	 * for them to choose from.
	 * 
	 * @param userInput The user's input variable
	 * @param scObj     The scanner to get the user's inputs
	 * @return The user's choice as a String
	 */
	// Get a 1, 2 or 3 from the user
	// Method to choose which person-type the user wants to interact with
	public String getUserChoiceThreeOptions(String userInput, Scanner scObj) {

		// Create variables
		boolean gettingInput = true;

		// Loop to control for bad inputs
		while (gettingInput) {

			userInput = scObj.nextLine().strip();

			switch (userInput) {

			// Customer
			case "1":
				gettingInput = false;
				break;

			// Architect
			case "2":
				gettingInput = false;
				break;

			// Contractor
			case "3":
				gettingInput = false;
				break;

			default:
				System.out.println("Please enter 1, 2, or 3");
			}
		}
		return userInput;
	}

	/**
	 * Method which gets a user's response to a prompt where there are two options
	 * for them to choose from.
	 * 
	 * @param userInput The user's input variable
	 * @param scObj     The scanner to get the user's inputs
	 * @return The user's choice as a String
	 */
	// Method to get a 1 or 2 from the user
	// Method to choose between searching by name or project number
	public String getUserChoiceTwoOptions(String userInput, Scanner scObj) {

		// Create variables
		boolean gettingInput = true;

		// Loop to control for bad inputs
		while (gettingInput) {

			userInput = scObj.nextLine().strip();

			switch (userInput) {

			case "1":
				gettingInput = false;
				break;

			case "2":
				gettingInput = false;
				break;

			default:
				System.out.println("Please enter 1 or 2");
			}
		}
		return userInput;
	}

	/**
	 * Method which gets a user's response to a prompt where there are six options
	 * for them to choose from.
	 * 
	 * @param userInput The user's input variable
	 * @param scObj     The scanner to get the user's inputs
	 * @return The user's choice as a String
	 */
	// Method to get user choice 6 options
	public String getUserChoiceSixOptions(String userInput, Scanner scObj) {

		// Create variables
		boolean gettingInput = true;

		// Loop to control for bad inputs
		while (gettingInput) {

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

			default:
				System.out.println("Please enter 1-6");
			}
		}
		return userInput;
	}
}