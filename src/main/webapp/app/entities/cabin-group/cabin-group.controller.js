(function() {
    'use strict';

    angular
        .module('cabcallerApp')
        .controller('CabinGroupController', CabinGroupController);

    CabinGroupController.$inject = ['CabinGroup'];

    function CabinGroupController(CabinGroup) {

        var vm = this;

        vm.cabinGroups = [];

        loadAll();

        function loadAll() {
            CabinGroup.query(function(result) {
                vm.cabinGroups = result;
                vm.searchQuery = null;
            });
        }
    }
})();
