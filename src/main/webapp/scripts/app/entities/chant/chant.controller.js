'use strict';

angular.module('songbookApp')
    .controller('ChantController', function ($scope, Chant, ParseLinks) {
        $scope.chants = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Chant.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.chants = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Chant.get({id: id}, function(result) {
                $scope.chant = result;
                $('#deleteChantConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Chant.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteChantConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.chant = {
                name: null,
                number: null,
                lyric: null,
                image: null,
                id: null
            };
        };
    });
