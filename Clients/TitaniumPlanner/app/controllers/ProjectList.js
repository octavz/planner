var args = arguments[0] || {};

var addprojectView = Alloy.createController('ProjectNew').getView();
function OpenAddProject(e) {
	Ti.API.log(e);
	addprojectView.open();
}

var mysettingsView = Alloy.createController('MySettings').getView();
function OpenMySettings(e) {
	Ti.API.log(e.item);
	mysettingsView.open();
}

function OpenEditProject(e) {
	Ti.API.log(e.itemIndex, e.sectionIndex, e.itemId);
	var projectId = e.itemId;
	Ti.API.log("projectId", projectId);
	Alloy.createController('ProjectEdit', {
		projectId : projectId
	}).getView().open();
}

function DoLogout(e) {

}

Alloy.Collections.Project.fetch();
