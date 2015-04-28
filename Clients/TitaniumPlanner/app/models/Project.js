exports.definition = {
	config : {
		columns : {
			"id" : "TEXT",
			"name" : "TEXT",
			"desc" : "TEXT",
			"parent" : "",
			//todo cip which is the type here?
			"public" : "TEXT",
			//todo cip whichj is the type here?
			"perm" : "TEXT"

		},
		URL : Alloy.CFG.App.ApiUrl + '/project/',
		adapter : {
			type : Alloy.CFG.App.UseFakeData ? "sql" : "restapi",
			collection_name : "Projects",
			idAttribute : "id",
		},
		debug : true,
	},
	extendModel : function(Model) {
		_.extend(Model.prototype, {
			// extended functions and properties go here
		});

		return Model;
	},
	extendCollection : function(Collection) {
		_.extend(Collection.prototype, {
			// extended functions and properties go here
		});

		return Collection;
	}
};
