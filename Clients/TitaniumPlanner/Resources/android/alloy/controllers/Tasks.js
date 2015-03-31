function __processArg(obj, key) {
    var arg = null;
    if (obj) {
        arg = obj[key] || null;
        delete obj[key];
    }
    return arg;
}

function Controller() {
    function __alloyId29(e) {
        if (e && e.fromAdapter) return;
        __alloyId29.opts || {};
        var models = __alloyId28.models;
        var len = models.length;
        var rows = [];
        for (var i = 0; len > i; i++) {
            var __alloyId24 = models[i];
            __alloyId24.__transform = {};
            var __alloyId25 = Ti.UI.createTableViewRow({
                hasCheck: false,
                width: Ti.UI.FILL,
                height: "40dp",
                rowId: "undefined" != typeof __alloyId24.__transform["id_task"] ? __alloyId24.__transform["id_task"] : __alloyId24.get("id_task")
            });
            rows.push(__alloyId25);
            var __alloyId26 = Ti.UI.createView({
                width: "90%",
                height: Ti.UI.FILL,
                left: "10dp"
            });
            __alloyId25.add(__alloyId26);
            var __alloyId27 = Ti.UI.createLabel({
                left: 0,
                color: "black",
                font: {
                    fontFamily: "HelveticaNeue",
                    fontSize: "12sp"
                },
                text: "undefined" != typeof __alloyId24.__transform["title"] ? __alloyId24.__transform["title"] : __alloyId24.get("title")
            });
            __alloyId26.add(__alloyId27);
        }
        $.__views.tbTasks.setData(rows);
    }
    function removeRow(_row) {
        var recoverDatabase = Alloy.createCollection("Task");
        recoverDatabase.fetch({
            query: "SELECT * FROM Tasks"
        });
        for (var i = 0; i < recoverDatabase.length; i++) if (recoverDatabase.at(i).get("id_task") == _row.rowId) {
            var table = Alloy.createCollection("Task");
            table.fetch({
                query: "SELECT * FROM Tasks where id_task = " + _row.rowId
            });
            if (table.length > 0) {
                table.at(0).destroy();
                Alloy.Collections.Task.fetch();
            }
        }
    }
    require("alloy/controllers/BaseController").apply(this, Array.prototype.slice.call(arguments));
    this.__controllerPath = "Tasks";
    if (arguments[0]) {
        {
            __processArg(arguments[0], "__parentSymbol");
        }
        {
            __processArg(arguments[0], "$model");
        }
        {
            __processArg(arguments[0], "__itemTemplate");
        }
    }
    var $ = this;
    var exports = {};
    Alloy.Collections.instance("Task");
    $.__views.Tasks = Ti.UI.createWindow({
        backgroundColor: "white",
        id: "Tasks"
    });
    $.__views.Tasks && $.addTopLevelView($.__views.Tasks);
    $.__views.__alloyId23 = Ti.UI.createLabel({
        text: "Tasks",
        id: "__alloyId23"
    });
    $.__views.Tasks.add($.__views.__alloyId23);
    $.__views.tfInsert = Ti.UI.createTextField({
        width: "290dp",
        height: "40dp",
        top: "10dp",
        left: "15dp",
        borderRadius: "6dp",
        borderColor: "black",
        borderWidth: "1dp",
        maxLength: "80dp",
        paddingLeft: "10dp",
        textAlign: Titanium.UI.TEXT_ALIGNMENT_LEFT,
        clearOnEdit: true,
        id: "tfInsert"
    });
    $.__views.Tasks.add($.__views.tfInsert);
    $.__views.btnInsert = Ti.UI.createButton({
        right: "15dp",
        top: "60dp",
        title: "Insert",
        id: "btnInsert"
    });
    $.__views.Tasks.add($.__views.btnInsert);
    $.__views.tbTasks = Ti.UI.createTableView({
        widht: Ti.UI.FILL,
        height: Ti.UI.FILL,
        top: "100dp",
        id: "tbTasks"
    });
    $.__views.Tasks.add($.__views.tbTasks);
    var __alloyId28 = Alloy.Collections["Task"] || Task;
    __alloyId28.on("fetch destroy change add remove reset", __alloyId29);
    exports.destroy = function() {
        __alloyId28.off("fetch destroy change add remove reset", __alloyId29);
    };
    _.extend($, $.__views);
    arguments[0] || {};
    $.btnInsert.addEventListener("click", function() {
        $.tfInsert.blur();
        if ("" != $.tfInsert.value) {
            var task = Alloy.createModel("Task", {
                title: $.tfInsert.value
            });
            task.save();
            Alloy.Collections.Task.fetch();
        } else alert("Please fill out the text field above!");
    });
    $.tbTasks.addEventListener("longpress", function(e) {
        var tableViewEvent = e.row;
        var alertDialog = Titanium.UI.createAlertDialog({
            title: "Remove",
            message: "Do you want to remove this row?",
            buttonNames: [ "Yes", "No" ],
            cancel: 1
        });
        alertDialog.show();
        alertDialog.addEventListener("click", function(e) {
            0 == e.index ? removeRow(tableViewEvent) : 1 == e.index;
        });
    });
    Alloy.Collections.Task.fetch();
    _.extend($, exports);
}

var Alloy = require("alloy"), Backbone = Alloy.Backbone, _ = Alloy._;

module.exports = Controller;