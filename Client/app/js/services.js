'use strict';

/* Services */


var services = angular.module('myApp.services', ['ngResource']);

var baseUrl = 'http://localhost\\:49664';

services.factory('UsersFactory', function ($resource) {
    return $resource(baseUrl + '/users', {}, {
        query: { method: 'GET', isArray: true },
        create: { method: 'POST' }
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
