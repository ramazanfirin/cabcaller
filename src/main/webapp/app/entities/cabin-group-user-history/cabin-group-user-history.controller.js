(function() {
    'use strict';

    angular
        .module('cabcallerApp')
        .controller('CabinGroupUserHistoryController', CabinGroupUserHistoryController);

    CabinGroupUserHistoryController.$inject = ['CabinGroupUserHistory'];

    function CabinGroupUserHistoryController(CabinGroupUserHistory) {

        var vm = this;

        vm.cabinGroupUserHistories = [];

        loadAll();

        function loadAll() {
            CabinGroupUserHistory.query(function(result) {
                vm.cabinGroupUserHistories = result;
                vm.searchQuery = null;
            });
        }
    }
})();
