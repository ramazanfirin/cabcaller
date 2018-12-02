'use strict';

describe('Controller Tests', function() {

    describe('CabinGroupUserHistory Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockCabinGroupUserHistory, MockCabinGroup, MockStuff;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockCabinGroupUserHistory = jasmine.createSpy('MockCabinGroupUserHistory');
            MockCabinGroup = jasmine.createSpy('MockCabinGroup');
            MockStuff = jasmine.createSpy('MockStuff');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'CabinGroupUserHistory': MockCabinGroupUserHistory,
                'CabinGroup': MockCabinGroup,
                'Stuff': MockStuff
            };
            createController = function() {
                $injector.get('$controller')("CabinGroupUserHistoryDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'cabcallerApp:cabinGroupUserHistoryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
