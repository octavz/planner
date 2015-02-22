function __processArg(obj, key) {
    var arg = null;
    if (obj) {
        arg = obj[key] || null;
        delete obj[key];
    }
    return arg;
}

function Controller() {
    require("alloy/controllers/BaseController").apply(this, Array.prototype.slice.call(arguments));
    this.__controllerPath = "main";
    if (arguments[0]) {
        var __parentSymbol = __processArg(arguments[0], "__parentSymbol");
        {
            __processArg(arguments[0], "$model");
        }
        {
            __processArg(arguments[0], "__itemTemplate");
        }
    }
    var $ = this;
    var exports = {};
    var __alloyId3 = [];
    $.__views.__alloyId5 = Alloy.createController("Projects", {
        id: "__alloyId5",
        __parentSymbol: __parentSymbol
    });
    $.__views.__alloyId4 = Ti.UI.createTab({
        window: $.__views.__alloyId5.getViewEx({
            recurse: true
        }),
        title: "Projects",
        id: "__alloyId4"
    });
    __alloyId3.push($.__views.__alloyId4);
    $.__views.__alloyId9 = Alloy.createController("Tasks", {
        id: "__alloyId9",
        __parentSymbol: __parentSymbol
    });
    $.__views.__alloyId8 = Ti.UI.createTab({
        window: $.__views.__alloyId9.getViewEx({
            recurse: true
        }),
        title: "Tasks",
        id: "__alloyId8"
    });
    __alloyId3.push($.__views.__alloyId8);
    $.__views.main = Ti.UI.createTabGroup({
        tabs: __alloyId3,
        id: "main"
    });
    $.__views.main && $.addTopLevelView($.__views.main);
    exports.destroy = function() {};
    _.extend($, $.__views);
    _.extend($, exports);
}

var Alloy = require("alloy"), Backbone = Alloy.Backbone, _ = Alloy._;

module.exports = Controller;