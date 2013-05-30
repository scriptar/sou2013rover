var ROGO = {};

if (typeof console !== "object") {
	var console = {
		msg: []
	};
}
if (typeof console.log !== "function") {
	console.log = function () {
		var i;
		for (i = 0; i < arguments.length; i += 1) {
			console.msg.push(String(arguments[i]));
		}
	};
}

/* constants */
ROGO.c = {
	NUM_ROGOVARS: 10,
	priority: {
		STOP_PRIORITY: 0,
		CONTROL_PRIORITY: 1,
		PREFIX_PRIORITY: 2,
		INFIX_PRIORITY: 3
	},
	nodeType: {
		NT_UNKNOWN: 0,
		NT_CONTROL: 1,
		NT_OP: 2,
		NT_NUM: 3,
		NT_VAR: 4,
		NT_FUNC: 5,
		NT_LIST_START: 6,
		NT_LIST_END: 7
	}
};

/* primitives */
ROGO.primitives = {
	"-": {ntype: ROGO.c.nodeType.NT_OP, args: 2, priority: ROGO.c.priority.INFIX_PRIORITY + 3, func: null},
	"*": {ntype: ROGO.c.nodeType.NT_OP, args: 2, priority: ROGO.c.priority.INFIX_PRIORITY + 4, func: null},
	"/": {ntype: ROGO.c.nodeType.NT_OP, args: 2, priority: ROGO.c.priority.INFIX_PRIORITY + 4, func: null},
	"+": {ntype: ROGO.c.nodeType.NT_OP, args: 2, priority: ROGO.c.priority.INFIX_PRIORITY + 3, func: null},
	"<": {ntype: ROGO.c.nodeType.NT_OP, args: 2, priority: ROGO.c.priority.INFIX_PRIORITY + 1, func: null},
	"<=": {ntype: ROGO.c.nodeType.NT_OP, args: 2, priority: ROGO.c.priority.INFIX_PRIORITY + 1, func: null},
	"<>": {ntype: ROGO.c.nodeType.NT_OP, args: 2, priority: ROGO.c.priority.INFIX_PRIORITY + 1, func: null},
	"=": {ntype: ROGO.c.nodeType.NT_OP, args: 2, priority: ROGO.c.priority.PREFIX_PRIORITY, func: null},
	"==": {ntype: ROGO.c.nodeType.NT_OP, args: 2, priority: ROGO.c.priority.INFIX_PRIORITY + 1, func: null},
	">": {ntype: ROGO.c.nodeType.NT_OP, args: 2, priority: ROGO.c.priority.INFIX_PRIORITY + 1, func: null},
	">=": {ntype: ROGO.c.nodeType.NT_OP, args: 2, priority: ROGO.c.priority.INFIX_PRIORITY + 1, func: null},
	"AND": {ntype: ROGO.c.nodeType.NT_OP, args: 2, priority: ROGO.c.priority.INFIX_PRIORITY + 2, func: null},
	"NOT": {ntype: ROGO.c.nodeType.NT_OP, args: 1, priority: ROGO.c.priority.INFIX_PRIORITY + 3, func: null},
	"OR": {ntype: ROGO.c.nodeType.NT_OP, args: 2, priority: ROGO.c.priority.INFIX_PRIORITY + 2, func: null},
	"BACK": {ntype: ROGO.c.nodeType.NT_CONTROL, args: 1, priority: ROGO.c.priority.PREFIX_PRIORITY, func: null},
	"BK": {ntype: ROGO.c.nodeType.NT_CONTROL, args: 1, priority: ROGO.c.priority.PREFIX_PRIORITY, func: null},
	"FD": {ntype: ROGO.c.nodeType.NT_CONTROL, args: 1, priority: ROGO.c.priority.PREFIX_PRIORITY, func: null},
	"FORWARD": {ntype: ROGO.c.nodeType.NT_CONTROL, args: 1, priority: ROGO.c.priority.PREFIX_PRIORITY, func: null},
	"IF": {ntype: ROGO.c.nodeType.NT_CONTROL, args: 2, priority: ROGO.c.priority.CONTROL_PRIORITY, func: null},
	"IFELSE": {ntype: ROGO.c.nodeType.NT_CONTROL, args: 3, priority: ROGO.c.priority.CONTROL_PRIORITY, func: null},
	"LEFT": {ntype: ROGO.c.nodeType.NT_CONTROL, args: 1, priority: ROGO.c.priority.PREFIX_PRIORITY, func: null},
	"LT": {ntype: ROGO.c.nodeType.NT_CONTROL, args: 1, priority: ROGO.c.priority.PREFIX_PRIORITY, func: null},
	"LZAIM": {ntype: ROGO.c.nodeType.NT_CONTROL, args: 1, priority: ROGO.c.priority.PREFIX_PRIORITY, func: null},
	"LZFIRE": {ntype: ROGO.c.nodeType.NT_CONTROL, args: 1, priority: ROGO.c.priority.PREFIX_PRIORITY, func: null},
	"PAUSE": {ntype: ROGO.c.nodeType.NT_CONTROL, args: 0, priority: ROGO.c.priority.PREFIX_PRIORITY, func: null},
	"REPEAT": {ntype: ROGO.c.nodeType.NT_CONTROL, args: 2, priority: ROGO.c.priority.CONTROL_PRIORITY, func: null},
	"RIGHT": {ntype: ROGO.c.nodeType.NT_CONTROL, args: 1, priority: ROGO.c.priority.PREFIX_PRIORITY, func: null},
	"RT": {ntype: ROGO.c.nodeType.NT_CONTROL, args: 1, priority: ROGO.c.priority.PREFIX_PRIORITY, func: null}
};

