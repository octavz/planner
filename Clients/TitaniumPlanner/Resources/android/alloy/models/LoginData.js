var Alloy = require("alloy"), _ = require("alloy/underscore")._, model, collection;

exports.definition = {
    config: {
        columns: {
            userName: "string",
            userPassword: "string"
        },
        adapter: {
            type: "sql",
            collection_name: "loginData"
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

model = Alloy.M("loginData", exports.definition, []);

collection = Alloy.C("loginData", exports.definition, model);

exports.Model = model;

exports.Collection = collection;