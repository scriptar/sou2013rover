/*
	tree.c: Rogo tree node structures
	Created by: Jeff Miller, 2/24/2013
*/

#include "tree.h"

PRIMTYPE prims[] = {
	{"-", 1, 1, 1, PREFIX_PRIORITY + 2, getSubNode},
	{"*", 1, 1, 1, PREFIX_PRIORITY + 3, getMulNode},
	{"/", 1, 1, 1, PREFIX_PRIORITY + 3, getDivideNode},
	{"+", 1, 1, 1, PREFIX_PRIORITY + 2, getAddNode},
	{"<", 2, 2, 2, PREFIX_PRIORITY + 1, getLessNode},
	{"<=", 2, 2, 2, PREFIX_PRIORITY + 1, getLessEqualNode},
	{"<>", 2, 2, 2, PREFIX_PRIORITY + 1, getNotEqualNode},
	{"=", 2, 2, 2, PREFIX_PRIORITY + 1, getEqualNode},
	{">", 2, 2, 2, PREFIX_PRIORITY + 1, getGreaterNode},
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
	{"", 0, 0, 0, 0, 0},
	{"", 0, 0, 0, 0, 0},
	{"", 0, 0, 0, 0, 0},
	{"", 0, 0, 0, 0, 0},
	{"", 0, 0, 0, 0, 0},
	{"", 0, 0, 0, 0, 0}
};

TNODE *makeTree(const TEXTNODE *list)
{
	const TEXTNODE *listnode = list;
	char *text;
	int i = 0;
	
	qsort(prims, sizeof(prims)/sizeof(prims[0]), sizeof(PRIMTYPE), cmpPrim);
	printf("Making Syntax Tree...\n");
	//create command tree from list array
	TNODE *tree = NIL;
	TNODE *current = NIL;
	//split text into expressions and commands...
	while (listnode)
	{
		printf("List[%d]: %s (%s)\n", i++, listnode->text, dispType(listnode->type));
		if (tree == NIL)
		{
			current = tree = newTreeNode(listnode);
		}
		else
		{
			setNextNode(current, newTreeNode(listnode));
		}
		listnode = listnode->next;
	}
	return tree;
	//return NIL;
}

int cmpPrim(const void *p1, const void *p2)
{
    PRIMTYPE *prim1 = (PRIMTYPE *) p1;
    PRIMTYPE *prim2 = (PRIMTYPE *) p2;
    return strcmp(prim1->name, prim2->name);
}

TNODE *newTreeNode(const TEXTNODE *node)
{
	PRIMTYPE *pos = NULL;
	PRIMTYPE *search = NULL;
	NodeType treetype = ntUnknown;
	int isVar;

	switch (node->type)
	{
		case OP:
		case ID:
			search = (PRIMTYPE *)malloc(sizeof(PRIMTYPE));
			search->name = (char *)malloc(strlen(node->text) + 1);
			strcpy(search->name, node->text);
			pos = (PRIMTYPE *) bsearch(search, prims, sizeof(prims)/sizeof(prims[0]), sizeof(PRIMTYPE), cmpPrim);
			free(search->name);
			free(search);
			//cmd?
			if (pos != NULL)
			{
				printf("\tFound: %s\n", pos->name);
				treetype = ntControl;
			}
			else
			{
				isVar = 0;
				if (strlen(node->text) == 2)
					isVar = (node->text[0] == 'V' && isdigit(node->text[1]));
				else if (strlen(node->text) == 3)
					isVar = (node->text[0] == 'V' && isdigit(node->text[1]) && isdigit(node->text[2]));
				if (isVar)
					printf("\tVariable %s found.\n", node->text);
				else
					printf("\tNot found: %s\n", node->text);
			}
			// var?
			
			// 
			break;
		case NUM:
			// convert to numeric
			treetype = ntNumber;
			break;
		case LBR:
			break;
		case RBR:
			break;
		default:
			return NIL;
	}
	
	TNODE *newNode = (TNODE *)malloc(sizeof(TNODE));
	//newNode->ident = ident;
	newNode->type = treetype;
	newNode->left = NULL;
	newNode->right = NULL;
	newNode->next = NULL;
	return newNode;
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


