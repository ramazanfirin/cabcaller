(function() {
    'use strict';

    angular
        .module('cabcallerApp')
        .controller('CallerDetailsDeleteController',CallerDetailsDeleteController);

    CallerDetailsDeleteController.$inject = ['$uibModalInstance', 'entity', 'CallerDetails'];

    function CallerDetailsDeleteController($uibModalInstance, entity, CallerDetails) {
        var vm = this;

        vm.callerDetails = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CallerDetails.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
