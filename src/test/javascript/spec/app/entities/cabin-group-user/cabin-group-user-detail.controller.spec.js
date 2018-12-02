'use strict';

describe('Controller Tests', function() {

    describe('CabinGroupUser Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockCabinGroupUser, MockCabinGroup, MockStuff;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockCabinGroupUser = jasmine.createSpy('MockCabinGroupUser');
            MockCabinGroup = jasmine.createSpy('MockCabinGroup');
            MockStuff = jasmine.createSpy('MockStuff');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'CabinGroupUser': MockCabinGroupUser,
                'CabinGroup': MockCabinGroup,
                'Stuff': MockStuff
            };
            createController = function() {
                $injector.get('$controller')("CabinGroupUserDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'cabcallerApp:cabinGroupUserUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
