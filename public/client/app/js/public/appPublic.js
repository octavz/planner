'use strict';

// App for login
angular.module('myAppPublic', [
    'ngRoute',
    'myAppPublic.services',
    'myAppPublic.controllers',
    'ui.bootstrap'
])

.config(['$routeProvider',
    function($routeProvider) {

        $routeProvider.when('/Login', {
            templateUrl: 'app/partials/Public/Login.html'
        });
        $routeProvider.when('/Register', {
            templateUrl: 'app/partials/Public/Register.html',
            controller: 'RegisterCtrl'
        });
        $routeProvider.when('/RegisterOk', {
            templateUrl: 'app/partials/Public/RegisterOk.html',
        });
        $routeProvider.otherwise({
            redirectTo: '/Login'
        });
    }
]);

angular.module('myAppPublic.controllers', []);
