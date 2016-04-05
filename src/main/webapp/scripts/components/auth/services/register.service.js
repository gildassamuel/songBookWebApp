'use strict';

angular.module('songbookApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


