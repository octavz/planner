'use strict';


//app with mock services
var myAppLoginDev = angular.module('myAppPublicDev', ['myAppPublic', 'ngMockE2E', 'angular-underscore/utils']);

//for additional examples see http://docs.angularjs.org/api/ngMockE2E.$httpBackend and http://docs.angularjs.org/api/ngResource.$resource
myAppLoginDev.run(function ($httpBackend) {

    var users = [{
        login: 'existing@mail.com'
    }];

    $httpBackend.whenPOST(/register/).respond(function (method, url, data) {
        var jsondata = angular.fromJson(data);
        var existingUsers = _(users).filter(function (o) {
            return o.login == jsondata.login;
        });

        if (existingUsers.length > 0) {
            return [200, {
                errMessage: "User already exists"
            }, {}];
        }

        users.push(angular.fromJson(data));
        return [200, {}, {}];
    });

    $httpBackend.whenGET(/^\/app\//).passThrough();
    $httpBackend.whenGET(/^app\//).passThrough();

})
