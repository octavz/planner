'use strict';

/* Services */

angular.module('myApp.services')

.provider('SiteMap', function () {
    var currentLinks = { items: [] };

    var registeredLinks = [];

    this.RegisterLinks = function (links) {
        registeredLinks = registeredLinks.concat(links);
    }

    this.$get = [
        '$q', 'RouteAccessApi', '$log',
        function ($q, RouteAccessApi, $log) {

            var calculateAllowedMenuLinks = function (allLinks, allowedRoutes) {
                var filtererLinks = _(allLinks).filter(function (linkItem) {
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

                return RouteAccessApi.get().$promise
                    .then(function (res) {
                        $log.debug("RouteAccessApi", res);
                        currentLinks.items = currentLinks.items.concat(calculateAllowedMenuLinks(registeredLinks, res.data));
                    });
            };

            return {
                init: init,
                Links: currentLinks
            };
        }
    ];

});
