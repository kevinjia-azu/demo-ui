//定义模块：
var app=angular.module('pinyougou',[]);

//定义过滤器：一个过滤器只做一件事，可根据需求创建多个过滤器
app.filter('trustHtml',function ($sce) {
    return function (data) {//传入参数表示被过滤的内容
        return $sce.trustAsHtml(data);//返回的是过滤后的内容（信任html的转换）
    }

});
