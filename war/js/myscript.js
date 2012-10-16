$('a#top').click(function(){
     $('html, body').animate({scrollTop: '0px'}, 1000);
     return false;
});

jQuery(function($){
	$(".tweet_block").tweet({
	  join_text: "auto",
	  username: "envato",
	  avatar_size: 0,
	  count: 2,
	  auto_join_text_default: "we said,",
	  auto_join_text_ed: "we",
	  auto_join_text_ing: "we were",
	  auto_join_text_reply: "we replied",
	  auto_join_text_url: "we were checking out",
	  loading_text: "loading tweets..."
	});
});

$(document).ready(function(){		
	//toggle
	$(".toggle h3").eq(0).addClass("active");
	$(".toggle .toggle_cont").eq(0).show();

	$(".toggle h3").click(function(){
		$(this).next(".toggle_cont").slideToggle("slow")
		$(this).toggleClass("active");
	});
		
	//tabs
	$(".tab_content").hide();
	$("ul.tabs li:first").addClass("active").show();
	$(".tab_content:first").show();
	$("ul.tabs li").click(function() {
		$("ul.tabs li").removeClass("active");
		$(this).addClass("active");
		$(".tab_content").hide();
		var activeTab = $(this).find("a").attr("href");
		$(activeTab).fadeIn(1000);
		return false;
	});	
	
	//accordion
	$(".accordion h3").eq(0).addClass("active");
	$(".accordion .accord_cont").eq(0).show();

	$(".accordion h3").click(function(){
		$(this).next(".accord_cont").slideToggle("slow")
		.siblings(".accord_cont:visible").slideUp("slow");
		$(this).toggleClass("active");
		$(this).siblings("h3").removeClass("active");
	});	
	
		
});


