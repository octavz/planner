'use strict';


//app with mock services
var myAppDev = angular.module('myAppDev', ['myApp', 'ngMockE2E', 'angular-underscore/utils']);

//for additional examples see http://docs.angularjs.org/api/ngMockE2E.$httpBackend and http://docs.angularjs.org/api/ngResource.$resource
myAppDev.run(function ($httpBackend) {

    $httpBackend.whenGET(/^\/templates\//).passThrough();
    $httpBackend.whenGET(/^partials\//).passThrough();
    $httpBackend.whenGET(/html/).passThrough();

    var users = [{
        Email: 'existing@mail.com'
    }];

    var routes = {
        data: [
            //todo cip what is this?
            calcMD5("/Home"),

            calcMD5("/:usercode"),
            calcMD5("/:usercode/Projects"),
            calcMD5("/:usercode/ProjectNew"),

            calcMD5("/:usercode/:projectcode"),
            calcMD5("/:usercode/:projectcode/Edit"),
            calcMD5("/:usercode/:projectcode/Tasks"),
            calcMD5("/:usercode/:projectcode/TaskNew")
        ]
    };

    $httpBackend.whenGET(/users/).respond(users);
    $httpBackend.whenGET(/routes/).respond(routes);

    $httpBackend.whenPOST(/users/).respond(function (method, url, data) {
        var jsondata = angular.fromJson(data);
        var existingUsers = _(users).filter(function (o) {
            return o.Email == jsondata.Email;
        });

        if (existingUsers.length > 0) {
            return [200, {
                error: "User already exists"
            }, {}];
        }

        //users.push(angular.fromJson(data));
        return [200, {
            ok: true
        }, {}];
    });

    // Projects mock.
    var currentIdx = 0;
    var projects = [];

    //handle possible 10 projects
    for (var i = 0; i < 10; i++) {
        projects.push({ id: ++currentIdx, name: "proj "+i, desc: "a descr", parent: "" });

        var regexp = new RegExp('projects\?id\=' + i);
        $httpBackend.whenGET(regexp).respond({ data: _.findWhere(projects, { id: i }) });
    }
    
    $httpBackend.whenGET(/projects/).respond({ data: projects });
    $httpBackend.whenPOST(/projects/).respond(function (method, url, data) {

        var jsondata = angular.fromJson(data);
        jsondata.id = ++currentIdx;
        projects.push(jsondata);

        return [200, { ok: true, o: jsondata }];
    });

    $httpBackend.whenPUT(/projects/).respond(function (method, url, data) {

        var jsondata = angular.fromJson(data);
        var id = parseInt(jsondata.id);
        //find the object in our array
        var o = _.findWhere(projects, { id: id });
        if (o != undefined) {
            o.name = jsondata.name;
            o.desc = jsondata.desc;
            o.parent = jsondata.parent;
        } else {
            console.warn("mock: project not found for edit", data);
            //?
            return [404];
        }

        return [200, { ok: true, o: jsondata }];
    });

    $httpBackend.whenGET(/.*/).passThrough();
})
