'use strict';

/* Services */


var services = angular.module('myApp.services', ['ngResource']);

var baseUrl = 'http://localhost\\:9090';

services.factory('UsersApi', function ($resource) {
    return $resource(baseUrl + '/users', {}, {
        register: { method: 'POST' }
    });
});

services.factory('ProjectsApi', function ($resource) {
    return $resource(baseUrl + '/projects', {}, {
        saveProject: { method: 'POST' }
    });
});


services.factory('LoggedUserApi', function ($resource) {
    return $resource(baseUrl + '/users', {}, {
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
