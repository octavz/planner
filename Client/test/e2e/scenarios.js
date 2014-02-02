'use strict';

/* http://docs.angularjs.org/guide/dev_guide.e2e-testing */

describe('my app', function () {

    beforeEach(function () {
        browser().navigateTo('../../app/index.html');
    });


    it('should automatically redirect to /Register when location hash/fragment is empty', function () {
        expect(browser().location().url()).toBe("/Register");
    });

    describe('Register', function () {

        beforeEach(function () {
            browser().navigateTo('#/Register');
        });

        it('should render Register when user navigates to /Register', function () {
            expect(element('[ng-view] .Register').count())
                .toEqual(1);
        });

        it('Should have only three inputs and the error message hidden', function () {
            expect(element('[ng-view] input').count())
                .toEqual(3);
            expect(element('[ng-view] div.ErrorMessageDiv:hidden').count())
                .toEqual(1);
        });

        it('Validation - submitting with no fields filled should give an error', function () {
            //arrange
            input('user.Email').enter('');
            input('user.Password').enter('');
            input('user.RePassword').enter('');

            //act
            element(':button').click();

            //assert
            expect(element('[ng-view] div.ErrorMessageDiv:hidden').count())
                .toEqual(0);
        });

        it('Validation - submitting with all three fields filled should redirect to RegisterOk view', function () {
            //arrange
            input('user.Email').enter('NotExistingUser@NotExistingUser.com');
            input('user.Password').enter('AGoodPassword');
            input('user.RePassword').enter('AGoodPassword');

            //act
            element(':button').click();

            //assert
            expect(browser().location().url()).toBe("/RegisterOk");
        });
    });

});
