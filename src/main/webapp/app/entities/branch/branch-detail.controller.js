(function() {
    'use strict';

    angular
        .module('cabcallerApp')
        .controller('BranchDetailController', BranchDetailController);

    BranchDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Branch', 'Company'];

    function BranchDetailController($scope, $rootScope, $stateParams, previousState, entity, Branch, Company) {
        var vm = this;

        vm.branch = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cabcallerApp:branchUpdate', function(event, result) {
            vm.branch = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
