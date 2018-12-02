(function() {
    'use strict';

    angular
        .module('cabcallerApp')
        .controller('CabinGroupUserHistoryDeleteController',CabinGroupUserHistoryDeleteController);

    CabinGroupUserHistoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'CabinGroupUserHistory'];

    function CabinGroupUserHistoryDeleteController($uibModalInstance, entity, CabinGroupUserHistory) {
        var vm = this;

        vm.cabinGroupUserHistory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CabinGroupUserHistory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
