'use strict';

/* Services */

angular.module('myApp.services')

.factory('SiteMap', ['$q', 'RouteAccessApi',
    function($q, RouteAccessApi) {
        var links = [{
            'title': 'Home',
            'link': '#/Home'
        }, {
            'title': 'Projects',
            'link': '#/Projects'
        }, {
            'title': 'New Project',
            'link': '#/ProjectNew'
        }, {
            'title': 'Tasks',
            'link': '#/Tasks'
        }, {
            'title': 'New Task',
            'link': '#/TaskNew'
        }, {
            'title': 'Other',
            'link': '#/Other'
        }];

        var calculateAllowedMenuLinks = function(allLinks, allowedRoutes) {
            var filtererLinks = _(allLinks).filter(function(linkItem) {
                var lnk = linkItem.link;
                if (lnk[0] == '#')
                    lnk = lnk.substr(1, lnk.length - 1);
                var lnkHashed = calcMD5(lnk);
                var ret = _(allowedRoutes).contains(lnkHashed);
                return ret;
            })
            console.log("calculateAllowedMenuLinks",filtererLinks);
            return filtererLinks;
        };

        var getLinks = function() {

            return RouteAccessApi.get().$promise
                .then(function(res) {
                    console.log("RouteAccessApi", res);
                    return calculateAllowedMenuLinks(links, res.data);
                });
        };

        return {
            getLinks: getLinks
        };
    }
]);
