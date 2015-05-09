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
