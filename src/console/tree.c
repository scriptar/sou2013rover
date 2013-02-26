/*
	tree.c: Rogo tree node structures
	Created by: Jeff Miller, 2/24/2013
*/

#include "tree.h"

/*
NODE *exec(NODE *args, char fcn) {
	switch(fcn) {
		case '-': ival = -ival; break;
		
	}
}

*/
RNODE *newnode(NODETYPES type)
{
	RNODE *newnd = (RNODE *)0;
	//newnode->nodetype = type;
	//newnode->params = (RNODE *)0;
	//newnd->next = (RNODE *)0;
	return newnd;
}

RNODE *getRootNode(void)
{
	return newnode(NT_ROOT);
}

RNODE *getMulNode(RNODE *args)
{
	return (RNODE *)0;
}

RNODE *getAddNode(RNODE *args)
{
	return (RNODE *)0;
}

RNODE *getSubNode(RNODE *args)
{
	return (RNODE *)0;
}

RNODE *getDivideNode(RNODE *args)
{
	return (RNODE *)0;
}

RNODE *getLessNode(RNODE *args)
{
	return (RNODE *)0;
}

RNODE *getEqualNode(RNODE *args)
{
	return (RNODE *)0;
}

RNODE *getGreaterNode(RNODE *args)
{
	return (RNODE *)0;
}

RNODE *getLessEqualNode(RNODE *args)
{
	return (RNODE *)0;
}

RNODE *getNotEqualNode(RNODE *args)
{
	return (RNODE *)0;
}

RNODE *getGreaterEqualNode(RNODE *args)
{
	return (RNODE *)0;
}

RNODE *getAndNode(RNODE *args)
{
	return (RNODE *)0;
}

RNODE *getBackNode(RNODE *args)
{
	return (RNODE *)0;
}

RNODE *getForwardNode(RNODE *args)
{
	return (RNODE *)0;
}

RNODE *getIfNode(RNODE *args)
{
	return (RNODE *)0;
}

RNODE *getIfElseNode(RNODE *args)
{
	return (RNODE *)0;
}

RNODE *getLeftNode(RNODE *args)
{
	return (RNODE *)0;
}

RNODE *getLzAimNode(RNODE *args)
{
	return (RNODE *)0;
}

RNODE *getLzFireNode(RNODE *args)
{
	return (RNODE *)0;
}

RNODE *getNotNode(RNODE *args)
{
	return (RNODE *)0;
}

RNODE *getOrNode(RNODE *args)
{
	return (RNODE *)0;
}

RNODE *getPauseNode(RNODE *args)
{
	return (RNODE *)0;
}

RNODE *getRepeatNode(RNODE *args)
{
	return (RNODE *)0;
}

RNODE *getRightNode(RNODE *args)
{
	return (RNODE *)0;
}

PRIMTYPE prims[] = {
	{"*", 1, 1, 1, PREFIX_PRIORITY + 3, getMulNode},
	{"+", 1, 1, 1, PREFIX_PRIORITY + 2, getAddNode},
	{"-", 1, 1, 1, PREFIX_PRIORITY + 2, getSubNode},
	{"/", 1, 1, 1, PREFIX_PRIORITY + 3, getDivideNode},
	{"<", 2, 2, 2, PREFIX_PRIORITY + 1, getLessNode},
	{"=", 2, 2, 2, PREFIX_PRIORITY + 1, getEqualNode},
	{">", 2, 2, 2, PREFIX_PRIORITY + 1, getGreaterNode},
	{"<=", 2, 2, 2, PREFIX_PRIORITY + 1, getLessEqualNode},
	{"<>", 2, 2, 2, PREFIX_PRIORITY + 1, getNotEqualNode},
	{">=", 2, 2, 2, PREFIX_PRIORITY + 1, getGreaterEqualNode},
	{"AND", 0, 2, -1, PREFIX_PRIORITY, getAndNode},
	{"BACK", 1, 1, 1, PREFIX_PRIORITY, getBackNode},
	{"BK", 1, 1, 1, PREFIX_PRIORITY, getBackNode},
	{"FD", 1, 1, 1, PREFIX_PRIORITY, getForwardNode},
	{"FORWARD", 1, 1, 1, PREFIX_PRIORITY, getForwardNode},
	{"IF", 2, 2, 3, CONTROL_PRIORITY, getIfNode},
	{"IFELSE", 3, 3, 3, CONTROL_PRIORITY, getIfElseNode},
	{"LEFT", 1, 1, 1, PREFIX_PRIORITY, getLeftNode},
	{"LT", 1, 1, 1, PREFIX_PRIORITY, getLeftNode},
	{"LZAIM", 1, 1, 1, PREFIX_PRIORITY, getLzAimNode},
	{"LZFILE", 1, 1, 1, PREFIX_PRIORITY, getLzFireNode},
	{"NOT", 1, 1, 1, PREFIX_PRIORITY, getNotNode},
	{"OR", 0, 2, -1, PREFIX_PRIORITY, getOrNode},
	{"PAUSE", 0, 0, 0, PREFIX_PRIORITY, getPauseNode},
	{"REPEAT", 2, 2, 2, CONTROL_PRIORITY, getRepeatNode},
	{"RIGHT", 1, 1, 1, PREFIX_PRIORITY, getRightNode},
	{"RT", 1, 1, 1, PREFIX_PRIORITY, getRightNode},
	{0, 0, 0, 0, 0, 0}
};


