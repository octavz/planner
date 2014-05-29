'use strict';

// Declare app level module which depends on filters, and services
var app = angular.module('myApp', [
    'ngRoute',
    'myApp.filters',
    'myApp.services',
    'myApp.directives',
    'myApp.controllers',
    'myAuth.services',
    'ngStorage',
    'ui.bootstrap'
]);

angular.module('myApp.controllers', []);


//see https://coderwall.com/p/y0zkiw
app.config(function ($controllerProvider, $compileProvider, $filterProvider, $provide, $logProvider, SiteMapProvider) {

    SiteMapProvider.RegisterLinks([
                {
                    'title': 'Home',
                    'link': '#/Home'
                }, {
                    'title': 'Projects',
                    'link': '#/Projects'
                }, {
                    'title': 'New Project',
                    'link': '#/ProjectNew'
                }, {
                    'title': 'Other',
                    'link': '#/Other'
                }
    ]);


    // save references to the providers
    app.lazy = {
        controller: $controllerProvider.register,
        directive: $compileProvider.directive,
        filter: $filterProvider.register,
        factory: $provide.factory,
        service: $provide.service
    };

    // define routes, etc.
});


//todo cip find a better way to register modules/plugins to load
//todo cip ! now how to load myAppDev or myApp?
var ModulesToLoad = ['myAppDev'];
