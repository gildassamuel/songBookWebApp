'use strict';

angular.module('songbookApp').controller('ChantDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Chant',
        function($scope, $stateParams, $modalInstance, entity, Chant) {

        $scope.chant = entity;
        $scope.load = function(id) {
            Chant.get({id : id}, function(result) {
                $scope.chant = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('songbookApp:chantUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.chant.id != null) {
                Chant.update($scope.chant, onSaveFinished);
            } else {
                Chant.save($scope.chant, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
