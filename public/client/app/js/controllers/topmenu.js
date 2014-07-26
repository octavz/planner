'use strict';

/* Controllers */

angular.module('myApp.controllers')

.controller('TopMenuCtrl', ['Auth', '$rootScope', '$scope', '$location', 'SiteMap', '$log', 'CurrentView', '$route', '$routeParams',
    function (Auth, $rootScope, $scope, $location, SiteMap, $log, CurrentView, $route, $routeParams) {

        var tranformProjectLinks = function (item) {
            var newitem = {
                link: applyProjectPath(item.link),
                title: item.title,
            };
            if (item.items == null)
                return newitem;
            else
                newitem.items = _(item.items).map(tranformProjectLinks);
            return newitem;
        }
        var tranformUserLinks = function (item) {
            var newitem = {
                link: applyUserPath(item.link),
                title: item.title,
            };
            if (item.items == null)
                return newitem;
            else
                newitem.items = _(item.items).map(tranformProjectLinks);
            return newitem;
        }
 
        var applyProjectPath = function (link) {

            return SiteMap.GetAbsolutePathForUserAndProject(link);
        }
        var applyUserPath = function (link) {

            return SiteMap.GetAbsolutePathForUser(link);
        }
        SiteMap.init().then(function () {

            $log.debug("SiteMap was initialized");
            if (CurrentView.IsUser()) {
                $scope.menuItemsUser = _(SiteMap.LinksUser.items).map(tranformUserLinks);
            } else {
                $scope.menuItems = _(SiteMap.Links.items).map(tranformProjectLinks);
            }

            $scope.Home = SiteMap.getCurrentUserLink();
            $scope.MyHomePage = SiteMap.GetBaseUrlForUser();
            $scope.MySettings = applyUserPath('MySettings');
            $scope.ProjectNew = applyUserPath('ProjectNew');
        })

        $scope.attachTopNodeClass = function (menuItem) {
            var cssClasses = [];
            cssClasses.push(isActive(menuItem));
            //todo cip wouldnt be better to put this in html?
            cssClasses.push(menuItem.items != null ? "dropdown" : "");

            return cssClasses.join(" ");
        }

        var isActive = function (menuItem) {
            var loc = "#" + $location.path();
            if (menuItem.link == loc) {
                return 'active';
            }
        }


        $scope.isLoggedIn = Auth.isLoggedIn;
    }
]);
