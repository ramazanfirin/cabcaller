(function() {
    'use strict';

    angular
        .module('cabcallerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cabin-group-user-history', {
            parent: 'entity',
            url: '/cabin-group-user-history',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cabcallerApp.cabinGroupUserHistory.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cabin-group-user-history/cabin-group-user-histories.html',
                    controller: 'CabinGroupUserHistoryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cabinGroupUserHistory');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('cabin-group-user-history-detail', {
            parent: 'cabin-group-user-history',
            url: '/cabin-group-user-history/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cabcallerApp.cabinGroupUserHistory.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cabin-group-user-history/cabin-group-user-history-detail.html',
                    controller: 'CabinGroupUserHistoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cabinGroupUserHistory');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CabinGroupUserHistory', function($stateParams, CabinGroupUserHistory) {
                    return CabinGroupUserHistory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'cabin-group-user-history',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('cabin-group-user-history-detail.edit', {
            parent: 'cabin-group-user-history-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cabin-group-user-history/cabin-group-user-history-dialog.html',
                    controller: 'CabinGroupUserHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CabinGroupUserHistory', function(CabinGroupUserHistory) {
                            return CabinGroupUserHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cabin-group-user-history.new', {
            parent: 'cabin-group-user-history',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cabin-group-user-history/cabin-group-user-history-dialog.html',
                    controller: 'CabinGroupUserHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                groupID: null,
                                userID: null,
                                actionDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cabin-group-user-history', null, { reload: 'cabin-group-user-history' });
                }, function() {
                    $state.go('cabin-group-user-history');
                });
            }]
        })
        .state('cabin-group-user-history.edit', {
            parent: 'cabin-group-user-history',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cabin-group-user-history/cabin-group-user-history-dialog.html',
                    controller: 'CabinGroupUserHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CabinGroupUserHistory', function(CabinGroupUserHistory) {
                            return CabinGroupUserHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cabin-group-user-history', null, { reload: 'cabin-group-user-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cabin-group-user-history.delete', {
            parent: 'cabin-group-user-history',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cabin-group-user-history/cabin-group-user-history-delete-dialog.html',
                    controller: 'CabinGroupUserHistoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CabinGroupUserHistory', function(CabinGroupUserHistory) {
                            return CabinGroupUserHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cabin-group-user-history', null, { reload: 'cabin-group-user-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
