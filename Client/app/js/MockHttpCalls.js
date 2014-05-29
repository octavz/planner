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

            calcMD5("/Tasks"),
            calcMD5("/TaskNew"),
            calcMD5("/Tasks/items")
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

    $httpBackend.whenGET(/.*/).passThrough();
    $httpBackend.whenGET(/^\/templates\//).passThrough();
    $httpBackend.whenGET(/^partials\//).passThrough();
    //...
})
