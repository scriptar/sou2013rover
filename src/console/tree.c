/*
	tree.c: Rogo tree node structures
	Created by: Jeff Miller, 2/24/2013
*/
#include <limits.h>
#include "tree.h"

PRIMTYPE prims[] = {
	{"-", 2, INFIX_PRIORITY + 3, execSubNode},
	{"*", 2, INFIX_PRIORITY + 4, execMulNode},
	{"/", 2, INFIX_PRIORITY + 4, execDivideNode},
	{"+", 2, INFIX_PRIORITY + 3, execAddNode},
	{"<", 2, INFIX_PRIORITY + 1, execLessNode},
	{"<=", 2, INFIX_PRIORITY + 1, execLessEqualNode},
	{"<>", 2, INFIX_PRIORITY + 1, execNotEqualNode},
	{"=", 2, PREFIX_PRIORITY, execAssignNode},
	{"==", 2, INFIX_PRIORITY + 1, execEqualNode},
	{">", 2, INFIX_PRIORITY + 1, execGreaterNode},
	{">=", 2, INFIX_PRIORITY + 1, execGreaterEqualNode},
	{"AND", 2, INFIX_PRIORITY + 2, execAndNode},
	{"BACK", 1, PREFIX_PRIORITY, execBackNode},
	{"BK", 1, PREFIX_PRIORITY, execBackNode},
	{"FD", 1, PREFIX_PRIORITY, execForwardNode},
	{"FORWARD", 1, PREFIX_PRIORITY, execForwardNode},
	{"IF", 2, CONTROL_PRIORITY, execIfNode},
	{"IFELSE", 3, CONTROL_PRIORITY, execIfElseNode},
	{"LEFT", 1, PREFIX_PRIORITY, execLeftNode},
	{"LT", 1, PREFIX_PRIORITY, execLeftNode},
	{"LZAIM", 1, PREFIX_PRIORITY, execLzAimNode},
	{"LZFIRE", 1, PREFIX_PRIORITY, execLzFireNode},
	{"NOT", 1, INFIX_PRIORITY + 3, execNotNode},
	{"OR", 2, INFIX_PRIORITY + 2, execOrNode},
	{"PAUSE", 0, PREFIX_PRIORITY, execPauseNode},
	{"REPEAT", 2, CONTROL_PRIORITY, execRepeatNode},
	{"RIGHT", 1, PREFIX_PRIORITY, execRightNode},
	{"RT", 1, PREFIX_PRIORITY, execRightNode},
	{"", 0, 0, 0},
	{"", 0, 0, 0},
	{"", 0, 0, 0},
	{"", 0, 0, 0},
	{"", 0, 0, 0},
	{"", 0, 0, 0}
};

TNODE *makeFlatTree(const TEXTNODE *list)
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
		//printf("List[%d]: %s (%s)\n", i++, listnode->text, dispPrimType(listnode->type));
		if (tree == NIL)
		{
			current = tree = newTreeNode(listnode);
		}
		else
		{
			current = setNextNode(current, newTreeNode(listnode));
		}
		listnode = listnode->next;
	}
	return tree;
}

