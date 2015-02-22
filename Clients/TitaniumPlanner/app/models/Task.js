exports.definition = {
	config : {
		columns : {
			"id_task" : "INTEGER PRIMARY KEY AUTOINCREMENT",
			"title" : "TEXT",
		},
		adapter : {
			type : "sql",
			collection_name : "Tasks"//(cip)note: this name is used only in storage
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