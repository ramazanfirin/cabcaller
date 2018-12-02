(function() {
    'use strict';

    angular
        .module('cabcallerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cabin-group-user', {
            parent: 'entity',
            url: '/cabin-group-user',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cabcallerApp.cabinGroupUser.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cabin-group-user/cabin-group-users.html',
                    controller: 'CabinGroupUserController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cabinGroupUser');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('cabin-group-user-detail', {
            parent: 'cabin-group-user',
            url: '/cabin-group-user/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cabcallerApp.cabinGroupUser.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cabin-group-user/cabin-group-user-detail.html',
                    controller: 'CabinGroupUserDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cabinGroupUser');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CabinGroupUser', function($stateParams, CabinGroupUser) {
                    return CabinGroupUser.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'cabin-group-user',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('cabin-group-user-detail.edit', {
            parent: 'cabin-group-user-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cabin-group-user/cabin-group-user-dialog.html',
                    controller: 'CabinGroupUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CabinGroupUser', function(CabinGroupUser) {
                            return CabinGroupUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cabin-group-user.new', {
            parent: 'cabin-group-user',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cabin-group-user/cabin-group-user-dialog.html',
                    controller: 'CabinGroupUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                groupID: null,
                                userID: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cabin-group-user', null, { reload: 'cabin-group-user' });
                }, function() {
                    $state.go('cabin-group-user');
                });
            }]
        })
        .state('cabin-group-user.edit', {
            parent: 'cabin-group-user',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cabin-group-user/cabin-group-user-dialog.html',
                    controller: 'CabinGroupUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CabinGroupUser', function(CabinGroupUser) {
                            return CabinGroupUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cabin-group-user', null, { reload: 'cabin-group-user' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cabin-group-user.delete', {
            parent: 'cabin-group-user',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cabin-group-user/cabin-group-user-delete-dialog.html',
                    controller: 'CabinGroupUserDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CabinGroupUser', function(CabinGroupUser) {
                            return CabinGroupUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cabin-group-user', null, { reload: 'cabin-group-user' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
