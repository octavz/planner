var args = arguments[0] || {};

Ti.API.log("Project home");

var projectId = args.projectId;
var project = Alloy.Collections.Project.get(projectId);

if (project == null) {
	alert('not found');
	$.win.close();
} else {
	Ti.API.log(project);
	$.win.title = project.attributes["name"] + " Home";
}

function OpenEditProject(e) {
	Ti.API.log("projectId", projectId);
	Alloy.createController('ProjectEdit', {
		projectId : projectId
	}).getView().open();
}

function OpenDeleteProject(e) {
	Ti.API.log("projectId", projectId);
	Alloy.createController('ProjectEdit', {
		projectId : projectId
	}).getView().open();
}
