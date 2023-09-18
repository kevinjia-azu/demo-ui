app.controller("baseController",function ($scope) {

    //分页控件配置currentPage:当前页   totalItems :总记录数  itemsPerPage:每页记录数  perPageOptions :分页选项  onChange:当页码变更后自动触发的方法
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function () {
            $scope.reloadList();
        }
    };

//刷新列表
    $scope.reloadList = function () {
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    }

    $scope.selectIds = [];//用户勾选的ID集合

    //用户勾选复选框
    $scope.updateSelection = function ($event, id) {
        if ($event.target.checked) {//$event.target就是获取当前的 input checkbox对象，通过checked属性可以判断true为已勾选
            $scope.selectIds.push(id);//push向集合添加元素
        } else {
            var index = $scope.selectIds.indexOf(id);//查找值的 位置
            $scope.selectIds.splice(index, 1);//参数1：移除的位置 参数2：移除的个数
        }
    }

    //字符串转换json
    $scope.jsonToString=function (jsonString,key) {
        var json = JSON.parse(jsonString);//将json字符串格式转换为json对象
        var values="";
        for(var i = 0 ;i<json.length;i++){
            if (i>0){
                values += ",";
            }
            values += json[i][key];//如果key值是变量，就不能直接 json对象.key,因为json数据中的key值都是带引号的
        }
        return values;
    }

});
