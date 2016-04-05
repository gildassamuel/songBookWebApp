'use strict';

angular.module('songbookApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('chant', {
                parent: 'entity',
                url: '/chants',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'songbookApp.chant.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/chant/chants.html',
                        controller: 'ChantController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('chant');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('chant.detail', {
                parent: 'entity',
                url: '/chant/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'songbookApp.chant.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/chant/chant-detail.html',
                        controller: 'ChantDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('chant');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Chant', function($stateParams, Chant) {
                        return Chant.get({id : $stateParams.id});
                    }]
                }
            })
            .state('chant.new', {
                parent: 'chant',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/chant/chant-dialog.html',
                        controller: 'ChantDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    number: null,
                                    lyric: null,
                                    image: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('chant', null, { reload: true });
                    }, function() {
                        $state.go('chant');
                    })
                }]
            })
            .state('chant.edit', {
                parent: 'chant',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/chant/chant-dialog.html',
                        controller: 'ChantDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Chant', function(Chant) {
                                return Chant.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('chant', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
