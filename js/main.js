function next() {
	$(".btn-next:first").remove();
	$(".show-all:first").remove();

	$(".clicktrace-shot.ct-hidden:first").removeClass("ct-hidden").addClass("ct-visible");
	scroll(".ct-visible:last");

	$(".btn-next:first").click(next);
}

function scroll(to) {
	$("body").animate({scrollTop: $(to).offset().top}, '500', 'swing');
}

$(function() {
	$(".btn-next:first").click(next);
	$(".show-all").click(function() {
		$(".clicktrace-shot.ct-hidden").removeClass("ct-hidden").addClass("ct-visible");
		$(".btn-next").remove();
		$(".show-all").remove();
	});
	
	registerToggle("install");
	registerToggle("all-features");
	registerToggle("use");
});

function registerToggle(topic) {
	$(".header-off." + topic + " span").click(function() {
		$(".header-off." + topic).addClass("ct-hidden");
		$(".header-on." + topic).removeClass("ct-hidden");
		$(".content." + topic).removeClass("ct-hidden");
	});
	
	$(".header-on." + topic + " span").click(function() {
		$(".header-off." + topic).removeClass("ct-hidden");
		$(".header-on." + topic).addClass("ct-hidden");
		$(".content." + topic).addClass("ct-hidden");
	});
}
