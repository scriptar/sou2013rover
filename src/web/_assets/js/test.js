var goRogo = function () {
	var r = document.getElementById("rogo"),
	x = 0, y = 0, max_x = 400, max_y = 400,
	v = [
		{x:0,y:0,cl:""},
		{x:1,y:0,cl:"right"},
		{x:0,y:-1,cl:"up"},
		{x:-1,y:0,cl:"left"},
		{x:0,y:1,cl:"down"}
	],
	vDir = {stop:0,right:1,up:2,left:3,down:4},
	speed = 5,
	dir = vDir.right;
	r.style.display = "inline";
	r.style.position = "relative";
	setInterval(function () {
		x += speed * v[dir].x;
		y += speed * v[dir].y;
		if (x > max_x)	{dir = vDir.down; x = max_x;}
		if (x < 0) {dir = vDir.up; x = 0;}
		if (y > max_y) {dir = vDir.left; y = max_y;}
		if (y < 0) {dir = vDir.right; y = 0;}
		r.className = v[dir].cl;
		r.style.left = x + "px";
		r.style.top = y + "px";
	}, 50);
};