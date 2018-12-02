'use strict';

describe('Controller Tests', function() {

    describe('CallerDetails Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockCallerDetails, MockBranch, MockCabin, MockStuff;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockCallerDetails = jasmine.createSpy('MockCallerDetails');
            MockBranch = jasmine.createSpy('MockBranch');
            MockCabin = jasmine.createSpy('MockCabin');
            MockStuff = jasmine.createSpy('MockStuff');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'CallerDetails': MockCallerDetails,
                'Branch': MockBranch,
                'Cabin': MockCabin,
                'Stuff': MockStuff
            };
            createController = function() {
                $injector.get('$controller')("CallerDetailsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'cabcallerApp:callerDetailsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
