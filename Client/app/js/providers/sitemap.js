'use strict';

/* Services */

angular.module('myApp.services')

.provider('SiteMap', function () {
    var currentLinks = { items: [] };
    var currentLinksUser = { items: [] };
    var currentLinksSettings = { items: [] };

    var registeredRelativeLinks = [];
    var registeredUserRelativeLinks = [];
    var registeredLinksSettings = [];

    this.RegisterLinks = function (relativeLinks) {
        registeredRelativeLinks = registeredRelativeLinks.concat(relativeLinks);
    }

    this.RegisterLinksUser = function (relativeLinks) {
        registeredUserRelativeLinks = registeredUserRelativeLinks.concat(relativeLinks);
    }

    this.RegisterLinksSettings = function (links) {
        registeredLinksSettings = registeredLinksSettings.concat(links);
    }

    this.GetBaseRouteUrlForProject = function () {
        return '/:usercode/:projectcode';
    }

    this.GetBaseRouteUrlForUser = function () {
        return '/:usercode';
    }

    this.$get = [
        '$q', 'RouteAccessApi', '$log', '$window', 'CurrentView', '$route', '$routeParams',
        function ($q, RouteAccessApi, $log, $window, CurrentView, $route, $routeParams) {

            var calculateAllowedMenuLinks = function (allLinks, allowedRoutes) {
                var filtererLinks = _(allLinks).filter(function (linkItem) {

                    //todo cip !!! fix this!
                    return true;
                    var lnk = linkItem.link;
                    if (lnk[0] == '#')
                        lnk = lnk.substr(1, lnk.length - 1);
                    var lnkHashed = calcMD5(lnk);
                    var ret = _(allowedRoutes).contains(lnkHashed);
                    return ret;
                })
                $log.debug("calculateAllowedMenuLinks", filtererLinks);
                return filtererLinks;
            };
            var init = function () {
                $log.debug("init");
                return RouteAccessApi.get().$promise
                    .then(function (res) {
                        $log.debug("RouteAccessApi", res);
                        currentLinks.items = currentLinks.items.concat(calculateAllowedMenuLinks(registeredRelativeLinks, res.data));
                        currentLinksUser.items = registeredUserRelativeLinks;
                        currentLinksSettings.items = registeredLinksSettings;
                    });
            };

            var GetBaseUrlForProject = function () {
                if ($routeParams.projectcode == null) {
                    $log.error("projectcode is missing", $routeParams);
                    throw "Cannot find project code";
                }
                if ($routeParams.usercode == null) {
                    $log.error("usercode is missing", $routeParams);
                    throw "Cannot find user code";
                }
                return '#/' + $routeParams.usercode + '/' + $routeParams.projectcode;
            }

            var GetBaseUrlForUser = function () {
                if ($routeParams.usercode == null) {
                    $log.error("usercode is missing", $routeParams);
                    throw "Cannot find user code";
                }
                return '#/' + $routeParams.usercode;
            }

            //todo cip !!! handle this hard coded values
            var getCurrentUserLink = function () {
                return "/app/TheLoggedOne/";
            }

            var getProjectLink = function (projectCode) {
                return getCurrentUserLink() + projectCode;
            }

            var GetAbsolutePathForUserAndProject = function (relPath) {
                return GetBaseUrlForProject() + "/" + relPath;
            }
            var GetAbsolutePathForUser = function (relPath) {
               return GetBaseUrlForUser() + "/" + relPath;
            }
            var GetAbsolutePath = function (relPath) {
                if (CurrentView.IsUser())
                    return GetAbsolutePathForUser(relPath);
                else
                    return GetAbsolutePathForUserAndProject(relPath);
            }

            return {
                init: init,
                Links: currentLinks,
                LinksUser: currentLinksUser,
                LinksSettings: currentLinksSettings,
                //todo cip is it good to have switch here?
                SwitchToProject: function (projectCode) {
                    $window.location = getProjectLink(projectCode);
                },
                SwitchToUser: function () {
                    $window.location = getCurrentUserLink();
                },
                getCurrentUserLink: getCurrentUserLink,
                GetAbsolutePathForUserAndProject: GetAbsolutePathForUserAndProject,
                GetAbsolutePathForUser: GetAbsolutePathForUser,
                GetAbsolutePath: GetAbsolutePath
            };
        }
    ];

});
