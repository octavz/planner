'use strict';

/* Services */

angular.module('myApp.plugins.projects.rest', ['ngResource'])

.factory('ProjectsApi', function ($resource) {
    return $resource('/projects', {}, {
        insertProject: { method: 'POST' },
        updateProject: { method: 'PUT' },
    });
});

