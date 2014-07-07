'use strict';

angular.module('myApp.plugins.projects.controllers', [
    'myApp.plugins.projects.rest'
])

.controller('ProjectsCtrl', ['$scope', '$location', '$window', '$routeParams', 'ProjectsApi', 'SiteMap', function ($scope, $location, $window, $routeParams, ProjectsApi, SiteMap) {


    var TransformProjects = function(proj) {
        var ret = proj;
        ret.link = SiteMap.GetAbsolutePath(proj.code);
        return ret;
    }

    var projectcode = $routeParams.projectcode;
    if (projectcode != null) {
        $scope.title = 'Edit project';
        ProjectsApi.get({ id: projectcode }, function (resp) {
            console.log("received something", resp);

            //fill the scope
            $scope.project = {
                Name: resp.data.name,
                Description: resp.data.desc,
                Parent: resp.data.parent

            }
        });

    } else {
        $scope.title = 'Insert a new project';
    }

    ProjectsApi.get(function (resp) {
        $scope.projects = _(resp.data).map(TransformProjects);
    });

    $scope.save = function () {

        if ($scope.projectForm.$invalid) {
            return;
        }

        if (projectcode != null) {
            //make a post
            ProjectsApi.updateProject({
                id: projectcode,
                name: $scope.project.Name,
                desc: $scope.project.Description,
                parent: $scope.project.Parent,
            }
            , function (data, headers) {
                if (typeof (data.error) !== "undefined") {
                    return;
                }
                if (data.ok) {
                    $location.path('/Projects');
                }
            });
        } else {
            //make a put

            ProjectsApi.insertProject({
                name: $scope.project.Name,
                desc: $scope.project.Description,
                parent: $scope.project.Parent,
            }
            , function (data, headers) {
                if (typeof (data.error) !== "undefined") {
                    return;
                }
                if (data.ok) {
                    $scope.RedirectProject(data.o);
                }
            });
        }
    }
}]);