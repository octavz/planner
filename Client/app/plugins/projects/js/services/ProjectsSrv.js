'use strict';

angular.module('myApp.plugins.projects')

.service('ProjectsSrv',
    ['$location', '$window', '$routeParams', 'ProjectsApi', 'SiteMap',
    function ($location, $window, $routeParams, ProjectsApi, SiteMap) {

        this.TransformProjects = function (proj) {
            var ret = proj;
            ret.link = SiteMap.GetAbsolutePath(proj.id);
            return ret;
        }

    }]);