(function() {
    'use strict';

    angular
        .module('cabcallerApp')
        .controller('CabinDialogController', CabinDialogController);

    CabinDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Cabin', 'CabinGroup'];

    function CabinDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Cabin, CabinGroup) {
        var vm = this;

        vm.cabin = entity;
        vm.clear = clear;
        vm.save = save;
        vm.cabingroups = CabinGroup.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.cabin.id !== null) {
                Cabin.update(vm.cabin, onSaveSuccess, onSaveError);
            } else {
                Cabin.save(vm.cabin, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('cabcallerApp:cabinUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
