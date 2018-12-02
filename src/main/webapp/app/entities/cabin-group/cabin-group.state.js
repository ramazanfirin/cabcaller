(function() {
    'use strict';

    angular
        .module('cabcallerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cabin-group', {
            parent: 'entity',
            url: '/cabin-group',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cabcallerApp.cabinGroup.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cabin-group/cabin-groups.html',
                    controller: 'CabinGroupController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cabinGroup');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('cabin-group-detail', {
            parent: 'cabin-group',
            url: '/cabin-group/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cabcallerApp.cabinGroup.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cabin-group/cabin-group-detail.html',
                    controller: 'CabinGroupDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cabinGroup');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CabinGroup', function($stateParams, CabinGroup) {
                    return CabinGroup.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'cabin-group',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('cabin-group-detail.edit', {
            parent: 'cabin-group-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cabin-group/cabin-group-dialog.html',
                    controller: 'CabinGroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CabinGroup', function(CabinGroup) {
                            return CabinGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cabin-group.new', {
            parent: 'cabin-group',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cabin-group/cabin-group-dialog.html',
                    controller: 'CabinGroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cabin-group', null, { reload: 'cabin-group' });
                }, function() {
                    $state.go('cabin-group');
                });
            }]
        })
        .state('cabin-group.edit', {
            parent: 'cabin-group',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cabin-group/cabin-group-dialog.html',
                    controller: 'CabinGroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CabinGroup', function(CabinGroup) {
                            return CabinGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cabin-group', null, { reload: 'cabin-group' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cabin-group.delete', {
            parent: 'cabin-group',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cabin-group/cabin-group-delete-dialog.html',
                    controller: 'CabinGroupDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CabinGroup', function(CabinGroup) {
                            return CabinGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cabin-group', null, { reload: 'cabin-group' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
