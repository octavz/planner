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

        it('Should have only three inputs and the save button disabled', function () {
            expect(element('[ng-view] input').count())
                .toEqual(3);
            expect(element('button:disabled').count()).toBe(1);
        });

        it('Validation - submitting with no fields filled should remain in the same screen (with error)', function () {
            //arrange
            input('user.Email').enter('');
            input('user.Password').enter('');
            input('user.RePassword').enter('');

            //todo cip is it correct to make a pre-assert?
            //pre-assert
            expect(element('button:disabled').count()).toBe(1);

            //act
            element(':button').click();

            //assert
            expect(browser().location().url()).toBe("/Register");
        });

        it('Validation - submitting with all three fields filled should redirect to RegisterOk view', function () {
            //arrange
            input('user.Email').enter('NotExistingUser@NotExistingUser.com');
            input('user.Password').enter('AGoodPassword');
            input('user.RePassword').enter('AGoodPassword');

            //todo cip is it correct to make a pre-assert?
            //pre-assert
            expect(element('button:disabled').count()).toBe(0);

            //act
            element(':button').click();

            //assert
            expect(browser().location().url()).toBe("/RegisterOk");
        });

        it('Validation - submitting an invalid field should remain in the same screen (invalid email)', function () {
            //arrange
            input('user.Email').enter('InvaliEmail.com');
            input('user.Password').enter('AGoodPassword');
            input('user.RePassword').enter('AGoodPassword');

            //todo cip is it correct to make a pre-assert?
            //pre-assert
            expect(element('button:disabled').count()).toBe(1);

            //act
            element(':button').click();

            //assert
            expect(browser().location().url()).toBe("/Register");
        });

        it('Validation - submitting an invalid field should remain in the same screen (invalid password)', function () {
            //arrange
            input('user.Email').enter('NotExistingUser@NotExistingUser.com');
            input('user.Password').enter('1');
            input('user.RePassword').enter('AGoodPassword');

            //todo cip is it correct to make a pre-assert?
            //pre-assert
            expect(element('button:disabled').count()).toBe(1);

            //act
            element(':button').click();

            //assert
            expect(browser().location().url()).toBe("/Register");
        });

    });

});
