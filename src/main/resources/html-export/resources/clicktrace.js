var viewWidth = 800;

function zoom(pixels) {
	viewWidth += pixels;
	if (viewWidth < 100) viewWidth = 100;
	document.getElementById('view').setAttribute("style","width:"+viewWidth+"px");
}