var Alloy = require("alloy"), _ = require("alloy/underscore")._, model, collection;

exports.definition = {
    config: {
        columns: {
            id_task: "INTEGER PRIMARY KEY AUTOINCREMENT",
            title: "TEXT"
        },
        adapter: {
            type: "sql",
            collection_name: "Tasks"
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

model = Alloy.M("Task", exports.definition, []);

collection = Alloy.C("Task", exports.definition, model);

exports.Model = model;

exports.Collection = collection;