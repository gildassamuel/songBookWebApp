'use strict';

angular.module('songbookApp')
    .factory('Chant', function ($resource, DateUtils) {
        return $resource('api/chants/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
