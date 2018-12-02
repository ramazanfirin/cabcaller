(function() {
    'use strict';

    angular
        .module('cabcallerApp')
        .controller('CabinGroupUserDialogController', CabinGroupUserDialogController);

    CabinGroupUserDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CabinGroupUser', 'CabinGroup', 'Stuff'];

    function CabinGroupUserDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CabinGroupUser, CabinGroup, Stuff) {
        var vm = this;

        vm.cabinGroupUser = entity;
        vm.clear = clear;
        vm.save = save;
        vm.cabingroups = CabinGroup.query();
        vm.stuffs = Stuff.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.cabinGroupUser.id !== null) {
                CabinGroupUser.update(vm.cabinGroupUser, onSaveSuccess, onSaveError);
            } else {
                CabinGroupUser.save(vm.cabinGroupUser, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('cabcallerApp:cabinGroupUserUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
