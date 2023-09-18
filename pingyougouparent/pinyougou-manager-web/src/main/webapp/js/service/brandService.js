app.service('brandService', function ($http) {

    // 条件查询 + 分页
    this.search = function (page, size, searchEntity) {
        return $http.post('../brand/search.do?page=' + page + '&size=' + size, searchEntity);
    }

    // 新增
    this.add = function (entity) {
        return $http.post('../brand/add.do', entity)
    }

    // 更新
    this.updata = function (entity) {
        return $http.post('../brand/update.do', entity)
    }

    // 根据商品id查询
    this.findOne = function (id) {
        return $http.get('../brand/findOne.do?id=' + id)
    }

    // 删除多个
    this.dele = function (selectIds) {
        return $http.get('../brand/delete.do?longs=' + selectIds);
    }

    //下拉列表
    this.selectOptionList= function () {
        return $http.post('../brand/selectOption.do')   

    }
});