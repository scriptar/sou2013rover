/*
	tree.c: Rogo tree node structures
	Created by: Jeff Miller, 2/24/2013
*/

#include "tree.h"

TNODE *makeTree(char **cmdlist)
{
	char *cmd;
	int i;
	//create command tree from cmdlist array
	//TNODE *tree = newTreeNode("ROGOBEGIN");
	//TNODE *next = tree;
	while (*cmdlist)
	{
		cmd = *cmdlist;
		printf("%s\n", cmd);
		//split cmd into cmds...?

		//setNextNode(next, newTreeNode(cmd));
		free(cmd);
		cmd = NULL;
		*cmdlist++;
	}
	//free(cmdlist);
	//setNextNode(next, newTreeNode("ROGOEND"));
	//return tree;
	return NIL;
}

TNODE *makeExpNode(TNODE *parent, TNODE *leftChild, TNODE *rightChild)
{
	parent->left = leftChild;
	parent->right = rightChild;
	return parent;
}

TNODE *setNextNode(TNODE *currentNode, TNODE *nextNode)
{
	currentNode->next = nextNode;
	return nextNode;
}


/*
TNODE *exec(TNODE *args, char fcn)
{
	switch(fcn) {
		case '-': ival = -ival; break;
		
	}
}
*/

TNODE *newTreeNode(char *ident)
{
	//char *pos = (char*) bsearch(ident, prims, sizeof(prims), sizeof(primtype), (int(*)(const void*, const void*))strcmp);
	//NodeType type = determine type of node from ident...
	NodeType type = ntUnknown;
	//if (strcmp(ident, "[") != 0)
	//	type = ntList;
	
	switch (type) {
		case ntVar:
			
			break;
		case ntOp:
			if (strcmp(ident, "=") != 0)
			{
				
			}
			break;
		case ntControl:
			
			break;
		case ntUnknown:
			return NIL;
	}
	TNODE *newNode = (TNODE *)malloc(sizeof(TNODE));
	//newNode->ident = ident;
	newNode->type = type;
	newNode->left = NULL;
	newNode->right = NULL;
	newNode->next = NULL;
	return newNode;
}

TNODE *getMulNode(TNODE *args)
{
	return NIL;
}

TNODE *getAddNode(TNODE *args)
{
	return NIL;
}

TNODE *getSubNode(TNODE *args)
{
	return NIL;
}

TNODE *getDivideNode(TNODE *args)
{
	return NIL;
}

TNODE *getLessNode(TNODE *args)
{
	return NIL;
}

TNODE *getEqualNode(TNODE *args)
{
	return NIL;
}

TNODE *getGreaterNode(TNODE *args)
{
	return NIL;
}

TNODE *getLessEqualNode(TNODE *args)
{
	return NIL;
}

TNODE *getNotEqualNode(TNODE *args)
{
	return NIL;
}

TNODE *getGreaterEqualNode(TNODE *args)
{
	return NIL;
}

TNODE *getAndNode(TNODE *args)
{
	return NIL;
}

TNODE *getBackNode(TNODE *args)
{
	return NIL;
}

TNODE *getForwardNode(TNODE *args)
{
	return NIL;
}

TNODE *getIfNode(TNODE *args)
{
	return NIL;
}

TNODE *getIfElseNode(TNODE *args)
{
	return NIL;
}

TNODE *getLeftNode(TNODE *args)
{
	return NIL;
}

TNODE *getLzAimNode(TNODE *args)
{
	return NIL;
}

TNODE *getLzFireNode(TNODE *args)
{
	return NIL;
}

TNODE *getNotNode(TNODE *args)
{
	return NIL;
}

TNODE *getOrNode(TNODE *args)
{
	return NIL;
}

TNODE *getPauseNode(TNODE *args)
{
	return NIL;
}

TNODE *getRepeatNode(TNODE *args)
{
	return NIL;
}

TNODE *getRightNode(TNODE *args)
{
	return NIL;
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


