//'use strict';

/* jasmine specs for services go here */

describe('myAuth services', function() {
    //excuted before each "it" is run.
    beforeEach(function() {

        //load the module.
        module('myAuth.services');

    });

    describe('Auth', function() {

        var auth;
        var cookies;
        var authConstants;

        //excuted before each "it" is run.
        beforeEach(function() {

            //inject your service for testing.
            inject(function($injector) {
                auth = $injector.get('Auth');
                cookies = $injector.get('$cookies');
                authConstants = $injector.get('AuthConstants');
            });
        });

        it('when login a user, the isLoggedIn property shouyld be true, when is logged out it should be false; initial should be false', function() {
            expect(auth.isLoggedIn()).toBe(false);
            cookies[authConstants.AuthCookieName] = "something";
            expect(auth.isLoggedIn()).toBe(true);
            auth.logout();
            expect(auth.isLoggedIn()).toBe(false);
        });

    });

});
