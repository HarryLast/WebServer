let tagsApp = new Vue({
    el:'#tagsApp',
    data:{
        tags:[]
    },
    methods:{
        loadTags:function () {
            console.log('执行了 loadTags');
            $.ajax({
                url:'/v1/tags',
                method:'GET',
                success:function (r) {
                    console.log(r);
                    if (r.code === OK){
                        console.log('成功获取tags');
                        tagsApp.tags = r.data;
                    }
                }
            });
        }
    },
    created:function () {
        this.loadTags();
    }
});