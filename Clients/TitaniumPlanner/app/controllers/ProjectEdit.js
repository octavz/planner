var args = arguments[0] || {};

var projectId = args.projectId;
var project = Alloy.Collections.Project.get(projectId);

if (project == null) {
	alert('not found');
	$.win.close();
} else {
	Ti.API.log(project);
	$.win.title = "Edit: " + project.attributes["name"];
}

