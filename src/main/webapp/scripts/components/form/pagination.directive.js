/* globals $ */
'use strict';

angular.module('songbookApp')
    .directive('songbookAppPagination', function() {
        return {
            templateUrl: 'scripts/components/form/pagination.html'
        };
    });
