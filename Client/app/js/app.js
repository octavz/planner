'use strict';

// Declare app level module which depends on filters, and services
var app = angular.module('myApp', [
    'ngRoute',
    'myApp.filters',
    'myApp.services',
    'myApp.directives',
    'myApp.controllers',
    'myAuth.services',
    'myApp.utils',
    'ngStorage',
    'ui.bootstrap'
]);

angular.module('myApp.controllers', []);

app.config(function ($logProvider, SiteMapProvider, $locationProvider) {

    // use the HTML5 History API
    $locationProvider.html5Mode(true);

    $logProvider.debugEnabled(true);

});

app.run([
    '$rootScope', 'CurrentView',
    function ($rootScope, CurrentView) {

        var $bIsUser = CurrentView.IsUser();

        if ($bIsUser) {
            $rootScope.TmplMain = 'partials/User.html';
        } else {
            $rootScope.TmplMain = 'partials/UserProject.html';
        }

   }
]);

//todo cip find a better way to register modules/plugins to load
//todo cip ! now how to load myAppDev or myApp?
var ModulesToLoad = ['myAppDev'];
