(function() {
    'use strict';

    angular
        .module('cabcallerApp')
        .controller('CabinGroupDeleteController',CabinGroupDeleteController);

    CabinGroupDeleteController.$inject = ['$uibModalInstance', 'entity', 'CabinGroup'];

    function CabinGroupDeleteController($uibModalInstance, entity, CabinGroup) {
        var vm = this;

        vm.cabinGroup = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CabinGroup.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
