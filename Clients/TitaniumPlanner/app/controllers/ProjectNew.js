var args = arguments[0] || {};

$.btnInsert.addEventListener('click', function(e) {
	//Hides the keyboard in iOS
	$.tfInsert.blur();

	//if there is something in the textbox
	if ($.tfInsert.value != "") {
		//This is how we are creating an instance of a model
		var project = Alloy.createModel("Project", {
			//with out custom parameters
			name : $.tfInsert.value,
			public : true
		});

		//This is how we save a model to our databaseif the model already exists, the save will be an "update".
		project.save();

		//Resets the model's state from the database
		Alloy.Collections.Project.fetch();
	} else {
		alert("Please fill out the text field above!");
	}
});

