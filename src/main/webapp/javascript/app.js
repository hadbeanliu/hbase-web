$(document).foundation()
require.config({
	paths: {
		'a': ['a'],
		'b': ['b']
	}
});
//返回头部、底部
function goTop() {
	window.scroll(0, 0)
}

function goBottom() {
	var bottomH = Math.max(document.body.scrollHeight, document.documentElement.scrollHeight);
	window.scroll(0, bottomH);
}

//;(function($){var _=["DOMMouseScroll","mousewheel"];if($.event.fixHooks)for(var B=_.length;B;)$.event.fixHooks[_[--B]]=$.event.mouseHooks;$.event.special.mousewheel={setup:function(){if(this.addEventListener){for(var $=_.length;$;)this.addEventListener(_[--$],A,false)}else this.onmousewheel=A},teardown:function(){if(this.removeEventListener){for(var $=_.length;$;)this.removeEventListener(_[--$],A,false)}else this.onmousewheel=null}};$.fn.extend({mousewheel:function($){return $?this.bind("mousewheel",$):this.trigger("mousewheel")},unmousewheel:function($){return this.unbind("mousewheel",$)}});function A(B){var _=B||window.event,A=[].slice.call(arguments,1),D=0,C=true,E=0,F=0;B=$.event.fix(_);B.type="mousewheel";if(_.wheelDelta)D=_.wheelDelta/120;if(_.detail)D=-_.detail/3;F=D;if(_.axis!==undefined&&_.axis===_.HORIZONTAL_AXIS){F=0;E=-1*D}if(_.wheelDeltaY!==undefined)F=_.wheelDeltaY/120;if(_.wheelDeltaX!==undefined)E=-1*_.wheelDeltaX/120;A.unshift(B,D,E,F);return($.event.dispatch||$.event.handle).apply(this,A)}})(jQuery);
//返回头部、底部
$(function() {
	$(".gotop").click( /*定义返回顶部点击向上滚动的动画*/
		function() {
			$('html,body').animate({
				scrollTop: 0
			}, 700);
		});
	$(".gohome").click( /*定义返回顶部点击向上滚动的动画*/
		function() {
			/*HomepageFavorite.Homepage();*/
		});
	$(".gobottom").click( /*定义返回顶部点击向上滚动的动画*/
		function() {
			var bottomH = Math.max(document.body.scrollHeight, document.documentElement.scrollHeight);
			window.scroll(0, bottomH);
		});
	$(document).scroll(
		function() {
			if($(document.body).scrollTop() + $(document).scrollTop() > 300 && $(document.body).scrollTop() + $(document).scrollTop() < 300) {
				$(".go").css("display", "block");
				$(".gobottom").css("display", "block"), $(".gotop").css("display", "none");
			} else if($(document.body).scrollTop() + $(document).scrollTop() > 300) {
				$(".go").css("display", "block");
				$(".gobottom").css("display", "none"), $(".gotop").css("display", "block");
			} else {
				$(".go").css("display", "none");
			}
		}
	);
});