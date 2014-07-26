'use strict';

/* Services */

angular.module('myApp.plugins.projects')

.factory('ProjectsApi', function ($resource) {
    return $resource('/projects/:id', {},
            {
                insertProject: { method: 'POST' },
                updateProject: { method: 'PUT' },
            });
});

