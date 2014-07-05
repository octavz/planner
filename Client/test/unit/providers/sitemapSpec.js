//'use strict';

describe('Providers', function () {
    //excuted before each "it" is run.
    beforeEach(function () {

        //load the module.
        module('myApp.services');
        //todo cip why to load also this??
        module('myAuth.services');

    });

    describe('SiteMap', function () {
        var siteMapProvider;
        var siteMap;
        //var routeAccessApi;
        var $httpBackend;

        var routes = {
            data: [
                //todo cip what is this?
                calcMD5("/Home"),

                calcMD5("/:usercode"),
                calcMD5("/:usercode/Projects"),
                calcMD5("/:usercode/ProjectNew"),

                calcMD5("/:usercode/:projectcode"),
                calcMD5("/:usercode/:projectcode/Edit"),
                calcMD5("/:usercode/:projectcode/Tasks"),
                calcMD5("/:usercode/:projectcode/TaskNew")
            ]
        };

        beforeEach(module('myApp.services', function (SiteMapProvider) {
            siteMapProvider = SiteMapProvider;
            
        }));

        beforeEach(inject(function ($injector) {
            siteMap = $injector.get('SiteMap');
            //routeAccessApi = $injector.get('RouteAccessApi');
            $httpBackend = $injector.get('$httpBackend');
            //$httpBackend.when('GET', '/routes').respond({ userId: 'userX' }, { 'A-Token': 'xxx' });
            $httpBackend.whenGET(/routes/).respond(routes);
        }));

        it('registering links', function () {

            //Arrange
            var links = [
                {
                    'title': 'Page 1',
                    'link': 'Page1'
                }, {
                    'title': 'Page 2',
                    'link': 'Page2'
                }
            ];

            //Act
            siteMapProvider.RegisterLinks(links);
            siteMap.init();
            $httpBackend.flush();

            //Assert
            expect(siteMap.Links).toBeTruthy();
            expect(siteMap.Links.items).toBeTruthy();
            expect(siteMap.Links.items).toEqual(links);
        });

        afterEach(function () {
            $httpBackend.verifyNoOutstandingExpectation();
            $httpBackend.verifyNoOutstandingRequest();
        });

        it('registering link hierarchy', function () {

            //Arrange
            var links = [
                {
                    'title': 'Page 1',
                    'items': [
                        {
                            'title': 'SubPage 3',
                            'link': 'Subpage3'
                        }, {
                            'title': 'SubPage 4',
                            'link': 'Subpage4'
                        }
                    ]
                }, {
                    'title': 'Page 2',
                    'link': 'Page2'
                }
            ];

            //Act
            siteMapProvider.RegisterLinks(links);
            siteMap.init();
            $httpBackend.flush();

            //Assert
            expect(siteMap.Links).toBeTruthy();
            expect(siteMap.Links.items).toBeTruthy();
            expect(siteMap.Links.items).toEqual(links);
        });

    });

});
