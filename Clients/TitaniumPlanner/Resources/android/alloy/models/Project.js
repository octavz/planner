var Alloy = require("alloy"), _ = require("alloy/underscore")._, model, collection;

exports.definition = {
    config: {
        columns: {
            name: "TEXT"
        },
        URL: Alloy.CFG.App.ApiUrl + "/project/",
        adapter: {
            type: "restapi",
            collection_name: "Projects",
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

model = Alloy.M("Project", exports.definition, []);

collection = Alloy.C("Project", exports.definition, model);

exports.Model = model;

exports.Collection = collection;