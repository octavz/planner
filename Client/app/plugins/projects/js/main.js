'use strict';

var plugin = angular.module('myApp.plugins.projects', [
    'myApp.plugins.projects.controllers'
]);

angular.module('myApp.plugins.projects.controllers', []);

plugin.config(function (SiteMapProvider, $routeProvider, RouteAccessProvider) {

    $routeProvider.when('/Projects', {
        templateUrl: 'plugins/projects/html/Projects.html',
        controller: 'ProjectsCtrl',
        resolve: RouteAccessProvider.routeResolvers
    });
    $routeProvider.when('/ProjectNew', {
        templateUrl: 'plugins/projects/html/ProjectNew.html',
        controller: 'ProjectsCtrl',
        resolve: RouteAccessProvider.routeResolvers
    });
    $routeProvider.when('/ProjectEdit/:id', {
        templateUrl: 'plugins/projects/html/ProjectNew.html',
        controller: 'ProjectsCtrl',
        resolve: RouteAccessProvider.routeResolvers
    });

    //register links in the menu
    SiteMapProvider.RegisterLinks([
        {
            'title': 'Projects',
            'link': '#/Projects'
        }, {
            'title': 'New Project',
            'link': '#/ProjectNew'
        }
    ]);
});

ModulesToLoad.push('myApp.plugins.projects');
