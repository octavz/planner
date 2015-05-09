var args = arguments[0] || {};

function SaveNewProject(e) {
	Ti.API.log("info", "[Info] Save New Project!");
	
	// Hides the keyboard in iOS
	$.tfProjectName.blur();

	// if there is something in the textbox
	if ($.tfProjectName.value != "") {
		// This is how we are creating an instance of a model
		var project = Alloy.createModel("Project", {
			//with out custom parameters
			name : $.tfProjectName.value,
			public : true
		});

		// This is how we save a model to our databaseif the model already exists, the save will be an "update".
		project.save();

		// Resets the model's state from the database
		Alloy.Collections.Project.fetch();
	} else {
		alert("Please fill out the text field above!");
	}
}

$.win.addEventListener("open", function() {

	$.win.activity.onCreateOptionsMenu = function(e) {
		
		e.menu.add({
			title : "Save",
			icon : Ti.Android.R.drawable.ic_menu_save,
			showAsAction : Titanium.Android.SHOW_AS_ACTION_IF_ROOM,
		}).addEventListener("click", SaveNewProject);
	};

	$.win.activity.invalidateOptionsMenu();
});

