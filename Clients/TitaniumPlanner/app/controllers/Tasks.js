var args = arguments[0] || {};

$.btnInsert.addEventListener('click', function(e) {
	//Hides the keyboard in iOS
	$.tfInsert.blur();

	//if there is something in the textbox
	if ($.tfInsert.value != "") {
		//This is how we are creating an instance of a model
		var task = Alloy.createModel("Task", {
			//with out custom parameters
			title : $.tfInsert.value,
		});

		//This is how we save a model to our databaseif the model already exists, the save will be an "update".
		task.save();

		//Resets the model's state from the database
		Alloy.Collections.Task.fetch();
	} else {
		alert("Please fill out the text field above!");
	}
});

//if we do a longpress we can delete the row
$.tbTasks.addEventListener('longpress', function(e) {
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
	var recoverDatabase = Alloy.createCollection("Task");
	recoverDatabase.fetch({
		query : "SELECT * FROM Tasks"
	});

	for (var i = 0; i < recoverDatabase.length; i++) {
		if (recoverDatabase.at(i).get("id_task") == _row.rowId) {
			var table = Alloy.createCollection("Task");
			table.fetch({
				query : "SELECT * FROM Tasks where id_task = " + _row.rowId
			});
			if (table.length > 0) {
				//To remove a row from the database we use destroy()
				table.at(0).destroy();
				Alloy.Collections.Task.fetch();
			}
		}
	}
}

Alloy.Collections.Task.fetch();
