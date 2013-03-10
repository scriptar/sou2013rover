/*
	tree.h: Rogo tree include file
	Created by: Jeff Miller, 2/24/2013
*/
#ifndef TREE_H
#define TREE_H

/*** includes ***/
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <math.h>
#include "parse.h"

/*** defines ***/

#define STOP_PRIORITY		0
#define CONTROL_PRIORITY	1
#define PREFIX_PRIORITY		2
#define INFIX_PRIORITY		3 

typedef enum {NT_UNKNOWN, NT_CONTROL, NT_OP, NT_INT, NT_DBL, NT_VAR, NT_FUNC, NT_LIST_START, NT_LIST_END} NodeType;

/*** type definitions ***/

typedef struct treeNode {
	NodeType type;
	short varIdx;
	int nInteger;
	double nRational;
	struct treeNode *left;
	struct treeNode *right;
	struct treeNode *prev;
	struct treeNode *next;
	short numargs;
	short priority;
	struct treeNode *(*prim_function) ();
} TNODE;

#define NIL (TNODE *)0

typedef struct primitive {
    char *name;
    short numargs;
    short priority;
    TNODE *(*prim_function) ();
} PRIMTYPE;

/*** function definitions ***/
TNODE *makeFlatTree(const TEXTNODE *list);
TNODE *makeParseTree(TNODE *tree);
void printTree(TNODE *tree, int depth);
const char *dispNodeType(const NodeType type);
int cmpPrim(const void *p1, const void *p2);
TNODE *newTreeNode(const TEXTNODE *node);
TNODE *makeExpNode(TNODE *parent, TNODE *leftChild, TNODE *rightChild);
TNODE *setNextNode(TNODE *currentNode, TNODE *nextNode);
TNODE *execMulNode(TNODE *args);
TNODE *execAddNode(TNODE *args);
TNODE *execSubNode(TNODE *args);
TNODE *execDivideNode(TNODE *args);
TNODE *execLessNode(TNODE *args);
TNODE *execAssignNode(TNODE *args);
TNODE *execEqualNode(TNODE *args);
TNODE *execGreaterNode(TNODE *args);
TNODE *execLessEqualNode(TNODE *args);
TNODE *execNotEqualNode(TNODE *args);
TNODE *execGreaterEqualNode(TNODE *args);
TNODE *execAndNode(TNODE *args);
TNODE *execBackNode(TNODE *args);
TNODE *execForwardNode(TNODE *args);
TNODE *execIfNode(TNODE *args);
TNODE *execIfElseNode(TNODE *args);
TNODE *execLeftNode(TNODE *args);
TNODE *execLzAimNode(TNODE *args);
TNODE *execLzFireNode(TNODE *args);
TNODE *execNotNode(TNODE *args);
TNODE *execOrNode(TNODE *args);
TNODE *execPauseNode(TNODE *args);
TNODE *execRepeatNode(TNODE *args);
TNODE *execRightNode(TNODE *args);

#endif