(function() {
    'use strict';

    angular
        .module('cabcallerApp')
        .controller('CabinGroupUserController', CabinGroupUserController);

    CabinGroupUserController.$inject = ['CabinGroupUser'];

    function CabinGroupUserController(CabinGroupUser) {

        var vm = this;

        vm.cabinGroupUsers = [];

        loadAll();

        function loadAll() {
            CabinGroupUser.query(function(result) {
                vm.cabinGroupUsers = result;
                vm.searchQuery = null;
            });
        }
    }
})();
