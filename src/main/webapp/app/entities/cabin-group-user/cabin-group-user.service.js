(function() {
    'use strict';
    angular
        .module('cabcallerApp')
        .factory('CabinGroupUser', CabinGroupUser);

    CabinGroupUser.$inject = ['$resource'];

    function CabinGroupUser ($resource) {
        var resourceUrl =  'api/cabin-group-users/:id';

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
