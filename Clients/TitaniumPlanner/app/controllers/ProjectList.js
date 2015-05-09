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
	Alloy.Globals.UserToken = undefined;
	Alloy.Globals.UserId = undefined;
	Alloy.createController('login').getView().open();
	Ti.UI.currentWindow.close();
	$.win = null;
}

// use action bar search view
var search = Alloy.createController("ProjectSearchView").getView();
$.lvProjects.searchView = search;

$.win.addEventListener("open", function() {

	$.win.activity.onCreateOptionsMenu = function(e) {
		
		e.menu.add({
			title : "Table Search",
			icon : Ti.Android.R.drawable.ic_menu_search,
			actionView : search,
			showAsAction : Ti.Android.SHOW_AS_ACTION_ALWAYS
		});

		e.menu.add({
			title : "Add",
			icon : Ti.Android.R.drawable.ic_menu_add,
			showAsAction : Titanium.Android.SHOW_AS_ACTION_IF_ROOM,
		}).addEventListener("click", OpenAddProject);

		e.menu.add({
			title : "My Settings",
			icon : Ti.Android.R.drawable.ic_menu_add,
			showAsAction : Titanium.Android.SHOW_AS_ACTION_IF_ROOM,
		}).addEventListener("click", OpenMySettings);

		e.menu.add({
			title : "Logout",
			icon : Ti.Android.R.drawable.ic_menu_add,
			showAsAction : Titanium.Android.SHOW_AS_ACTION_NEVER,
		}).addEventListener("click", DoLogout);

	};

	$.win.activity.invalidateOptionsMenu();
});

Alloy.Collections.Project.fetch();
