// reference the saved controller provider
app.lazy.controller('ItemsController', function($scope, $route) {

	//see http://jsfiddle.net/5FUQa/1/
	//see http://stackoverflow.com/questions/13153121/how-to-defer-routes-definition-in-angular-js
    function addRoute(path, route) {
        $route.routes[path] = angular.extend({
                reloadOnSearch: true
            },
            route,
            path && pathRegExp(path, route));

        // create redirection for trailing slashes
        if (path) {
            var redirectPath = (path[path.length - 1] == '/') ? path.substr(0, path.length - 1) : path + '/';

            $route.routes[redirectPath] = angular.extend({
                    redirectTo: path
                },
                pathRegExp(redirectPath, route));
        }

        return this;
    };

    function pathRegExp(path, opts) {
        var insensitive = opts.caseInsensitiveMatch,
            ret = {
                originalPath: path,
                regexp: path
            },
            keys = ret.keys = [];

        path = path.replace(/([().])/g, '\\$1')
            .replace(/(\/)?:(\w+)([\?\*])?/g, function(_, slash, key, option) {
                var optional = option === '?' ? option : null;
                var star = option === '*' ? option : null;
                keys.push({
                    name: key,
                    optional: !! optional
                });
                slash = slash || '';
                return '' + (optional ? '' : slash) + '(?:' + (optional ? slash : '') + (star && '(.+?)' || '([^/]+)') + (optional || '') + ')' + (optional || '');
            })
            .replace(/([\/$\*])/g, '\\$1');

        ret.regexp = new RegExp('^' + path + '$', insensitive ? 'i' : '');
        return ret;
    }
    $scope.defineRoute = function() {
        addRoute('/dynamic', {
            templateUrl: 'plugins/tasks/html/dynamic.tpl.html'
        });
        console.log("new route added",$route);
    };
});
