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

app.config(function ($logProvider, SiteMapProvider, $locationProvider) {

    // use the HTML5 History API
    $locationProvider.html5Mode(true);

    $logProvider.debugEnabled(true);


});

app.run([
    '$rootScope', '$location', '$http', '$log',
    function ($rootScope, $location, $http, $log) {

        var userUrlMatches = [];
        userUrlMatches.push(/^\/[^\/]+\/Projects[\/]{0,1}$/);
        userUrlMatches.push(/^\/[^\/]+\/ProjectNew[\/]{0,1}$/);
        userUrlMatches.push(/^\/[^\/]+\/Details[\/]{0,1}$/);
        userUrlMatches.push(/^\/[^\/]*[\/]{0,1}$/);

        var $bIsUser = false;
        var i, l;
        for (i = 0; i < (l = userUrlMatches.length) ; i++)
            $bIsUser = $bIsUser || userUrlMatches[i].test($location.$$path);

        if ($bIsUser) {
            $rootScope.TmplMain = 'partials/User.html';
        } else {
            $rootScope.TmplMain = 'partials/UserProject.html';
        }

        $log.debug($location);
    }
]);

//todo cip find a better way to register modules/plugins to load
//todo cip ! now how to load myAppDev or myApp?
var ModulesToLoad = ['myAppDev'];
