exports.definition = {
	config: {
		columns: {
		    "userName": "string",
		    "userPassword": "string"
		},
		adapter: {
			type: "sql",
			collection_name: "loginData"
		}
	},
	extendModel: function(Model) {
		_.extend(Model.prototype, {
			// extended functions and properties go here
		});

		return Model;
	},
	extendCollection: function(Collection) {
		_.extend(Collection.prototype, {
			// extended functions and properties go here
		});

		return Collection;
	}
};