exports.definition = {
	config : {
		columns : {
			"title" : "TEXT",
		},
		URL : Alloy.CFG.App.ApiUrl + '/task/',
		adapter : {
			type : "restapi",
			collection_name : "Tasks",
			idAttribute : "id",
		},
		// debug:true,
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
