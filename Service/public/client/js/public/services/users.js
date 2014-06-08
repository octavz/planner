'use strict';

/* Services */


var services = angular.module('myAppPublic.services', ['ngResource']);
services.factory('UsersApi', function($resource) {
    return $resource('users', {}, {
        register: {
            method: 'POST'
        }
    });
});