/* structs */
ROGO.treeNode = function () {
	var tnode = {
		ntxt: "",
		ntype: ROGO.c.nodeType.NT_UNKNOWN,
		varidx: -1,
		nval: 0,
		left: null,
		right: null,
		prev: null,
		next: null,
		primkey: ""
	};
	return tnode;
};

/* parser */
ROGO.parser = function (spec) {
	var obj = {},
		syntree = null,
		src = "",
		makeFlatTree,
		makeExpNode,
		makeParseTree;
	makeFlatTree = function () {
		var i,
			p = ROGO.primitives,
			tree = null,
			node = null,
			current = null,
			isVarRE = /^V\d+$/,
			isNumRE = /^(\-)?(\d)+(\.?\d+)?$/,
			word = "",
			wordList = [];
		tree = null;
		src = src.toUpperCase().replace(/[^A-Z0-9\.\+\-\*\/=<>\[\]]/g, ' '); //filter out unexpected chars
		src = src.replace(/([A-Z0-9\.\-]+)/g, function(match, group1, offset, original) { return ' ' + group1 + ' '; }); //pad words with spaces
		src = src.replace(/[\[]/g, ' [ ').replace(/[\]]/g, ' ] '); //pad brackets with spaces
		src = src.replace(/--/g, ' - -'); //fix minus/negative case
		src = src.replace(/\s+/g, ' '); //convert all whitespace into single space
		wordList = src.split(' ');
		for (i = 0; i < wordList.length; i += 1) {
			word = wordList[i];
			if (word.length > 0) {
				node = ROGO.treeNode();
				node.ntxt = word;
				if (typeof p[word] === "object") {
					//word found in primitives
					//document.write("<em>PRIM:</em> " + word + "<br />");
					node.ntype = p[word].ntype;
					node.primkey = word;
				} else if (isVarRE.test(word)) {
					//word is a variable
					//document.write("<em>VAR:</em> " + word + "<br />");
					node.ntype = ROGO.c.nodeType.NT_VAR;
					node.varidx = parseInt(word.substr(1), 10);
				} else if (isNumRE.test(word)) {
					//word is a number
					//document.write("<em>NUM:</em> " + word + "<br />");
					node.ntype = ROGO.c.nodeType.NT_NUM;
					node.nval = parseFloat(word);
				} else if (word == '[') {
					//document.write("[<div style=\"margin-left: 20px;\">");
					node.ntype = ROGO.c.nodeType.NT_LIST_START;
				} else if (word == ']') {
					//document.write("</div>]");
					node.ntype = ROGO.c.nodeType.NT_LIST_END;
				} else {
					//document.write("UNKNOWN: " + word + "<br />");
					//node = null;
				}
				if (!tree) {
					current = tree = node;
				} else {
					current.next = node;
					node.prev = current;
					current = node;
				}
			}
		}
		return tree;
	};
	makeExpNode = function (parent, leftChild, rightChild)
	{
		parent.left = leftChild;
		parent.right = rightChild;
		return parent;
	};
	makeParseTree = function (tree) {
		var current = null,
			opcmd = null,
			left = null,
			right = null,
			listCurrent = null,
			listCount = 0,
			priority = 0,
			nodePriority = 0;
		//handle lists first
		current = tree;
		while (current) {
			//any list beginnings?
			if (current.ntype === ROGO.c.nodeType.NT_LIST_START) {
				listCurrent = current;
				listCount = 0;
				while (listCurrent) {
					switch (listCurrent.ntype) {
						case ROGO.c.nodeType.NT_LIST_START:
							listCount++;
							break;
						case ROGO.c.nodeType.NT_LIST_END:
							listCount--;
							if (listCount == 0) {
								//end of list reached...
								left = current.next; //1st expression in list
								right = listCurrent; //list end
								makeExpNode(current, left, right); //make everything in list the left child of list start, right child is list end
								left.prev = null; //1st expression in list has no prior nodes
								if (right.next != null) {
									right.next.prev = current;
								}
								current.next = right.next; //list points to node after end of the list
								right.prev.next = null; //the "next" of the last element in list should point to nothing
								right.prev = null; //the list end should point to nothing
								right.next = null; //the list end should point to nothing
								left = makeParseTree(left); //inner list is its own tree, parse it
								if (current.left != left)
									current.left = left; //fix tree if root changed
							}
							break;
						default:
							break;
					}
					listCurrent = listCurrent.next;
				}
			}
			current = current.next;
		}
		
		//handle nodes from highest priority to lowest
		for (priority = ROGO.c.priority.INFIX_PRIORITY + 4; priority >= 0; priority--)
		{
			current = tree;
			while (current)
			{
				nodePriority = (current.primkey.length > 0 ? (typeof ROGO.primitives[current.primkey] === "object" ? ROGO.primitives[current.primkey].priority : 0) : 0);
				if (nodePriority == priority)
				{
					switch (current.ntype)
					{
						case ROGO.c.nodeType.NT_OP:
							//swap 1st and 2nd to make the op the current
							opcmd = current;
							if (ROGO.primitives[opcmd.primkey].args != 1) // (don't swap if op is is a "NOT")
							{
								left = current.prev;
								opcmd.prev = left.prev;
								if (opcmd.prev != null)
									opcmd.prev.next = opcmd;
								left.next = opcmd.next;
								if (left.next != null)
									left.next.prev = left;
								opcmd.next = left;
								left.prev = opcmd;
								if (left == tree)
									tree = opcmd; //fix tree if first node was left
							}
							/* no break */
						case ROGO.c.nodeType.NT_CONTROL:
							opcmd = current;
							left = current.next;
							right = (current.next != null ? current.next.next : null);
							if (ROGO.primitives[opcmd.primkey].args == 1) {
								/* unexpected missing argument, create one */
								if (left == null)
									left = ROGO.treeNode();
								makeExpNode(opcmd, left, null);
								opcmd.next = left.next;
								if (opcmd.next != null)
									opcmd.next.prev = opcmd;
								left.prev = null;
								left.next = null;
							} else if (ROGO.primitives[opcmd.primkey].args == 2) {
								makeExpNode(opcmd, left, right);
								if (left != null) {
									left.prev = null;
									left.next = null;
								}
								if (right != null) {
									opcmd.next = right.next;
									if (opcmd.next)
										opcmd.next.prev = opcmd;
									right.prev = null;
									right.next = null;
								} else {
									opcmd.next = null;
								}
							}
							break;
						case ROGO.c.nodeType.NT_NUM:
						case ROGO.c.nodeType.NT_VAR:

							break;
						default:
							break;
					}
				}
				current = current.next;
			}
		}
		return tree;
	};
	obj.source = "";
	
	obj.dispNodeType = function (type) {
		var nt = ROGO.c.nodeType;
		switch (type) {
			case nt.NT_UNKNOWN: return "UNKNOWN";
			case nt.NT_CONTROL: return "CONTROL";
			case nt.NT_OP: return "OP";
			case nt.NT_NUM: return "NUM";
			case nt.NT_VAR: return "VAR";
			case nt.NT_FUNC: return "FUNC";
			case nt.NT_LIST_START: return "LIST_START";
			case nt.NT_LIST_END: return "LIST_END";
			default: return "???";
		}
	};
	obj.printTree = function (tree) {
		var retVal = "",
		printInnerFunc = function(current, depth) {
			var i;
			while (current) {
				for (i = 0; i < depth; i++)
					retVal += "  ";
				switch (current.ntype)
				{
					case ROGO.c.nodeType.NT_OP:
					case ROGO.c.nodeType.NT_CONTROL:
						if (current.primkey.length > 0) {
							retVal += current.primkey;
						} else {
							retVal += "OP/CMD?";
						}
						break;
					case ROGO.c.nodeType.NT_NUM:
						retVal += String(current.nval);
						break;
					case ROGO.c.nodeType.NT_VAR:
						retVal += "V[" + current.varidx +"]";
						break;
					case ROGO.c.nodeType.NT_LIST_START:
						retVal += "[";
						break;
					case ROGO.c.nodeType.NT_LIST_END:
						retVal += "]";
						break;
					default:
						retVal += "???";
						break;
				}
				retVal += " (" + obj.dispNodeType(current.ntype) + ")\n";
				if (current.left != null)
				{
					for (i = 0; i < depth + 1; i++)
						retVal += "  ";
					retVal += "Left Child\n";
					printInnerFunc(current.left, depth + 1);
				}
				if (current.right != null)
				{
					for (i = 0; i < depth + 1; i++)
						retVal += "  ";
					retVal += "Right Child\n";
					printInnerFunc(current.right, depth + 1);
				}
				current = current.next;
			}
		};
		printInnerFunc(tree, 0);
		return retVal;
	};
	obj.destroyTree = function (tree) {
		if (typeof tree !== "object")
			tree = syntree;
		var current = tree,
			next;
		while (current)
		{
			if (current.left != null)
				obj.destroyTree(current.left);
			if (current.right != null)
				obj.destroyTree(current.right);
			next = current.next;
			current.prev = null;
			current.next = null;
			current.left = null;
			current.right = null;
			//free(current);
			delete current;
			current = next;
		}
	};
	obj.parse = function (args) {
		args = args || {};
		src = (typeof args.source === "string" ? args.source : (typeof args === "string" ? args : (typeof obj.source === "string" ? obj.source : "")));
		syntree = makeFlatTree();
		//document.write("<pre>" + obj.printTree(syntree) + "</pre><hr />");
		syntree = makeParseTree(syntree);
		return syntree;
	};
	return obj;
};

