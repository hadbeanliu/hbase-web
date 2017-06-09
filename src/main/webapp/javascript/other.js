//右侧消息弹窗
$(document).on("click",'.articl_edit',function() {
	$("#slide-content").addClass("open");
})
$(".close-slide-content").on("click",function() {
	$("#slide-content").removeClass("open");
})

//搜索
$(function() {
	var $search = $(".search");
	var $iconSearch = $("#icon-search");
	var $bgreveal = $('.bg-reveal');
	$iconSearch.on("click", function() {
		iconClick($(this));
		$search.toggleClass("open");
		$('#search').val("");
	});

	function iconClick(icon) {
		icon.toggleClass("icon-is-visible").siblings().removeClass("icon-is-visible");
		icon.parents(".header").toggleClass("open");
		$bgreveal.toggleClass("show");
		$bgreveal.removeClass("bg-black");
		$("body").toggleClass("over-hidden");
	}
	//搜索展开效果
	var sug = $('#suggestion');
	$li = sug.find('li')
	$('#search').on('keyup focus', function() {
		if($(this).val().length > 0) {
			sug.addClass('is-open');
			$bgreveal.addClass("bg-black");
		} else {
			$bgreveal.removeClass("bg-black");
		}
	})
	$li.each(function() {
		$(this).click(function() {
			$('#search').val($(this).text());
			sug.removeClass('is-open');
		});
	});
	$(document).bind('click', function(e) {
		var e = e || window.event; //浏览器兼容性 
		var ele = e.target || e.srcElement;
		var parent = $(ele).closest('.ac_result'),
			parent2 = $(ele).closest('.f-dropdown');
		if(parent && parent.attr('id') == "suggestion") {
			/*console.log('666');*/
		} else if(ele.tagName == 'INPUT' && ele.id == "search") {
			/*console.log('666');*/
		} else {
			sug.removeClass('is-open');
		}
	});
});
/*条件选择*/
var side_reveal = $("#create-conditions"),
	condition_list = side_reveal.find('.condition-list ul'),
	list_li_a = condition_list.find('li a'),
	tag_id;//当前的id
	list_li_a.click(function(){
		$(this).toggleClass('active');
		tag_id = $(this).attr('data-tag-id');
		$('#'+tag_id).toggle();
		rowclose($('#'+tag_id).find('.condition-close'));
	});
function rowclose(a){
	a.click(function(){
		var row =$(this).closest('.row'),
		data_tag_id = row.attr('id');
		row.css('display','none');
		$('a[data-tag-id='+data_tag_id+']').removeClass('active'); 
	});
}
//已选条件的删除
var data_con_3 = $('#data_tabdefault_con3');
	data_li = data_con_3.find('.condition-close');
	data_li.click(function(){
		$(this).closest('li').remove();
	});

//创建
var addCondition = $('#tag-sure');

