(function() {
    'use strict';

    angular
        .module('cabcallerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cabin', {
            parent: 'entity',
            url: '/cabin',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cabcallerApp.cabin.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cabin/cabins.html',
                    controller: 'CabinController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cabin');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('cabin-detail', {
            parent: 'cabin',
            url: '/cabin/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cabcallerApp.cabin.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cabin/cabin-detail.html',
                    controller: 'CabinDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cabin');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Cabin', function($stateParams, Cabin) {
                    return Cabin.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'cabin',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('cabin-detail.edit', {
            parent: 'cabin-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cabin/cabin-dialog.html',
                    controller: 'CabinDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cabin', function(Cabin) {
                            return Cabin.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cabin.new', {
            parent: 'cabin',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cabin/cabin-dialog.html',
                    controller: 'CabinDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                cabNo: null,
                                buttonId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cabin', null, { reload: 'cabin' });
                }, function() {
                    $state.go('cabin');
                });
            }]
        })
        .state('cabin.edit', {
            parent: 'cabin',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cabin/cabin-dialog.html',
                    controller: 'CabinDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cabin', function(Cabin) {
                            return Cabin.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cabin', null, { reload: 'cabin' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cabin.delete', {
            parent: 'cabin',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cabin/cabin-delete-dialog.html',
                    controller: 'CabinDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Cabin', function(Cabin) {
                            return Cabin.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cabin', null, { reload: 'cabin' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
