app.controller('brandController', function ($scope,brandService,$controller) {

   $controller('baseController',{$scope:$scope});//controller继承
    //保存
    $scope.save = function () {
        var serviceObject = null;
        if ($scope.entity.id != null) {
            serviceObject = brandService.updata($scope.entity);
        } else {
            serviceObject = brandService.add($scope.entity);
        }
        serviceObject.success(
            function (response) {
                if (response.if_success) {
                    alert(response.message)
                    $scope.reloadList();//刷新页面
                } else {
                    alert(response.message)
                }
            }
        )
    };

    //查询实体
    $scope.findOne = function (id) {
        brandService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }

    //删除
    $scope.dele = function () {
        if (confirm('确定要删除吗？')) {
            brandService.dele($scope.selectIds).success(
                function (response) {
                    if (response.if_success) {
                        alert(response.message);
                        $scope.reloadList();//刷新
                    } else {
                        alert(response.message);
                    }
                }
            );
        }

    }

    $scope.searchEntity = {};

    //条件查询
    $scope.search = function (page, size) {
        brandService.search(page, size, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;//显示当前页数据
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );

    }

});