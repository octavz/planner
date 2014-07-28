'use strict';

/* Controllers */

angular.module('myApp.controllers')

.controller('AppBootstrapCtrl', ['Auth', '$scope', '$location','SiteMap','$log',
    function(Auth, $scope, $location, SiteMap,$log) {

        if (Auth.isLoggedIn()) {
            $log.debug("SiteMap was initialized");

   	        SiteMap.init().then(function () {
	            $location.path(SiteMap.getCurrentUserLink());
	        })

        } else {
            $location.path('/Exit');
        }
    }
]);