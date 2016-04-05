/* globals $ */
'use strict';

angular.module('songbookApp')
    .directive('songbookAppPager', function() {
        return {
            templateUrl: 'scripts/components/form/pager.html'
        };
    });
