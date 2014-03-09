'use strict';

/* Controllers */

angular.module('myApp.controllers')

.controller('ErrorCtrl', ['$scope', '$location', 'RightsApi',
    function($scope, $location, RightsApi) {

        RightsApi.get()
            .success(function(data) {
                CurrentUserSession.setRights(data.rights);

                $location.path('/Home');
            })
            .error(function(data) {
                CurrentUserSession.setRights(data.rights);

                $location.path('/Error');
            });

    }
]);
