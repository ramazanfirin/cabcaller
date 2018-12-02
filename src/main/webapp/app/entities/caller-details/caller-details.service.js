(function() {
    'use strict';
    angular
        .module('cabcallerApp')
        .factory('CallerDetails', CallerDetails);

    CallerDetails.$inject = ['$resource', 'DateUtils'];

    function CallerDetails ($resource, DateUtils) {
        var resourceUrl =  'api/caller-details/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.callDate = DateUtils.convertLocalDateFromServer(data.callDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.callDate = DateUtils.convertLocalDateToServer(copy.callDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.callDate = DateUtils.convertLocalDateToServer(copy.callDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
