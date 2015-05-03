var args = arguments[0] || {};


//if we do a longpress we can delete the row
$.tbProjects.addEventListener('longpress', function(e) {
	var tableViewEvent = e.row;
	var alertDialog = Titanium.UI.createAlertDialog({
		title : 'Remove',
		message : 'Do you want to remove this row?',
		buttonNames : ['Yes', 'No'],
		cancel : 1
	});

	alertDialog.show();

	alertDialog.addEventListener('click', function(e) {

		if (e.index == 0) {// clicked "YES"
			removeRow(tableViewEvent);
		} else if (e.index == 1) {// clicked "NO"
		}
	});
});

//todo cip why so many code for removal?
function removeRow(_row) {
	var recoverDatabase = Alloy.createCollection("Project");
	recoverDatabase.fetch({
		query : "SELECT * FROM Projects"
	});

	for (var i = 0; i < recoverDatabase.length; i++) {
		if (recoverDatabase.at(i).get("id_project") == _row.rowId) {
			var table = Alloy.createCollection("Project");
			table.fetch({
				query : "SELECT * FROM Projects where id_project= " + _row.rowId
			});
			if (table.length > 0) {
				//To remove a row from the database we use destroy()
				table.at(0).destroy();
				Alloy.Collections.Project.fetch();
			}
		}
	}
}

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
	var projectId = e.rowData.rowId;
	Ti.API.log(e.rowData.rowId);
	Alloy.createController('ProjectEdit', {
		projectId : projectId
	}).getView().open();
}

function DoLogout(e) {

}

Alloy.Collections.Project.fetch();
