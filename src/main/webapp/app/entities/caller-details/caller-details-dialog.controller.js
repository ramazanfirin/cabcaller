(function() {
    'use strict';

    angular
        .module('cabcallerApp')
        .controller('CallerDetailsDialogController', CallerDetailsDialogController);

    CallerDetailsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CallerDetails', 'Branch', 'Cabin', 'Stuff'];

    function CallerDetailsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CallerDetails, Branch, Cabin, Stuff) {
        var vm = this;

        vm.callerDetails = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.branches = Branch.query();
        vm.cabins = Cabin.query();
        vm.stuffs = Stuff.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.callerDetails.id !== null) {
                CallerDetails.update(vm.callerDetails, onSaveSuccess, onSaveError);
            } else {
                CallerDetails.save(vm.callerDetails, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('cabcallerApp:callerDetailsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.callDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
