'use strict';

angular.module('myApp.controllers')

.controller('ProjectsCtrl', ['$scope', '$location', 'ProjectsApi', function ($scope, $location, ProjectsApi) {
    $scope.title = 'Projects';

    $scope.save = function(project){

        if ($scope.projectForm.$invalid) {
            return;
        }

        ProjectsApi.saveProject({
            Name: project.Name,
            Description: project.Description,
        }
        , function (data, headers) {
            if (typeof (data.error) !== "undefined") {
                return;
            }
            if (data.ok) {
                $location.path('index.html');
            }
        });
    }
}]);