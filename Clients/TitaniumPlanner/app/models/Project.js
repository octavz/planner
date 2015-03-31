exports.definition = {
	config : {
		columns : {
			"name" : "TEXT",
		},
		URL : Alloy.CFG.App.ApiUrl + '/project/',
		adapter : {
			type : "restapi",
			collection_name : "Projects",
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
