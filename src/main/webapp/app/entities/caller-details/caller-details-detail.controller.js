(function() {
    'use strict';

    angular
        .module('cabcallerApp')
        .controller('CallerDetailsDetailController', CallerDetailsDetailController);

    CallerDetailsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CallerDetails', 'Branch', 'Cabin', 'Stuff'];

    function CallerDetailsDetailController($scope, $rootScope, $stateParams, previousState, entity, CallerDetails, Branch, Cabin, Stuff) {
        var vm = this;

        vm.callerDetails = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cabcallerApp:callerDetailsUpdate', function(event, result) {
            vm.callerDetails = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
