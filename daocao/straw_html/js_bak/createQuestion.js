Vue.component('v-select', VueSelect.VueSelect);
let createQuestionApp = new Vue({
    el:'#createQuestionApp',
    data:{
        title:'',
        selectedTags:[],
        tags:[],
        selectedTeachers:[],
        teachers:[]
    },
    methods:{
        createQuestion:function(){
            let content = $('#summernote').val();
            console.log(content);
            //data 对象，与服务器端QuestionVo的属性对应
            let data = {
                title:createQuestionApp.title,
                tagNames: this.selectedTags,
                teacherNicknames: this.selectedTeachers,
                content:content
            };
            console.log(data);
            $.ajax({
                url:'/v1/questions',
                traditional: true,  //采用传统数组编码方式，SpringMVC才能接收
                method:'POST',
                data:data,
                success:function (r) {
                    console.log(r);
                    if(r.code === OK){
                        console.log(r.message);
                    }else{
                        console.log(r.message);
                    }
                }
            });
        },
        loadTags:function () {
            console.log("loadTags");
            $.ajax({
                url:'/v1/tags',
                method: 'GET',
                success:function (r) {
                    console.log(r);
                    if(r.code == OK){
                        let list=r.data;
                        let tags = [];
                        for (let i=0;i<list.length; i++) {
                           tags.push(list[i].name);
                        }
                        createQuestionApp.tags = tags;
                    }
                }
            });
        },
        loadTeachers:function () {
            console.log("loadTeachers");
            $.ajax({
                url:'/v1/users/teacher',
                method: 'GET',
                success:function (r) {
                    console.log(r);
                    if(r.code == OK){
                        let list=r.data;
                        let teachers = [];
                        for (let i=0;i<list.length; i++) {
                            teachers.push(list[i].nickname);
                        }
                        createQuestionApp. teachers= teachers;
                    }
                }
            });
        }
    },
    created:function () {
        this.loadTags();
        this.loadTeachers();
    }
});