/* executor */
ROGO.executor = function (spec) {
	var p = ROGO.primitives,
	rogo_vars = [],
	rover = spec.rover || null,
	_current = null,
	execTree = function (current) {
		while (current) {
			switch (current.ntype) {
				case ROGO.c.nodeType.NT_OP:
				case ROGO.c.nodeType.NT_CONTROL:
					if (current.primIdx != -1)
						ROGO.primitives[current.primkey].func(current);
					else
						console.log("OP/CMD?");
					break;
				case ROGO.c.nodeType.NT_NUM:
					console.log(current.nval);
					break;
				case ROGO.c.nodeType.NT_VAR:
					console.log("V[" + current.varIdx + "]");
					break;
				case ROGO.c.nodeType.NT_LIST_START:
					execTree(current.left);
					if (current.left != null) {
						current.nval = current.left.nval;
					}
					break;
				case ROGO.c.nodeType.NT_LIST_END:
					console.log("]");
					break;
				default:
					console.log("???");
					break;
			}
			current = current.next;
		}
	},
	loadVar = function (current) {
		if (current.varidx != -1 && current.varidx <= ROGO.c.NUM_ROGOVARS) {
			current.nval = rogo_vars[current.varidx];
			console.log("Loading Variable #" + current.varidx + ". Value: " + current.nval);
		}
		return current;
	},
	execNode = function (current, fcn) {
		var nval = 0, nleft = 0, nright = 0;
		console.log(fcn + " called");
		//left?
		if (current.left != null) {
			if (current.left.primkey.length > 0) {
				ROGO.primitives[current.left.primkey].func(current.left);
			}
			loadVar(current.left);
			nleft = current.left.nval;
		}
		//right?
		if (current.right != null) {
			if (current.right.primkey.length > 0) {
				ROGO.primitives[current.right.primkey].func(current.right);
			}
			loadVar(current.right);
			nright = current.right.nval;
		}
		//do it
		switch(fcn) {
			case 'sub': //subtract
				nval = nleft - nright;
				break;
			case 'add': //add
				nval = nleft + nright;
				break;
			case 'mul': //multiply
				nval = nleft * nright;
				break;
			case 'div': //divide
				if (nright != 0)
					nval = nleft / nright;
				break;
			case 'gt': //greater
				nval = (nleft > nright);
				break; 
			case 'lt': //less
				nval = (nleft < nright);
				break; 
			case 'assign': //assign
				nval = nright;
				if (current.left.varidx != -1 && current.left.varidx <= ROGO.c.NUM_ROGOVARS)
					rogo_vars[current.left.varidx] = nval;
				break;
			case 'eq': //eq
				nval = (nleft == nright);
				break;
			case 'neq': //neq
				nval = (nleft != nright);
				break;
			case 'lte': //lte
				nval = (nleft <= nright);
				break;
			case 'gte': //gte
				nval = (nleft >= nright);
				break;
			case 'and': //and
				nval = (nleft && nright);
				break;
			case 'or': //or
				nval = (nleft || nright);
				break;
			case 'not': //not
				nval = !nleft;
				break;
			default:
				break;
		}
		console.log(fcn + " " + String(nleft) + ", " + String(nright) + " => " + nval);
		current.nval = nval;
		return current;
	},
	evalNodeValue = function (current) {
		var node;
		if (current.ntype == ROGO.c.nodeType.NT_LIST_START && current.left != null)
			current = evalNodeValue(current.left);
		if (current.primkey.length > 0)
			node = ROGO.primitives[current.primkey].func(current);
		else if (current.varidx != -1)
			node = loadVar(current);
		else
			node = current;
		return node;
	},
	evalNodeValueInt = function (current) {
		return parseInt(current.nval, 10);
	};
	/* primitive functions */
	/* operators */
	p["-"].func = function (current) { return execNode(current, "sub"); };
	p["+"].func = function (current) { return execNode(current, "add"); };
	p["*"].func = function (current) { return execNode(current, "mul"); };
	p["/"].func = function (current) { return execNode(current, "div"); };
	p["<"].func = function (current) { return execNode(current, "lt"); };
	p["<="].func = function (current) { return execNode(current, "lte"); };
	p["<>"].func = function (current) { return execNode(current, "neq"); };
	p["="].func = function (current) { return execNode(current, "assign"); };
	p["=="].func = function (current) { return execNode(current, "eq"); };
	p[">"].func = function (current) { return execNode(current, "gt"); };
	p[">="].func = function (current) { return execNode(current, "gte"); };
	p["AND"].func = function (current) { return execNode(current, "and"); };
	p["NOT"].func = function (current) { return execNode(current, "not"); };
	p["OR"].func = function (current) { return execNode(current, "or"); };
	/* control statements */
	p["IF"].func = function (current) {
		if (evalNodeValueInt(evalNodeValue(current.left))) {
			//test successful
			console.log("Test successful.");
			execTree(current.right);
			
		} else {
			//test failed
			console.log("Test failed.");
		}
	};
	p["REPEAT"].func = function (current) { 
		var i = evalNodeValueInt(evalNodeValue(current.left));
		console.log("for(1.." + i + ") {");
		for (; i > 0; i--) {
			execTree(current.right);
		}
		console.log("}");
	};
	/* movement commands */
	p["FORWARD"].func = p["FD"].func = function (current) {
		var i = evalNodeValueInt(evalNodeValue(current.left));
		console.log("FD " + i);
		if (typeof rover === "object")
			rover.addQueue({cmd: "fd", params: [i]});
		return current;
	};
	p["BACK"].func = p["BK"].func = function (current) {
		var i = evalNodeValueInt(evalNodeValue(current.left));
		console.log("BK " + i);
		if (typeof rover === "object")
			rover.addQueue({cmd: "bk", params: [i]})
		return current;
	};
	p["LEFT"].func = p["LT"].func = function (current) {
		var i = evalNodeValueInt(evalNodeValue(current.left));
		console.log("LT " + i);
		if (typeof rover === "object")
			rover.addQueue({cmd: "lt", params: [i]})
		return current;
	};
	p["RIGHT"].func = p["RT"].func = function (current) {
		var i = evalNodeValueInt(evalNodeValue(current.left));
		console.log("RT " + i);
		if (typeof rover === "object")
			rover.addQueue({cmd: "rt", params: [i]})
		return current;
	};
	p["LZAIM"].func = function (current) { return current; };
	p["LZFIRE"].func = function (current) { return current; };
	return {
		exec: function (tree) {
			execTree(tree);
		}
	};
};

