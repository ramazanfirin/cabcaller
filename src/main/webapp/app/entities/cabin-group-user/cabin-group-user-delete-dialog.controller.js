(function() {
    'use strict';

    angular
        .module('cabcallerApp')
        .controller('CabinGroupUserDeleteController',CabinGroupUserDeleteController);

    CabinGroupUserDeleteController.$inject = ['$uibModalInstance', 'entity', 'CabinGroupUser'];

    function CabinGroupUserDeleteController($uibModalInstance, entity, CabinGroupUser) {
        var vm = this;

        vm.cabinGroupUser = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CabinGroupUser.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
