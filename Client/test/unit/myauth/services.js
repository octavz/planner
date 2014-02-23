//'use strict';

/* jasmine specs for services go here */

describe('myAuth services', function() {
    var auth;
    var accessRights;
    var currentUserSession;

    //excuted before each "it" is run.
    beforeEach(function() {

        //load the module.
        module('myAuth.services');

    });
    
    describe('Auth', function() {
        //excuted before each "it" is run.
        beforeEach(function() {

            //inject your service for testing.
            inject(function($injector) {
                auth = $injector.get('Auth');
                accessRights = $injector.get('AccessRights');
                currentUserSession = $injector.get('CurrentUserSession');
            });
        });

        //check to see if it has the expected function
        it('should have an exciteText function', function() {
            expect(angular.isFunction(auth.authorize)).toBe(true);
        });

        it('should allow when the rights are the same', function() {

            currentUserSession.setRights(accessRights.Public);
            var result = auth.authorize(accessRights.Public);
            expect(result).toBe(true);
        });

        it('should deny when the rights are different', function() {

            currentUserSession.setRights(accessRights.Public);
            var result = auth.authorize(accessRights.Registered);
            expect(result).toBe(false);
        });

        it('should allow when the resource is for public or register and the current is is public', function() {

            currentUserSession.setRights(accessRights.Public);
            var result = auth.authorize(accessRights.Public | accessRights.Registered);
            expect(result).toBe(true);
        });

        it('should allow when the resource is for Right1 and the current has Right1 but also others', function() {

            currentUserSession.setRights(accessRights.Right1 | accessRights.Right1);
            var result = auth.authorize(accessRights.Right1);
            expect(result).toBe(true);
        });

        it('should deny when the resource is for Right1 and the current has different rights', function() {

            currentUserSession.setRights(accessRights.Right2 | accessRights.Right3);
            var result = auth.authorize(accessRights.Right1);
            expect(result).toBe(false);
        });
    });
});
