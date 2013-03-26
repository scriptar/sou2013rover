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

#define DEBUG				0
#define STOP_PRIORITY		0
#define CONTROL_PRIORITY	1
#define PREFIX_PRIORITY		2
#define INFIX_PRIORITY		3
#define NUM_ROGOVARS		100

typedef enum {NT_UNKNOWN, NT_CONTROL, NT_OP, NT_NUM, NT_VAR, NT_FUNC, NT_LIST_START, NT_LIST_END} NodeType;
typedef enum {DT_UNKNOWN, DT_INT, DT_DBL} DataType;

/*** type definitions ***/

typedef struct treeNode {
	NodeType ntype;
	DataType dtype;
	short varIdx;
	int nInteger;
	double nRational;
	struct treeNode *left;
	struct treeNode *right;
	struct treeNode *prev;
	struct treeNode *next;
	int primIdx;
	short numargs;
	short priority;
} TNODE;

#define NIL (TNODE *)0

typedef struct primitive {
    char *name;
    short numargs;
    short priority;
    TNODE *(*prim_function) ();
} PRIMTYPE;

double rogo_vars[NUM_ROGOVARS + 1];

/*** function definitions ***/
TNODE *makeFlatTree(const TEXTNODE *list);
TNODE *makeParseTree(TNODE *tree);
void printTree(TNODE *tree, int depth);
void destroyTree(TNODE *tree);
const char *dispNodeType(const NodeType type);
int cmpPrim(const void *p1, const void *p2);
TNODE *newTreeNode(const TEXTNODE *node);
TNODE *makeExpNode(TNODE *parent, TNODE *leftChild, TNODE *rightChild);
TNODE *setNextNode(TNODE *currentNode, TNODE *nextNode);
void execTree(TNODE *tree);
TNODE *exec(TNODE *current, char fcn);
TNODE *evalNodeValue(TNODE *current);
int evalNodeValueInt(TNODE *current);
TNODE *execMulNode(TNODE *current);
TNODE *execAddNode(TNODE *current);
TNODE *execSubNode(TNODE *current);
TNODE *execDivideNode(TNODE *current);
TNODE *execLessNode(TNODE *current);
TNODE *execAssignNode(TNODE *current);
TNODE *execEqualNode(TNODE *current);
TNODE *execGreaterNode(TNODE *current);
TNODE *execLessEqualNode(TNODE *current);
TNODE *execNotEqualNode(TNODE *current);
TNODE *execGreaterEqualNode(TNODE *current);
TNODE *execAndNode(TNODE *current);
TNODE *execBackNode(TNODE *current);
TNODE *execForwardNode(TNODE *current);
TNODE *execIfNode(TNODE *current);
TNODE *execIfElseNode(TNODE *current);
TNODE *execLeftNode(TNODE *current);
TNODE *execLzAimNode(TNODE *current);
TNODE *execLzFireNode(TNODE *current);
TNODE *execNotNode(TNODE *current);
TNODE *execOrNode(TNODE *current);
TNODE *execPauseNode(TNODE *current);
TNODE *execRepeatNode(TNODE *current);
TNODE *execRightNode(TNODE *current);

#endif
