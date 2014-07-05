'use strict';

/* Services */

angular.module('myApp.services')

.provider('SiteMap', function () {
    var currentLinks = { items: [] };
    var currentLinksUser = { items: [] };
    var currentLinksSettings = { items: [] };

    var registeredLinks = [];
    var registeredLinksUser = [];
    var registeredLinksSettings = [];

    this.RegisterLinks = function (links) {
        registeredLinks = registeredLinks.concat(links);
    }

    this.RegisterLinksUser = function (links) {
        registeredLinksUser = registeredLinksUser.concat(links);
    }

    this.RegisterLinksSettings = function (links) {
        registeredLinksSettings = registeredLinksSettings.concat(links);
    }

    //todo cip !!! handle this hard coded values
    this.GetBaseUrlForProject = function () {
        return '#/User1/Project1';
    }
    this.GetBaseRouteUrlForProject = function () {
        return '/:usercode/:projectcode';
    }
    this.GetBaseUrlForUser = function () {
        return '#/User1';
    }
    this.GetBaseRouteUrlForUser = function () {
        return '/:usercode';
    }

    this.$get = [
        '$q', 'RouteAccessApi', '$log', '$window',
        function ($q, RouteAccessApi, $log, $window) {

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
                        currentLinks.items = currentLinks.items.concat(calculateAllowedMenuLinks(registeredLinks, res.data));
                        currentLinksUser.items = registeredLinksUser;
                        currentLinksSettings.items = registeredLinksSettings;
                    });
            };

            //todo cip !!! handle this hard coded values
            var getCurrentUserLink = function () {
                return "/app/User1/";
            }
            var getProjectLink = function (projectCode) {
                return getCurrentUserLink() + projectCode;
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
                getCurrentUserLink: getCurrentUserLink
            };
        }
    ];

});
