'use strict';


//app with mock services
var myAppDev = angular.module('myAppDev', ['myApp', 'ngMockE2E', 'angular-underscore/utils']);

//for additional examples see http://docs.angularjs.org/api/ngMockE2E.$httpBackend and http://docs.angularjs.org/api/ngResource.$resource
myAppDev.run(function ($httpBackend) {

    var users = [
    {
        Email: 'existing@mail.com'
    }];

    // returns the current list of phones
    $httpBackend.whenGET(/users/).respond(users);

    //todo cip dont make it relative
    $httpBackend.whenPOST(/users/).respond(function (method, url, data) {
        var jsondata = angular.fromJson(data);
        var existingUsers = _(users).filter(function (o) { return o.Email == jsondata.Email; });

        if (existingUsers.length > 0) {
            return [200, { error: "User already exists" }, {}];
        }

        //users.push(angular.fromJson(data));
        return [200, { ok: true }, {}];
    });

    // Projects mock.
    $httpBackend.whenPOST(/projects/).respond(function (method, url, data) {
        var jsondata = angular.fromJson(data);

        return [200, { ok: true }, {}];
    });

    $httpBackend.whenGET(/^\/templates\//).passThrough();
    $httpBackend.whenGET(/^partials\//).passThrough();
    //...
})
