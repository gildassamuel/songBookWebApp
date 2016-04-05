 'use strict';

angular.module('songbookApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-songbookApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-songbookApp-params')});
                }
                return response;
            }
        };
    });
