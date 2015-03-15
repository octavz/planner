var Alloy = require("alloy"), _ = require("alloy/underscore")._, model, collection;

exports.definition = {
    config: {
        columns: {
            title: "TEXT"
        },
        URL: Alloy.CFG.App.ApiUrl + "/task/",
        adapter: {
            type: "restapi",
            collection_name: "Tasks",
            idAttribute: "id"
        },
        debug: true
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