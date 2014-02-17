'use strict';

/* Directives */


angular.module('myApp.directives', [])
    .directive('appVersion', ['version', function (version) {
        return function (scope, elm, attrs) {
            elm.text(version);
        };
    }])

/**
 * based on http://ericpanorel.net/2013/10/05/angularjs-password-match-form-validation/
 */

    .directive('pwCheck', [function () {

        return {
            require: 'ngModel',
            link: function (scope, elem, attrs, ctrl) {

                var firstPassword = '#' + attrs.pwCheck;
                elem.add(firstPassword).on('keyup', function () {
                    scope.$apply(function () {
                        var v = elem.val() === $(firstPassword).val();
                        ctrl.$setValidity('pwmatch', v);
                    });
                });
            }
        };
    }]);
