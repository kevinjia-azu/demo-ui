//控制层
app.controller('goodsController', function ($scope,$location, $controller, goodsService, uploadService,itemCatService,typeTemplateService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        goodsService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        goodsService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne = function () {
        var id = $location.search()['id'];
        if(id==null){
            return;
        }
        goodsService.findOne(id).success(
            function (response) {
                $scope.entity = response;
                //富文本编辑框回显
                editor.html($scope.entity.goodsDesc.introduction);
                //商品图片回显
                $scope.entity.goodsDesc.itemImages = JSON.parse($scope.entity.goodsDesc.itemImages);
                //商品扩展属性回显
                $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.entity.goodsDesc.customAttributeItems);
                //商品规格及规格选项回显
                $scope.entity.goodsDesc.specificationItems = JSON.parse($scope.entity.goodsDesc.specificationItems);
                //sku回显
                for(var i=0;i< $scope.entity.itemList.length;i++ ){
                    $scope.entity.itemList[i].spec= JSON.parse($scope.entity.itemList[i].spec);
                }
            }
        );
    }

    $scope.checkAttributeValue=function (specName,optionName) {
        var items = $scope.entity.goodsDesc.specificationItems;
        var object = $scope.searchObjectByKey(items,"attributeName",specName);
        if(object != null){
            if(object.attributeValue.indexOf(optionName)>=0){
                return true;
            }else{
                return false;
            }
        }else{
           return false;
        }

    };


    //保存
    $scope.save = function () {
        $scope.entity.goodsDesc.introduction = editor.html();

        var serviceObject;//服务层对象

        if ($scope.entity.goods.id != null) {//如果有ID
            serviceObject = goodsService.update($scope.entity); //修改
        } else {
            serviceObject = goodsService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.if_success) {
                    alert("保存成功")
                    //重新查询
                    location.href="goods.html";
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        goodsService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        goodsService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //新增商品
    $scope.add = function () {

        $scope.entity.goodsDesc.introduction = editor.html();
        goodsService.add($scope.entity).success(
            function (response) {
                if (response.if_success) {
                    alert("恭喜，已成功添加！");
                    $scope.entity = {};
                    editor.html("");//清空富文本编辑器
                } else {
                    alert(response.message);
                }

            }
        )
    }

    //上传图片
    $scope.uploadFile = function () {
        uploadService.uploadFile().success(
            function (response) {
                if (response.if_success) {
                    $scope.image_entity.url = response.message;
                } else {
                    alert(response.message);
                }
            }
        );
    }


    $scope.entity={ goodsDesc:{itemImages:[],specificationItems:[]} };

    //将当前上传的图片实体存入图片列表
    $scope.add_image_entity=function(){
        $scope.entity.goodsDesc.itemImages.push($scope.image_entity);
    }

    //移除图片
    $scope.remove_image_entity=function(index){
        $scope.entity.goodsDesc.itemImages.splice(index,1);
    }



    //通过parenId查询商品分类列表
    $scope.selectItemCat1List = function () {
        itemCatService.findByParentId(0).success(
            function (response) {
                $scope.itemCat1List=response;
            }
        );
    }

    //查询2级分类
    $scope.$watch('entity.goods.category1Id',function (newValue,oldValue) {
        itemCatService.findByParentId(newValue).success(
            function (response) {
                $scope.itemCat2List=response;
            }
        );
    })

    //查询3级分类
    $scope.$watch('entity.goods.category2Id',function (newValue,oldValue) {
        itemCatService.findByParentId(newValue).success(
            function (response) {
                $scope.itemCat3List=response;
            }
        );
    })

    //查询模板ID
    $scope.$watch('entity.goods.category3Id',function (newValue,oldValue) {
        itemCatService.findOne(newValue).success(
            function (response) {
                $scope.entity.goods.typeTemplateId = response.typeId;
            }
        );

    })

    //根据模板id查询品牌列表
    $scope.$watch('entity.goods.typeTemplateId',function (newValue,oldValue) {
        typeTemplateService.findOne(newValue).success(
            function (response) {
                //模板对象
                $scope.typeTemplate= response;
                //品牌列表
                $scope.typeTemplate.brandIds = JSON.parse( $scope.typeTemplate.brandIds);//将json字符串转换为json对象

                if($location.search()['id']==null){
                    //扩展属性
                    $scope.entity.goodsDesc.customAttributeItems=JSON.parse($scope.typeTemplate.customAttributeItems);
                }
            }
        );

        typeTemplateService.findSpecList(newValue).success(
            function (response) {
                $scope.specList=response;
            }
        );
    })


    $scope.updateSpecAttribute = function ($event,name,value) {
        var object = $scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems,'attributeName',name);
        if(object != null){
            if($event.target.checked){//勾选
                object.attributeValue.push(value);
            }else{//取消勾选
                //移除规格选项
                object.attributeValue.splice(object.attributeValue.indexOf(value),1);
                if(object.attributeValue.length == 0){
                    $scope.entity.goodsDesc.specificationItems.splice( $scope.entity.goodsDesc.specificationItems.indexOf( object),1);
                }
            }
        }else{
            $scope.entity.goodsDesc.specificationItems.push({'attributeName':name,'attributeValue':[value]});
        }
    }


    //生成sku列表
    $scope.creatItemList=function () {
        $scope.entity.itemList = [{spec:{},price:0,num:99999,status:'0',isDefault:'0'}];
        var items = $scope.entity.goodsDesc.specificationItems;
        for(var i = 0;i<items.length;i++){
            $scope.entity.itemList = addcolumn($scope.entity.itemList,items[i].attributeName,items[i].attributeValue);
        }
    }

    //添加列
    addcolumn = function (list,columnName,columnValue) {
        var newList=[];//新集合
        for(var i = 0;i<list.length;i++){
            var oldRow = list[i];
            for(var j =0;j<columnValue.length;j++){
                var newRow = JSON.parse(JSON.stringify(oldRow));
                newRow.spec[columnName] = columnValue[j];
                newList.push(newRow);
            }
        }
        return newList;
    }
    $scope.status = ['未审核','已审核','审核未通过','已关闭'];

    $scope.ItemCatList=[];
    $scope.findItemCatList= function () {
        itemCatService.findAll().success(
            function (response) {
                for(var i = 0;i<response.length;i++){
                    $scope.ItemCatList[response[i].id]=response[i].name;
                }

            }
        );
    }
});	