/* virtual ROGO rover state */
ROGO.rover = function (spec) {
	var objRef = {
		domRef: null,
		tree: null,
		list: [],
		initial: {x: 0, y: 0},
		current: {x: 0, y: 0, heading: 0, time: 0, increment: 0, travelling: false, rotating: false},
		destination: {time: 0, heading: 0},
		vel: 0
	},
	parser = ROGO.parser(),
	transformStyleProperty = "transform",
	draw = function (ref) {
		ref.domRef.style.left = (parseInt(ref.initial.x + ref.current.x, 10) || 0) + "px";
		ref.domRef.style.top = (-1 * parseInt(ref.initial.y + ref.current.y, 10) || 0) + "px";
		ref.domRef.style[transformStyleProperty] = "rotate(" + ref.current.heading + "deg)";
	},
	destinationReached = function (ref) {
		var reached = true;
		if (ref.current.travelling)
			reached = (ref.destination.time <= ref.current.time);
		else if (ref.current.rotating)
			reached = (ref.destination.heading == ref.current.heading);
		return reached;
	},
	move = function (ref) {
		var reached = destinationReached(ref), theta;
		if (!reached) {
			ref.current.time += 0.5;
			if (ref.current.travelling) {
				theta = (ref.current.heading - (ref.current.increment > 0 ? 0 : 180)) * Math.PI / 180.0;
				ref.current.x = (ref.vel * Math.sin(theta) * ref.current.time);
				ref.current.y = (ref.vel * Math.cos(theta) * ref.current.time);
			}
			if (ref.current.rotating) {
				ref.current.heading += ref.current.increment;
			}
			setTimeout((function () {
				move(ref);
			}), 10);
		} else {
			vel = 0;
			ref.current.increment = 0;
			ref.current.travelling = false;
			ref.current.rotating = false;
			ref.current.time = 0;
			ref.destination.time = 0;
		}
		draw(ref);
		return reached;
	},
	go = function (ref, cmd, units, degrees) {
		ref.initial.x += ref.current.x;
		ref.initial.y += ref.current.y;
		ref.current.x = 0;
		ref.current.y = 0;
		switch (cmd) {
			case "FD":
				ref.destination.time = units;
				ref.current.travelling = true;
				ref.current.increment = 1;
				break;
			case "BK":
				ref.destination.time = units;
				ref.current.travelling = true;
				ref.current.increment = -1;
				break;
			case "RT":
				ref.destination.heading = ref.current.heading + degrees;
				ref.current.rotating = true;
				ref.current.increment = 1;
				break;
			case "LT":
				ref.destination.heading = ref.current.heading - degrees;
				ref.current.rotating = true;
				ref.current.increment = -1;
				break;
		}
		ref.vel = 10;
		move(ref);
	},
	fd = function (ref, arr) {
		if (!isNaN(parseInt(arr[0], 10)))
			go(ref, "FD", parseInt(arr[0], 10), 0);
	},
	bk = function (ref, arr) {
		if (!isNaN(parseInt(arr[0], 10)))
			go(ref, "BK", parseInt(arr[0], 10), 0);
	},
	lt = function (ref, arr) {
		if (!isNaN(parseInt(arr[0], 10)))
			go(ref, "LT", 0, parseInt(arr[0], 10) % 360);
	},
	rt = function (ref, arr) {
		if (!isNaN(parseInt(arr[0], 10)))
			go(ref, "RT", 0, parseInt(arr[0], 10) % 360);
	},
	run = function (ref) {
		var instruction;
		if (ref.current.travelling || ref.current.rotating) {
			setTimeout((function () {
				run(ref);
			}), 100);
			//console.log("Waiting...");
		} else if (ref.list.length > 0) {
			instruction = ref.list.pop();
			//console.log("Running: " + instruction.cmd + " " + instruction.params[0]);
			switch (instruction.cmd) {
				case "fd": fd(ref, instruction.params); break;
				case "bk": bk(ref, instruction.params); break;
				case "rt": rt(ref, instruction.params); break;
				case "lt": lt(ref, instruction.params); break;
			}
			run(ref);
		}
	};
	objRef.addQueue = function (instruction) {
		objRef.list.push(instruction);
		return objRef;
	};
	objRef.exec = function (src) {
		if (objRef.tree)
			parser.destroyTree(objRef.tree);
		objRef.tree = parser.parse(src);
		var executor = ROGO.executor({rover: objRef});
		executor.exec(objRef.tree);
		objRef.list.reverse();
		run(objRef);
		return objRef;
	};
	objRef.setID = function (id, x, y) {
		var i,
			props = ["transform", "-ms-transform", "-moz-transform", "-webkit-transform", "-o-transform"];
		objRef.domRef = document.getElementById(id);
		objRef.domRef.style.display = "inline";
		objRef.domRef.style.position = "absolute";
		for (i = 0; i < props.length; i++) {
			if (typeof objRef.domRef.style[props[i]] !== "undefined") {
				transformStyleProperty = props[i];
				break;
			}
		}
		return objRef;
	};
	objRef.setInitialXY = function(x, y) {
		objRef.initial.x = x;
		objRef.initial.y = y;
		return objRef;
	};
	objRef.forward = function (units) {
		fd(objRef, [units]);
		return objRef;
	};
	objRef.back = function (units) {
		bk(objRef, [units]);
		return objRef;
	};
	objRef.left = function (degrees) {
		lt(objRef, [degrees]);
		return objRef;
	};
	objRef.right = function (degrees) {
		rt(objRef, [degrees]);
		return objRef;
	}
	objRef.isMoving = function () {
		return objRef.current.travelling || objRef.current.rotating;
	};
	return objRef;
};

/* main */
ROGO.interpreter = function (spec) {
	var source,
		tree = {};
	return {
		load: function (args) {
			
		},
		run: function (args) {
			
		}
	};
};