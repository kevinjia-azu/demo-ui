app.controller('searchController',function ($scope,searchService) {

    //搜索
    $scope.search = function () {
        searchService.search($scope.searchMap).success(
            function (response) {
                $scope.resultMap = response;
            }
        );
    };

    $scope.searchMap = {'keywords':'','category':'','brand':'','spec':{}};

    //添加筛选条件
    $scope.addSearchItem=function (key,value) {
        if(key == 'category' || key == 'brand'){//如果用户点击的是分类或者品牌
            $scope.searchMap[key]==value;
        }else{
            $scope.searchMap.spec[key]==value;
        }
    }

});