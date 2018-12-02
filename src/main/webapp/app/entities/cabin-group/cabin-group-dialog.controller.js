(function() {
    'use strict';

    angular
        .module('cabcallerApp')
        .controller('CabinGroupDialogController', CabinGroupDialogController);

    CabinGroupDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CabinGroup', 'Branch'];

    function CabinGroupDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CabinGroup, Branch) {
        var vm = this;

        vm.cabinGroup = entity;
        vm.clear = clear;
        vm.save = save;
        vm.branches = Branch.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.cabinGroup.id !== null) {
                CabinGroup.update(vm.cabinGroup, onSaveSuccess, onSaveError);
            } else {
                CabinGroup.save(vm.cabinGroup, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('cabcallerApp:cabinGroupUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
