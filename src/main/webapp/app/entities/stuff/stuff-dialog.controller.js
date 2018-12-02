(function() {
    'use strict';

    angular
        .module('cabcallerApp')
        .controller('StuffDialogController', StuffDialogController);

    StuffDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Stuff', 'Branch', 'Company', 'CabinGroup'];

    function StuffDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Stuff, Branch, Company, CabinGroup) {
        var vm = this;

        vm.stuff = entity;
        vm.clear = clear;
        vm.save = save;
        vm.branches = Branch.query();
        vm.companies = Company.query();
        vm.cabingroups = CabinGroup.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.stuff.id !== null) {
                Stuff.update(vm.stuff, onSaveSuccess, onSaveError);
            } else {
                Stuff.save(vm.stuff, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('cabcallerApp:stuffUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
