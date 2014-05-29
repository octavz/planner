'use strict';

var plugin = angular.module('myApp.plugins.tasks', []);

plugin.config(function (SiteMapProvider, $routeProvider, RouteAccessProvider) {

    //register routes
    $routeProvider.when('/Tasks', {
        templateUrl: 'plugins/tasks/html/Tasks.html',
        resolve: RouteAccessProvider.routeResolvers
    });

    $routeProvider.when('/TaskNew', {
        templateUrl: 'plugins/tasks/html/TaskNew.html',
        resolve: RouteAccessProvider.routeResolvers
    });

    //register links in the menu
    SiteMapProvider.RegisterLinks([{
        'title': 'Tasks',
        'link': '#/Tasks'
    }, {
        'title': 'New Task',
        'link': '#/TaskNew'
    }
    ]);
});

ModulesToLoad.push('myApp.plugins.tasks');
