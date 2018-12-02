(function() {
    'use strict';

    angular
        .module('cabcallerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('caller-details', {
            parent: 'entity',
            url: '/caller-details?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cabcallerApp.callerDetails.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/caller-details/caller-details.html',
                    controller: 'CallerDetailsController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('callerDetails');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('caller-details-detail', {
            parent: 'caller-details',
            url: '/caller-details/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cabcallerApp.callerDetails.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/caller-details/caller-details-detail.html',
                    controller: 'CallerDetailsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('callerDetails');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CallerDetails', function($stateParams, CallerDetails) {
                    return CallerDetails.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'caller-details',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('caller-details-detail.edit', {
            parent: 'caller-details-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/caller-details/caller-details-dialog.html',
                    controller: 'CallerDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CallerDetails', function(CallerDetails) {
                            return CallerDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('caller-details.new', {
            parent: 'caller-details',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/caller-details/caller-details-dialog.html',
                    controller: 'CallerDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                callDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('caller-details', null, { reload: 'caller-details' });
                }, function() {
                    $state.go('caller-details');
                });
            }]
        })
        .state('caller-details.edit', {
            parent: 'caller-details',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/caller-details/caller-details-dialog.html',
                    controller: 'CallerDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CallerDetails', function(CallerDetails) {
                            return CallerDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('caller-details', null, { reload: 'caller-details' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('caller-details.delete', {
            parent: 'caller-details',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/caller-details/caller-details-delete-dialog.html',
                    controller: 'CallerDetailsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CallerDetails', function(CallerDetails) {
                            return CallerDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('caller-details', null, { reload: 'caller-details' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
