'use strict';

var plugin = angular.module('myApp.plugins.tasks', []);

plugin.config(function (SiteMapProvider, $routeProvider, RouteAccessProvider) {

    var projRoutePrefix = SiteMapProvider.GetBaseRouteUrlForProject();

    //register routes
    $routeProvider.when(projRoutePrefix + '/Tasks', {
        templateUrl: 'plugins/tasks/html/Tasks.html',
        resolve: RouteAccessProvider.routeResolvers
    });

    $routeProvider.when(projRoutePrefix + '/TaskNew', {
        templateUrl: 'plugins/tasks/html/TaskNew.html',
        resolve: RouteAccessProvider.routeResolvers
    });

    //register links in the menu
    SiteMapProvider.RegisterLinks([
        {
            'title': 'Tasks',
            'items': [
                {
                    'title': 'Show All',
                    'link': 'Tasks'
                }, {
                    'title': 'Add New Task',
                    'link':  'TaskNew'
                }
            ]
        }
    ]);
});

ModulesToLoad.push('myApp.plugins.tasks');
