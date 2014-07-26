'use strict';

/* Services */


var services = angular.module('myApp.services', ['ngResource']);

services.factory('UsersApi', function ($resource) {
    return $resource('/users', {}, {
        register: { method: 'POST' }
    });
});

services.factory('LoggedUserApi', function ($resource) {
    return $resource('/users', {}, {
        myDetails: { method: 'GET', params: { id: '@id' } },
    });
});


/*
// sample of usage for the ngResources (more details at http://draptik.github.io/blog/2013/07/28/restful-crud-with-angularjs/)

services.factory('UserFactory', function ($resource) {
    return $resource(baseUrl + '/ngdemo/web/users/:id', {}, {
        show: { method: 'GET' },
        update: { method: 'PUT', params: { id: '@id' } },
        delete: { method: 'DELETE', params: { id: '@id' } }
    });
});
*/
