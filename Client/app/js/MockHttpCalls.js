'use strict';


//app with mock services
var myAppDev = angular.module('myAppDev', ['myApp', 'ngMockE2E', 'angular-underscore/utils']);

//for additional examples see http://docs.angularjs.org/api/ngMockE2E.$httpBackend and http://docs.angularjs.org/api/ngResource.$resource
myAppDev.run(function ($httpBackend) {

    var users = [{
        Email: 'existing@mail.com'
    }];

    var routes = {
        data: [
            calcMD5("/Home"),
            calcMD5("/Projects"),
            calcMD5("/ProjectNew"),
            calcMD5("/ProjectEdit/:id"),
            calcMD5("/Tasks")]
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
    projects.push({ id: ++currentIdx, name: "proj 1", desc: "a descr", parent: "" });
    projects.push({ id: ++currentIdx, name: "proj 2", desc: "a descr", parent: "" });

    $httpBackend.whenGET(/projects\?id\=1/).respond({ data: _.findWhere(projects, { id: 1 }) });
    $httpBackend.whenGET(/projects\?id\=2/).respond({ data: _.findWhere(projects, { id: 2 }) });

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
    $httpBackend.whenGET(/^\/templates\//).passThrough();
    $httpBackend.whenGET(/^partials\//).passThrough();
    //...
})
