(function( $ ){ 
	
	
	function Feature(id){
		this.id=id;
	}
	
	//初始化文章列表
	$.ajax({
		url:"http://127.0.0.1:10080/HWEB/rec/default?biz_code=headlines&hm=10",
		type:"get",
		dataType:"json",
//		jsonpCallback:"callback",
		success:function(data){
			console.log(data);
			$(data).each(function(i,obj){
				
				var item=obj.first;
				$(".transverse ul").append('<li class="clearfix">'+
										//'<div class="img"><img src="images/n1.jpg" alt=""></div>'+
										'<div class="content">'+
											
											'<div class="title">'+
												'<a href="item/get?iid='+item.id+'">'+item.properties["t"]+'</a>'+
											'</div>'+
											'<div class="rel-info">'+
												'<div class="new-info"><span class="label">'+item.info["ca"]+'</span><span>搜狐</span>·<span>01-14 10:05</span></div>'+
												'<div class="btn_container">'+
													/*'<div class="news-tag">'+
														'<textarea id="'+item.id+'"></textarea>'+
													'</div>'+*/
													'<div class="news-controls float-right">'+
														'<a title="编辑" class="articl_edit" data-toggle="info-set" data-id="'+item.id+'" ><i class="fa fa-edit"></i>编辑</a>'+
														'<span title="阅读">阅读量(273)</span>'+
														'<span title="浏览">浏览 273</span>'+
														'<a title="评论"><i class="fa fa-fw fa-comments-o"></i>评论'+
															'<num class="comment-num">(12)</num>'+
														'</a>'+
													'</div>'+
												'</div>'+
											'</div>'+
										'</div>'+
									'</li>');
			});
				
			
		}
	
		
		
	})
	
	//获取人群特征设置

	$("#pop-scope").click(function(){
		
		var province=$("select[name='province']").find('option:selected').text();
		var city=$("select[name='city']").find('option:selected').text();
		var bage=$(".begin-age").val();
		var eage=$(".end-age").val();
		var sex=$("input[name='sex']:checked").val();
		var job=$("input[name='job']").val();
		var pref=$("input[name='hobby']").val();
		var name=$("input[name='name']").val()
		
		var rank=$("input[name='rank']").val()
		
		var str="";
		var feature=new Feature();
		
		if(name!=null&&""!=name){
			str+=name+":";
			feature.name=name;
		}
		if(rank!=null&&""!=rank){
			
			str+="推送级别:"+rank+";";
			
			feature.rank=rank;
			
		}
		
		if(province!=null&&""!=province){
			feature.province=province;
			str+="地区:"+province;
			if(city!=null&&""!=city){
				str+=city;
				feature.city=city;
			}
			str+=";";
			
			
		}
		if((bage!=null&&""!=bage)||(eage!=null&&""!=eage)){
			if((bage!=null&&""!=bage))
				str+="年龄:大于"+bage+";";
			else if((eage!=null&&""!=eage))
				str+="年龄:小于"+eage+";";
			else if((bage!=null&&""!=bage)&&(eage!=null&&""!=eage))
				str+="年龄:"+bage+"-"+eage+";";
			feature.age=bage+","+eage;
		}
		if(sex!=null&&""!=sex){
			str+="性别:"+sex+";";
			feature.sex=sex;
		}
		if(job!=null&&""!=job){
			str+="职业:"+job+";";
			feature.job=job;
			
		}
		if(pref!=null&&""!=pref){
			str+="偏好:"+pref+";";
			feature.pref=pref;
		}
		console.log(str)
		var length=$("#pop-set ul li").size();
		$("#pop-set ul").append('<li>'+
		'<a data-open="create-conditions" aria-controls="create-conditions" aria-haspopup="true" tabindex="'+length+'">'+
		str+'</a>'+
		'<input type="hidden" name="pop'+length+'" value='+JSON.stringify(feature)+' />'+
		'<a class="condition-close">×</a></li>');
//		console.log(province+":"+city+":"+bage+":"+eage+":"+sex);
		
		
	});
	
	
	//文章特征设置提交
	$("#feature-sub").click(function(){
		
		var form=new FormData();
		var wr=$("input[name='score']").val();
		var tags= $("#tag-set").tagEditor('getTags')[0].tags;
		var ca=$("#category").val();
		console.log(wr);

		console.log(tags.toString());
		if(ca!=null)
			form.append("ca",ca);
		if(wr!=null)
			form.append("wr",wr);
		if(tags!=null)
			form.append("tags",tags.toString());
		var popSet=new Array();
		$("#pop-set ul input").each(function(index,data){popSet[index]=$(data).val()});
		
		form.append("popSet",popSet.toString());
		
		
		console.log(form);
		
//		$.ajax({
//			url:"http://192.168.16.111:10080/HWEB/item/edit",
//			data:form,
//			
//			error:function(data){
//				console.log(data);
//			}
//		});
		
		
		var httpReq=new MyXMLHttpRequest();
//		httpReq.open("POST","http://192.168.16.111:10080/HWEB/item/edit");
//		httpReq.setRequestHeader("Content-type","application/x-www-form-urlencoded");

		httpReq.send("POST","http://192.168.16.111:10080/HWEB/item/edit",form,function(text,xml){},null);
		
		
		
	});
	$('#tag-set').tagEditor();
	
	//初始化文章特征，点击文章编辑按钮时
	$(document).on("click",'.articl_edit',function(){
			
		
		    var tags = $('#tag-set').tagEditor('getTags')[0].tags;
		    for (i = 0; i < tags.length; i++) { $('#tag-set').tagEditor('removeTag', tags[i]); }
		
			var id=$(this).attr("data-id");
			$("input[name='featureId']").attr("value",id);
			$.ajax({
				url:"http://192.168.16.111:10080/HWEB/item/get?biz_code=headlines&iid="+id,
				type:"get",
				dataType: 'JSONP',

				jsonpCallback:"callback",
				success:function(data){
					console.log(data.info["ca"]);
					/*$('.hero-demo').tagEditor({
					    initialTags: [data.info["ca"]],
					    delimiter: ', ',  space and comma 
					    placeholder: 'Enter tags ...'
					});*/					
					$(data.keyWord).each(function(i,val){
						
						for(var tag in val){
							$("#tag-set").tagEditor('addTag',tag+"|"+val[tag]);
						}
					})
//					$("#raty").raty({
//						  score: function() {
//							   $(this).attr("data-score",'2');
//							  }
//							});
					
					$('#raty').raty({ score: 3 ,path:'images/raty'});
//				$("input[name='score']").attr("value",data.info["wr"])	;
					
				}
				
			})
			
		}
	
	);
	
})(jQuery );