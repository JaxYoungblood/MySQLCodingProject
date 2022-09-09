package projects.service;

import java.util.List;


import java.util.NoSuchElementException;
import projects.dao.ProjectDao;
import projects.entity.Project;
import projects.exception.DbException;

public class ProjectService {
	private ProjectDao projectDao = new ProjectDao();
	

	public Project addProject(Project project) {
		return projectDao.insertProject(project);
	}//end METHOD addProject


	public List<Project> fetchAllProjects() {
		return projectDao.fetchAllProjects();
	}//end METHOD fetchAllProjects


	public Project fetchProjectById(Integer projectId) {
		return projectDao.fetchProjectById(projectId).orElseThrow(() -> new NoSuchElementException("Project ID=" + projectId + " does not exist!"));
	}//end METHOD fetchProjectById


	public void modifyProjectDetails(Project project) {
		if(!projectDao.modifyProjectDetails(project)) {
			throw new DbException("Project ID=" + project.getProjectId() + " does not exist!");
		}//end IF
	}//end METHOD modifyProjectDetails


	public void deleteProject(Integer projectId) {
		if(!projectDao.deleteProject(projectId)) {
			throw new DbException("Project ID=" + projectId + " does not exist!");
		}//end IF
	}//end METHOD deleteProject

}//end CLASS