TNODE *makeParseTree(TNODE *tree)
{
	//set parent and child nodes...
	short priority;
	TNODE *current = NULL;
	TNODE *opcmd = NULL;
	TNODE *left = NULL;
	TNODE *right = NULL;
	TNODE *listCurrent = NULL;
	int listCount;
	//handle lists first
	current = tree;
	while (current)
	{
		//any list beginnings?
		if (current->type == NT_LIST_START)
		{
			listCurrent = current;
			listCount = 0;
			while (listCurrent)
			{
				switch (listCurrent->type)
				{
					case NT_LIST_START:
						listCount++;
						break;
					case NT_LIST_END:
						listCount--;
						if (listCount == 0)
						{
							//end of list reached...
							left = current->next; //1st expression in list
							right = listCurrent; //list end
							makeExpNode(current, left, right); //make everything in list the left child of list start, right child is list end
							left->prev = NULL; //1st expression in list has no prior nodes
							current->next = right->next; //list points to node after end of the list
							right->prev->next = NULL; //the "next" of the last element in list should point to nothing
							right->prev = NULL; //the list end should point to nothing
							right->next = NULL; //the list end should point to nothing
							makeParseTree(left); //inner list is its own tree, parse it
						}
						break;
				}
				listCurrent = listCurrent->next;
			}
		}
		current = current->next;
	}
	
	//handle nodes from highest priority to lowest
	for (priority = INFIX_PRIORITY + 4; priority >= 0; priority--)
	{
		current = tree;
		while (current)
		{
			if (current->priority == priority)
			{
				switch (current->type)
				{
					case NT_OP:
						//swap 1st and 2nd to make the op the current
						opcmd = current;
						if (opcmd->numargs != 1) // (don't swap if op is is a "NOT")
						{
							left = current->prev;
							opcmd->prev = left->prev;
							if (opcmd->prev != NULL)
								opcmd->prev->next = opcmd;
							left->next = opcmd->next;
							if (left->next != NULL)
								left->next->prev = left;
							opcmd->next = left;
							left->prev = opcmd;
							if (left == tree)
								tree = opcmd; //fix tree if first node was left
						}
						//pass through...
					case NT_CONTROL:
						opcmd = current;
						left = current->next;
						right = current->next->next;
						if (opcmd->numargs == 1)
						{
							makeExpNode(opcmd, left, NULL);
							opcmd->next = left->next;
							if (opcmd->next != NULL)
								opcmd->next->prev = opcmd;
							left->prev = NULL;
							left->next = NULL;
						}
						else if (opcmd->numargs == 2)
						{
							makeExpNode(opcmd, left, right);
							if (left != NULL)
							{
								left->prev = NULL;
								//don't do this if type == NT_OP...
								//still buggy, EQ...
								//if (opcmd->type == NT_CONTROL)
									left->next = NULL;
							}
							if (right != NULL)
							{
								opcmd->next = right->next;
								if (opcmd->next)
									opcmd->next->prev = opcmd;
								right->prev = NULL;
								right->next = NULL;
							}
							else
							{
								opcmd->next = NULL;
							}
						}
						break;
					case NT_INT:
					case NT_DBL:
					case NT_VAR:

						break;
				}
			}
			current = current->next;
		}
	}
	return tree;
}

void printTree(TNODE *tree, int depth)
{
	TNODE *current = tree;
	int i;
	while (current)
	{
		for (i = 0; i < depth; i++)
			printf("  ");
		switch (current->type)
		{
			case NT_OP:
			case NT_CONTROL:
				if (current->prim_function != NULL)
					current->prim_function();
				else
					printf("OP/CMD?");
				break;
			case NT_INT:
				printf("%d", current->nInteger);
				break;
			case NT_DBL:
				printf("%lf", current->nRational);
				break;
			case NT_VAR:
				printf("V[%d]", current->varIdx);
				break;
			case NT_LIST_START:
				printf("[");
				break;
			case NT_LIST_END:
				printf("]");
				break;
			default:
				printf("???");
		}
		//printf(" Type: %s Priority: %d Depth: %d\n", dispNodeType(current->type), current->priority, depth);
		printf(" (%s)\n", dispNodeType(current->type));
		if (current->left != NULL)
		{
			for (i = 0; i < depth + 1; i++)
				printf("  ");
			printf("Left Child\n");
			printTree(current->left, depth + 1);
		}
		if (current->right != NULL)
		{
			for (i = 0; i < depth + 1; i++)
				printf("  ");
			printf("Right Child\n");
			printTree(current->right, depth + 1);
		}
		current = current->next;
	}
}

