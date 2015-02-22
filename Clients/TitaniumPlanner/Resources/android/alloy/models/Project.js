var Alloy = require("alloy"), _ = require("alloy/underscore")._, model, collection;

exports.definition = {
    config: {
        columns: {
            id_project: "INTEGER PRIMARY KEY AUTOINCREMENT",
            name: "TEXT"
        },
        adapter: {
            type: "sql",
            collection_name: "Projects"
        }
    },
    extendModel: function(Model) {
        _.extend(Model.prototype, {});
        return Model;
    },
    extendCollection: function(Collection) {
        _.extend(Collection.prototype, {});
        return Collection;
    }
};

model = Alloy.M("Project", exports.definition, []);

collection = Alloy.C("Project", exports.definition, model);

exports.Model = model;

exports.Collection = collection;