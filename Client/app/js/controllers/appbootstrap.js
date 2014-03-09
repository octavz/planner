'use strict';

/* Controllers */

angular.module('myApp.controllers')

.controller('AppBootstrapCtrl', ['$scope', '$location', 'CurrentUserSession', 'AllowedRoutes',
    function ($scope, $location, CurrentUserSession, AllowedRoutes) {

        AllowedRoutes.get(function (data, headers) {
            CurrentUserSession.setRights(data.routes);

            $location.path('/Home');
        }, function (err) {
            $location.path('/Error');
        });

    }
]);
