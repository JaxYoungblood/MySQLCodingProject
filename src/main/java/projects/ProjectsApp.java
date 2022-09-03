package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

public class ProjectsApp {
	private Scanner scanner = new Scanner(System.in);
	private ProjectService projectService = new ProjectService();
	private Project curProject;

	// @formatter:off
		private List<String> operations = List.of(
				"1) Add a Project",
				"2) List Projects",
				"3) Select a project"
		);//end OPERATIONS LIST
		// @formatter:on

	public static void main(String[] args) {
		new ProjectsApp().processUserSelections();

	}// end MAIN method

	private void processUserSelections() {
		boolean done = false;

		while(!done) {
			try {
				int selection = getUserSelection();

				
			switch(selection) {
				case -1:
					done = exitMenu();
					break;
				case 1:
					createProject();
					break;
				case 2:
					listProjects();
					break;
				case 3:
					selectProject();
					break;
				default:
					System.out.println("\n" + selection + " is not a valid selection. Try again!");
					break;
			}//end SWITCH 
 			} // end TRY

			catch(Exception e) {
				System.out.println("\nError: " + e + " Try again!");
			} // end CATCH
		} // end WHILE
	}// end METHOD ProcessUserSelections 

	
	private void selectProject() {
		listProjects();
		Integer projectId = getIntInput("Enter a project ID to select a project");
		curProject = null;
		
		curProject = projectService.fetchProjectById(projectId);
//possibly take out below!		
		if(Objects.isNull(curProject)) {
			System.out.println("\nInvalid projectID selected");
		}//end IF
	}//end METHOD selectProject

	private void listProjects() {
		List<Project> projects = projectService.fetchAllProjects();
		System.out.println("\nProjects:");
		
		projects.forEach(project -> System.out.println("    " + project.getProjectId() + ": " + project.getProjectName()));
	}//end METHOD listProjects

	private void createProject() {
		String projectName = getStringInput("Enter the project name");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours");
		Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
		String notes = getStringInput("Enter the project notes");
		
		Project project = new Project();
		
		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);
		
		Project dbProject = projectService.addProject(project);
		System.out.println(" ");
		System.out.println("~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~"); //fun stuff
		System.out.println("You have successfully created project: " + dbProject);

	}//end METHOD createProject


	private boolean exitMenu() {
		System.out.println(",_,_,_,_,_,_,_,_,_,_,"); //just for fun to make things 'cleaner'
		System.out.println("| Exiting the menu! |");
		System.out.println("'-'-'-'-'-'-'-'-'-'-'");
		return true;
	}//end METHOD exitMenu
	
	
	private int getUserSelection() {
		printOperations();
		
		Integer input = getIntInput("Enter a menu selection");
		
		return Objects.isNull(input) ? -1 : input;
	}//end METHOD GetUserSelection 

	
	private BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput(prompt);
		
		if(Objects.isNull(input)) {
			return null;
		}//end IF
		
		try {
			return new BigDecimal(input).setScale(2);
		}//end TRY
		catch(NumberFormatException e) {
			throw new DbException(input + " is not a valid decimal number, try again!");
		}//end CATCH	
	}//end METHOD GetDecimalInput
	
	
	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);
		
		if(Objects.isNull(input)) {
			return null;
		}//end IF
		
		try {
			return Integer.valueOf(input);
		}//end TRY
		catch(NumberFormatException e) {
			throw new DbException(input + " is not a valid number, try again!");
		}//end CATCH
	}//end METHOD GetIntInput 

	
	private String getStringInput(String prompt) {
		System.out.print(prompt + ": ");
		String input = scanner.nextLine();
		return input.isBlank() ? null : input.trim();
	}//end METHOD GetStringInput

	
	private void printOperations() {
		System.out.println("\nThese are the available selections. Press the ENTER key to quit:");
		operations.forEach(line -> System.out.println("   " + line));
		
		if(Objects.isNull(curProject)) {
			System.out.println("\nYou are not working with a project.");
		}//end IF
		else {
			System.out.println("\nYou are working with project: " + curProject);
		}//end ELSE
	}//end METHOD PrintOperations 

	
}// end PUBLIC CLASS
