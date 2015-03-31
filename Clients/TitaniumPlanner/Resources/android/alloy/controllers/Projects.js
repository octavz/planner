function __processArg(obj, key) {
    var arg = null;
    if (obj) {
        arg = obj[key] || null;
        delete obj[key];
    }
    return arg;
}

function Controller() {
    function __alloyId20(e) {
        if (e && e.fromAdapter) return;
        __alloyId20.opts || {};
        var models = __alloyId19.models;
        var len = models.length;
        var rows = [];
        for (var i = 0; len > i; i++) {
            var __alloyId15 = models[i];
            __alloyId15.__transform = {};
            var __alloyId16 = Ti.UI.createTableViewRow({
                hasCheck: false,
                width: Ti.UI.FILL,
                height: "40dp",
                rowId: "undefined" != typeof __alloyId15.__transform["id_project"] ? __alloyId15.__transform["id_project"] : __alloyId15.get("id_project")
            });
            rows.push(__alloyId16);
            var __alloyId17 = Ti.UI.createView({
                width: "90%",
                height: Ti.UI.FILL,
                left: "10dp"
            });
            __alloyId16.add(__alloyId17);
            var __alloyId18 = Ti.UI.createLabel({
                left: 0,
                color: "black",
                font: {
                    fontFamily: "HelveticaNeue",
                    fontSize: "12sp"
                },
                text: "undefined" != typeof __alloyId15.__transform["name"] ? __alloyId15.__transform["name"] : __alloyId15.get("name")
            });
            __alloyId17.add(__alloyId18);
        }
        $.__views.tbProjects.setData(rows);
    }
    function removeRow(_row) {
        var recoverDatabase = Alloy.createCollection("Project");
        recoverDatabase.fetch({
            query: "SELECT * FROM Projects"
        });
        for (var i = 0; i < recoverDatabase.length; i++) if (recoverDatabase.at(i).get("id_project") == _row.rowId) {
            var table = Alloy.createCollection("Project");
            table.fetch({
                query: "SELECT * FROM Projects where id_project= " + _row.rowId
            });
            if (table.length > 0) {
                table.at(0).destroy();
                Alloy.Collections.Project.fetch();
            }
        }
    }
    require("alloy/controllers/BaseController").apply(this, Array.prototype.slice.call(arguments));
    this.__controllerPath = "Projects";
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
    Alloy.Collections.instance("Project");
    $.__views.Projects = Ti.UI.createWindow({
        backgroundColor: "white",
        id: "Projects"
    });
    $.__views.Projects && $.addTopLevelView($.__views.Projects);
    $.__views.__alloyId14 = Ti.UI.createLabel({
        text: "Projects",
        id: "__alloyId14"
    });
    $.__views.Projects.add($.__views.__alloyId14);
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
    $.__views.Projects.add($.__views.tfInsert);
    $.__views.btnInsert = Ti.UI.createButton({
        right: "15dp",
        top: "60dp",
        title: "Insert",
        id: "btnInsert"
    });
    $.__views.Projects.add($.__views.btnInsert);
    $.__views.tbProjects = Ti.UI.createTableView({
        widht: Ti.UI.FILL,
        height: Ti.UI.FILL,
        top: "100dp",
        id: "tbProjects"
    });
    $.__views.Projects.add($.__views.tbProjects);
    var __alloyId19 = Alloy.Collections["Project"] || Project;
    __alloyId19.on("fetch destroy change add remove reset", __alloyId20);
    exports.destroy = function() {
        __alloyId19.off("fetch destroy change add remove reset", __alloyId20);
    };
    _.extend($, $.__views);
    arguments[0] || {};
    $.btnInsert.addEventListener("click", function() {
        $.tfInsert.blur();
        if ("" != $.tfInsert.value) {
            var project = Alloy.createModel("Project", {
                name: $.tfInsert.value
            });
            project.save();
            Alloy.Collections.Project.fetch();
        } else alert("Please fill out the text field above!");
    });
    $.tbProjects.addEventListener("longpress", function(e) {
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
    Alloy.Collections.Project.fetch();
    _.extend($, exports);
}

var Alloy = require("alloy"), Backbone = Alloy.Backbone, _ = Alloy._;

module.exports = Controller;