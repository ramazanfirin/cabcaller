(function() {
    'use strict';

    angular
        .module('cabcallerApp')
        .controller('CabinGroupUserHistoryDialogController', CabinGroupUserHistoryDialogController);

    CabinGroupUserHistoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CabinGroupUserHistory', 'CabinGroup', 'Stuff'];

    function CabinGroupUserHistoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CabinGroupUserHistory, CabinGroup, Stuff) {
        var vm = this;

        vm.cabinGroupUserHistory = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
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
            if (vm.cabinGroupUserHistory.id !== null) {
                CabinGroupUserHistory.update(vm.cabinGroupUserHistory, onSaveSuccess, onSaveError);
            } else {
                CabinGroupUserHistory.save(vm.cabinGroupUserHistory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('cabcallerApp:cabinGroupUserHistoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.actionDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
