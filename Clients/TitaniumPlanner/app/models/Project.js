exports.definition = {
	config : {
		columns : {
			// "id" : "TEXT",
			"id": "INTEGER PRIMARY KEY AUTOINCREMENT",
			"name" : "TEXT",
			"desc" : "TEXT",
			"parent" : "",
			//todo cip which is the type here?
			"public" : "TEXT",
			//todo cip whichj is the type here?
			"perm" : "TEXT"

		},
		adapter : {
			type : Alloy.CFG.App.UseFakeData ? "sql" : "restapi",
			collection_name : "Projects",
			idAttribute : "id",
		},
		headers : {// your custom headers
			"Authorization" : function() {
				return "OAuth " + Alloy.Globals.UserToken;
			},
			"Content-Type" : "application/json"
		},
		parentNode : "items", //your root node
		debug : true,
	},
	extendModel : function(Model) {
		_.extend(Model.prototype, {
			// extended functions and properties go here
			url : function() {
				return Alloy.CFG.App.ApiUrl + '/project';
			},
		});

		return Model;
	},
	extendCollection : function(Collection) {
		_.extend(Collection.prototype, {
			url : function() {
				//todo cip count and offset should not be here!
				return Alloy.CFG.App.ApiUrl + '/user/' + Alloy.Globals.UserId + '/projects?offset=0&count=100';
			},
		});

		return Collection;
	}
};
