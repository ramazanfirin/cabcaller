(function() {
    'use strict';
    angular
        .module('cabcallerApp')
        .factory('CabinGroup', CabinGroup);

    CabinGroup.$inject = ['$resource'];

    function CabinGroup ($resource) {
        var resourceUrl =  'api/cabin-groups/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
