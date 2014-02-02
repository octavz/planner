'use strict';


// Declare app level module which depends on filters, and services
angular.module('myApp', [
  'ngRoute',
  'myApp.filters',
  'myApp.services',
  'myApp.directives',
  'myApp.controllers'
]).
config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/Register', { templateUrl: 'partials/Register.html', controller: 'RegisterCtrl' });
  $routeProvider.when('/RegisterOk', { templateUrl: 'partials/RegisterOk.html', controller: 'RegisterOkCtrl' });
  $routeProvider.otherwise({redirectTo: '/Register'});
}]);
