(function() {
    'use strict';

    angular
        .module('cabcallerApp')
        .controller('CabinController', CabinController);

    CabinController.$inject = ['Cabin'];

    function CabinController(Cabin) {

        var vm = this;

        vm.cabins = [];

        loadAll();

        function loadAll() {
            Cabin.query(function(result) {
                vm.cabins = result;
                vm.searchQuery = null;
            });
        }
    }
})();
