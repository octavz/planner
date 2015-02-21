exports.definition = {
	config : {
		columns : {
			"id_project" : "INTEGER PRIMARY KEY AUTOINCREMENT",
			"name" : "TEXT",
		},
		adapter : {
			type : "sql",
			collection_name : "Projects"
		}
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