const char *dispNodeType(const NodeType type)
{
	switch (type)
	{
		case NT_UNKNOWN: return "UNKNOWN";
		case NT_CONTROL: return "CONTROL";
		case NT_OP: return "OP";
		case NT_INT: return "INT";
		case NT_DBL: return "DBL";
		case NT_VAR: return "VAR";
		case NT_FUNC: return "FUNC";
		case NT_LIST_START: return "LIST_START";
		case NT_LIST_END: return "LIST_END";
		default: return "???";
	}
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
	NodeType ntype = NT_UNKNOWN;
	int i, isVar, nInt = 0;
	short varIdx = -1;
	double nDbl = 0.0;

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
				//printf("\tFound: %s\n", pos->name);
				switch (node->type)
				{
					case OP: ntype = NT_OP; break;
					case ID: ntype = NT_CONTROL; break;
				}
			}
			else
			{
				isVar = 0;
				if (strlen(node->text) == 2)
					isVar = (node->text[0] == 'V' && isdigit(node->text[1]));
				else if (strlen(node->text) == 3)
					isVar = (node->text[0] == 'V' && isdigit(node->text[1]) && isdigit(node->text[2]));
				if (isVar)
				{
					varIdx = atoi(node->text + 1);
					//printf("\tVariable v%d found.\n", varIdx);
					ntype = NT_VAR;
				}
				//else
					//printf("\tNot found: %s\n", node->text);
			}
			break;
		case NUM:
			// convert to numeric
			ntype = NT_INT;
			nInt = atoi(node->text);
			if (nInt == INT_MAX || nInt == INT_MIN)
				ntype = NT_DBL;
			else
				for (i = 0; i < strlen(node->text); i++)
					if (node->text[i] == '.')
						ntype = NT_DBL;
			nDbl = atof(node->text);
			break;
		case LBR:
			ntype = NT_LIST_START;
			break;
		case RBR:
			ntype = NT_LIST_END;
			break;
		default:
			return NIL;
	}
	TNODE *newNode = (TNODE *)malloc(sizeof(TNODE));
	newNode->type = ntype;
	newNode->varIdx = varIdx;
	newNode->nInteger = nInt;
	newNode->nRational = nDbl;
	newNode->left = NULL;
	newNode->right = NULL;
	newNode->prev = NULL;
	newNode->next = NULL;
	newNode->numargs = 0;
	newNode->priority = 0;
	newNode->prim_function = NULL;
	if (pos != NULL)
	{
		newNode->numargs = pos->numargs;
		newNode->priority = pos->priority;
		newNode->prim_function = pos->prim_function;
	}
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
	nextNode->prev = currentNode;
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



TNODE *execMulNode(TNODE *args)
{
	printf("MUL");
	return NIL;
}

TNODE *execAddNode(TNODE *args)
{
	printf("ADD");
	return NIL;
}

TNODE *execSubNode(TNODE *args)
{
	printf("SUB");
	return NIL;
}

TNODE *execDivideNode(TNODE *args)
{
	printf("DIV");
	return NIL;
}

TNODE *execLessNode(TNODE *args)
{
	printf("LT");
	return NIL;
}

TNODE *execAssignNode(TNODE *args)
{
	printf("SET");
	return NIL;
}

TNODE *execEqualNode(TNODE *args)
{
	printf("EQ");
	return NIL;
}

TNODE *execGreaterNode(TNODE *args)
{
	printf("GT");
	return NIL;
}

TNODE *execLessEqualNode(TNODE *args)
{
	printf("LTE");
	return NIL;
}

TNODE *execNotEqualNode(TNODE *args)
{
	printf("NEQ");
	return NIL;
}

TNODE *execGreaterEqualNode(TNODE *args)
{
	printf("GTE");
	return NIL;
}

TNODE *execAndNode(TNODE *args)
{
	printf("AND");
	return NIL;
}

TNODE *execBackNode(TNODE *args)
{
	printf("BK");
	return NIL;
}

TNODE *execForwardNode(TNODE *args)
{
	printf("FD");
	return NIL;
}

TNODE *execIfNode(TNODE *args)
{
	printf("IF");
	return NIL;
}

TNODE *execIfElseNode(TNODE *args)
{
	printf("IFELSE");
	return NIL;
}

TNODE *execLeftNode(TNODE *args)
{
	printf("LT");
	return NIL;
}

TNODE *execLzAimNode(TNODE *args)
{
	printf("LZAIM");
	return NIL;
}

TNODE *execLzFireNode(TNODE *args)
{
	printf("LZFIRE");
	return NIL;
}

TNODE *execNotNode(TNODE *args)
{
	printf("NOT");
	return NIL;
}

TNODE *execOrNode(TNODE *args)
{
	printf("OR");
	return NIL;
}

TNODE *execPauseNode(TNODE *args)
{
	printf("PAUSE");
	return NIL;
}

TNODE *execRepeatNode(TNODE *args)
{
	printf("REPEAT");
	return NIL;
}

TNODE *execRightNode(TNODE *args)
{
	printf("RT");
	return NIL;
}


