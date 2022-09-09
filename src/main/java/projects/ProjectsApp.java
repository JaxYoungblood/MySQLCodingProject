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
				"3) Select a Project",
				"4) Update Project Details",
				"5) Delete a project"
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
				case 4:
					updateProjectDetails();
					break;
				case 5:
					deleteProject();
					break;
				default:
					System.out.println(" ");
					System.out.println("+----------E-R-R-O-R------------+");
					System.out.println("|  " + selection + " is not a valid selection!  |");
					System.out.println("+-------T-R-Y---A-G-A-I-N-------+");
					break;
			}//end SWITCH 
 			} // end TRY

			catch(Exception e) {
				System.out.println(" ");
				System.out.println("+-------------------------E-R-R-O-R---------------------------+");
				System.out.println(" " + e);
				System.out.println("+----------------------T-R-Y---A-G-A-I-N----------------------+");
				//System.out.println("\nError: " + e + " Try again!");
			} // end CATCH
		} // end WHILE
	}// end METHOD ProcessUserSelections 

	
	private void deleteProject() {
		listProjects();
		
		Integer projectId = getIntInput("Enter the ID of the project to delete");
		
		projectService.deleteProject(projectId);
		System.out.println("Project [" + projectId + "] was successfully deleted!");
		
		if(Objects.nonNull(curProject) && curProject.getProjectId().equals(projectId)) {
			curProject = null;
		}//end IF
	}//end METHOD deleteProject

	private void updateProjectDetails() {
		if(Objects.isNull(curProject)) {
			System.out.println("\n+---------------------E-R-R-O-R----------------------+");
			System.out.println("| Please select a project first, then we can update! |");
			System.out.println("+------------------T-R-Y---A-G-A-I-N-----------------+");
			return;
		}//end IF
		
		String projectName = getStringInput("Enter the project name [" + curProject.getProjectName() + "]");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours [" + curProject.getEstimatedHours() + "]");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours [" + curProject.getActualHours() + "]");
		Integer difficulty = getIntInput("Enter the project difficulty (1-5) [" + curProject.getDifficulty() + "]");
		String notes = getStringInput("Enter the project notes [" + curProject.getNotes() + "]");

		Project project = new Project();
		
		project.setProjectId(curProject.getProjectId());
		project.setProjectName(Objects.isNull(projectName) ? curProject.getProjectName() : projectName);
		project.setEstimatedHours(Objects.isNull(estimatedHours) ? curProject.getEstimatedHours() : estimatedHours);
		project.setActualHours(Objects.isNull(actualHours) ? curProject.getActualHours() : actualHours);
		project.setDifficulty(Objects.isNull(difficulty) ? curProject.getDifficulty() : difficulty);
		project.setNotes(Objects.isNull(notes) ? curProject.getNotes() : notes);

		projectService.modifyProjectDetails(project);
		
		curProject = projectService.fetchProjectById(curProject.getProjectId());
		
	}//end METHOD updateProjectDetails

	private void selectProject() {
		listProjects();
		Integer projectId = getIntInput("Enter a project ID to select a project");
		curProject = null;
		
		curProject = projectService.fetchProjectById(projectId);
//possibly take out below!		
		if(Objects.isNull(curProject)) {
			System.out.println(" ");
			System.out.println("+---------E-R-R-O-R-----------+");
			System.out.println("| Invalid Project ID Selected |");
			System.out.println("+------T-R-Y---A-G-A-I-N------+");

		}//end IF
	}//end METHOD selectProject

	private void listProjects() {
		List<Project> projects = projectService.fetchAllProjects();
		System.out.println("\nAvailable Projects:");
		
		projects.forEach(project -> System.out.println("    " + project.getProjectId() + ": " + project.getProjectName()));
	}//end METHOD listProjects

	private void createProject() {
		String projectName = getStringInput("\nEnter the project name");
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
		System.out.println("\nYou have successfully created project: " + dbProject);

	}//end METHOD createProject


	private boolean exitMenu() {
		System.out.println("\n------E-X-I-T-I-N-G------"); //just for fun to make things 'cleaner'
		System.out.println("-----T-H-E---M-E-N-U-----");
		System.out.println("------G-O-O-D-B-Y-E------");
		return true;
	}//end METHOD exitMenu
	
	
	private int getUserSelection() {
		printOperations();
		
		Integer input = getIntInput("\nEnter a menu selection");
		
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
			throw new DbException("[ " + input + " ]" + " is not a valid decimal number!");
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
			throw new DbException("[ " + input + " ]" + " is not a valid number!");
		}//end CATCH
	}//end METHOD GetIntInput 

	
	private String getStringInput(String prompt) {
		System.out.print(prompt + ": ");
		String input = scanner.nextLine();
		return input.isBlank() ? null : input.trim();
	}//end METHOD GetStringInput

	
	private void printOperations() {
		System.out.println("\n~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~"); //fun stuff
		System.out.println(" These are the available selections. Press the ENTER key to quit:");
		operations.forEach(line -> System.out.println("   " + line));
		
		if(Objects.isNull(curProject)) {
			System.out.println("\n(No project selected)");
		}//end IF
		else {
			System.out.println("\nYou are working with project: " + curProject);
		}//end ELSE
	}//end METHOD PrintOperations 

	
}// end PUBLIC CLASS
