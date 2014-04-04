(function () {
    'use strict';

    var uMod = angular.module('usersModule', []);

    uMod.directive('ngEnter', function () {
        return function (scope, element, attrs) {
            element.bind("keydown keypress", function (event) {
                if(event.which === 13) {
                    scope.$apply(function (){
                        scope.$eval(attrs.ngEnter);
                    });

                    event.preventDefault();
                }
            });
        };
    });

    uMod.controller('UsersCtrl', function ($scope, $filter) {

        $scope.gap = 5;

        $scope.filteredUsers = [];
        $scope.groupedUsers = [];
        $scope.usersPerPage = 10;
        $scope.pagedUsers = [];
        $scope.currentPage = 0;
        $scope.users = vlacsuserdata; //User data here

        var searchMatch = function (haystack, needle) {
            var hIsNum = false;
            var nIsNum = false;

            if (!needle)
                return true;
            if (parseInt(haystack, 10) > 0)
                hIsNum = true;
            if (parseInt(needle, 10) > 0)
                nIsNum = true;
            if (!hIsNum && nIsNum)
                return false;
            if (!nIsNum && hIsNum)
                return false;

            if (hIsNum) {
                if (haystack == needle)
                    return true;
                return false;
            }
            else {
                if (haystack.toLowerCase().indexOf(needle.toLowerCase()) != -1)
                    return true;
                return false;
            };
        };

        $scope.search = function () {
            $scope.filteredUsers = $filter('filter')($scope.users, function (user) {
                if (!$scope.query) {
                    return true;
                }
                var searchFields = [user.id, user.username, user.firstname, user.lastname, user.privilege];
                var splitQuery = $scope.query.split(" ");
                var match = 0;

                for (var q in splitQuery) {
                    for (var field in searchFields) {
                        if (searchMatch(searchFields[field], splitQuery[q]))
                            ++match;
                    }
                }
                if (match >= splitQuery.length)
                    return true;
                return false;
            });
            $scope.currentPage = 0;
            $scope.groupToPages();
        };

        $scope.groupToPages = function () {
            $scope.pagedUsers = [];

            for (var i = 0; i < $scope.filteredUsers.length; i++) {
                if (i % $scope.usersPerPage === 0) {
                    $scope.pagedUsers[Math.floor(i / $scope.usersPerPage)] = [ $scope.filteredUsers[i] ];
                } else {
                    $scope.pagedUsers[Math.floor(i / $scope.usersPerPage)].push($scope.filteredUsers[i]);
                }
            }
        };

        $scope.range = function (size,start, end) {
            var ret = [];

            if (size < $scope.gap) {
                $scope.gap = size;
            }
            else {
                $scope.gap = 5;
            }


            if (size < end) {
                end = size;
                start = size-$scope.gap;
            }
            for (var i = start; i < end; i++) {
                ret.push(i);
            }
            return ret;
        };

        $scope.prevPage = function () {
            if ($scope.currentPage > 0) {
                $scope.currentPage--;
            }
        };

        $scope.nextPage = function () {
            if ($scope.currentPage < $scope.pagedUsers.length - 1) {
                $scope.currentPage++;
            }
        };

        $scope.setPage = function () {
            $scope.currentPage = this.n;
        };

        $scope.showingFrom = function () {
            if ($scope.filteredUsers.length == 0)
                return 0;
            return ($scope.currentPage * $scope.usersPerPage) + 1;
        };

        $scope.showingTo = function () {
            if ($scope.filteredUsers.length == 0)
                return 0;
            if (($scope.currentPage + 1) == $scope.pagedUsers.length)
                return $scope.filteredUsers.length;
            return ($scope.currentPage + 1) * $scope.usersPerPage;
        };

        $scope.runSearch = function () {
            $scope.query = $scope.userQuery;
            $scope.search();
        };

        $scope.search();
    });

    //uMod.$inject = ['$scope', '$filter'];

}());
