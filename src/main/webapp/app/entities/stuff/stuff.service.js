(function() {
    'use strict';
    angular
        .module('cabcallerApp')
        .factory('Stuff', Stuff);

    Stuff.$inject = ['$resource'];

    function Stuff ($resource) {
        var resourceUrl =  'api/stuffs/:id';

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
