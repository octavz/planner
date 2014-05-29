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

app.config(function ($logProvider, SiteMapProvider) {

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


});


//todo cip find a better way to register modules/plugins to load
//todo cip ! now how to load myAppDev or myApp?
var ModulesToLoad = ['myAppDev'];
