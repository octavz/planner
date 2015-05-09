var args = arguments[0] || {};

var projectId = args.projectId;
var project = Alloy.Collections.Project.get(projectId);

if (project == null) {
	alert('not found');
	$.win.close();
} else {
	Ti.API.log(project);
	$.win.title = "Edit: " + project.attributes["name"];
	//TODO: Shouldn't we use binding?
	$.tfProjectName.value = project.attributes["name"];
	$.tfProjectDesc.value = project.attributes["desc"];
}

function SaveProject(e) {
	Ti.API.log("info", "[Info] Save project with Id: " + projectId.toString());
	
	project.fetch();
	Ti.API.log("info", "[Info] Name: " + project.name);
	//TODO: Is there a way of automate validation?
	if(validate() === true){	 
		project.save();
	}else{
		alert('Please fill all fields!');
	}
}

function validate(){
	var isValid = true;
	
	if($.tfProjectName.value.length == 0){
		isValid = false;
	}
	
	if($.tfProjectDesc.value.legnth == 0){
		isValid = false;
	}
	
	return isValid;
}

$.win.addEventListener("open", function() {

	$.win.activity.onCreateOptionsMenu = function(e) {
		
		e.menu.add({
			title : "Save",
			icon : Ti.Android.R.drawable.ic_menu_save,
			showAsAction : Titanium.Android.SHOW_AS_ACTION_IF_ROOM,
		}).addEventListener("click", SaveProject);
	};

	$.win.activity.invalidateOptionsMenu();
});

$.win.addEventListener("close", function(){
    $.destroy();
});