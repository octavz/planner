'use strict';

// App for login
angular.module('myAppPublic', [
    'ngRoute',
    'myAppPublic.services',
    'myAppPublic.controllers'
])

.config(['$routeProvider',
    function($routeProvider) {

        $routeProvider.when('/Login', {
            templateUrl: 'partials/Public/Login.html'
        });
        $routeProvider.when('/Register', {
            templateUrl: 'partials/Public/Register.html',
            controller: 'RegisterCtrl'
        });
        $routeProvider.when('/RegisterOk', {
            templateUrl: 'partials/Public/RegisterOk.html',
        });
        $routeProvider.otherwise({
            redirectTo: '/Login'
        });
    }
]);

angular.module('myAppPublic.controllers', []);
