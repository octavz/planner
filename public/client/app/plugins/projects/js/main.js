'use strict';

var plugin = angular.module('myApp.plugins.projects', ['ngResource']);


plugin.config(function (SiteMapProvider, $routeProvider, RouteAccessProvider) {

    var userRoutePrefix = SiteMapProvider.GetBaseRouteUrlForUser();
    var projRouterPrefix = SiteMapProvider.GetBaseRouteUrlForProject();

    $routeProvider.when(userRoutePrefix + '', {
        templateUrl: 'app/plugins/projects/html/User.html',
        controller: 'UserCtrl',
        resolve: RouteAccessProvider.routeResolvers
    });
    $routeProvider.when(userRoutePrefix + '/Projects', {
        templateUrl: 'app/plugins/projects/html/Projects.html',
        controller: 'Projects',
        resolve: RouteAccessProvider.routeResolvers
    });
    $routeProvider.when(userRoutePrefix + '/ProjectNew', {
        templateUrl: 'app/plugins/projects/html/ProjectNew.html',
        controller: 'ProjectInsert',
        resolve: RouteAccessProvider.routeResolvers
    });
    $routeProvider.when(projRouterPrefix + '', {
        templateUrl: 'app/plugins/projects/html/ProjectHome.html',
        controller: 'Projects',
        resolve: RouteAccessProvider.routeResolvers
    });
    $routeProvider.when(projRouterPrefix + '/Edit', {
        templateUrl: 'app/plugins/projects/html/ProjectNew.html',
        controller: 'ProjectEdit',
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
