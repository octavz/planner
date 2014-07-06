'use strict';

var plugin = angular.module('myApp.plugins.projects', [
    'myApp.plugins.projects.controllers'
]);


plugin.config(function (SiteMapProvider, $routeProvider, RouteAccessProvider) {

    var userRoutePrefix = SiteMapProvider.GetBaseRouteUrlForUser();
    var projRouterPrefix = SiteMapProvider.GetBaseRouteUrlForProject();

    $routeProvider.when(userRoutePrefix + '', {
        templateUrl: 'plugins/projects/html/User.html',
        controller: 'UserCtrl',
        resolve: RouteAccessProvider.routeResolvers
    });
    $routeProvider.when(userRoutePrefix + '/Projects', {
        templateUrl: 'plugins/projects/html/Projects.html',
        controller: 'ProjectsCtrl',
        resolve: RouteAccessProvider.routeResolvers
    });
    $routeProvider.when(userRoutePrefix + '/ProjectNew', {
        templateUrl: 'plugins/projects/html/ProjectNew.html',
        controller: 'ProjectsCtrl',
        resolve: RouteAccessProvider.routeResolvers
    });
    $routeProvider.when(projRouterPrefix + '', {
        templateUrl: 'plugins/projects/html/ProjectHome.html',
        controller: 'ProjectsCtrl',
        resolve: RouteAccessProvider.routeResolvers
    });
    $routeProvider.when(projRouterPrefix + '/Edit', {
        templateUrl: 'plugins/projects/html/ProjectNew.html',
        controller: 'ProjectsCtrl',
        resolve: RouteAccessProvider.routeResolvers
    });

    //register links in the menu
    SiteMapProvider.RegisterLinks([
        {
            'title': 'Project',
            'items': [
              {
                  'title': 'Settings',
                  'link':  'Edit'
              }
            ]
        }

    ]);

    //register links in the menu
    SiteMapProvider.RegisterLinksUser([
        {
            'title': 'Projects',
            'link': 'Projects'
        }, {
            'title': 'New Project',
            'link':  'ProjectNew'
        }
    ]);
});

ModulesToLoad.push('myApp.plugins.projects');
