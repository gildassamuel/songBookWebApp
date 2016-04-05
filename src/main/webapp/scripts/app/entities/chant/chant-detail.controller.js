'use strict';

angular.module('songbookApp')
    .controller('ChantDetailController', function ($scope, $rootScope, $stateParams, entity, Chant) {
        $scope.chant = entity;
        $scope.load = function (id) {
            Chant.get({id: id}, function(result) {
                $scope.chant = result;
            });
        };
        var unsubscribe = $rootScope.$on('songbookApp:chantUpdate', function(event, result) {
            $scope.chant = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
