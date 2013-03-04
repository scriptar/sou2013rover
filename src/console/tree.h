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

typedef enum {ntUnknown, ntControl, ntOp, ntNumber, ntVar, ntFunc, ntList} NodeType;

/*** type definitions ***/

typedef struct treeNode {
	NodeType type;
	struct treeNode *left;
	struct treeNode *right;
	struct treeNode *next;
	struct {
	    short minargs;
	    short defargs;
		short maxargs;
		short priority;
		struct treeNode *(*prim_funtion) ();
	} prim;
} TNODE;

#define NIL (TNODE *)0

typedef struct primitive {
    char *name;
    short minargs;
    short defargs;
    short maxargs;
    short priority;
    TNODE *(*prim_function) ();
} PRIMTYPE;

/*** function definitions ***/
TNODE *makeTree(const TEXTNODE *list);
int cmpPrim(const void *p1, const void *p2);
TNODE *newTreeNode(const TEXTNODE *node);
TNODE *makeExpNode(TNODE *parent, TNODE *leftChild, TNODE *rightChild);
TNODE *setNextNode(TNODE *currentNode, TNODE *nextNode);
TNODE *getMulNode(TNODE *args);
TNODE *getAddNode(TNODE *args);
TNODE *getSubNode(TNODE *args);
TNODE *getDivideNode(TNODE *args);
TNODE *getLessNode(TNODE *args);
TNODE *getEqualNode(TNODE *args);
TNODE *getGreaterNode(TNODE *args);
TNODE *getLessEqualNode(TNODE *args);
TNODE *getNotEqualNode(TNODE *args);
TNODE *getGreaterEqualNode(TNODE *args);
TNODE *getAndNode(TNODE *args);
TNODE *getBackNode(TNODE *args);
TNODE *getForwardNode(TNODE *args);
TNODE *getIfNode(TNODE *args);
TNODE *getIfElseNode(TNODE *args);
TNODE *getLeftNode(TNODE *args);
TNODE *getLzAimNode(TNODE *args);
TNODE *getLzFireNode(TNODE *args);
TNODE *getNotNode(TNODE *args);
TNODE *getOrNode(TNODE *args);
TNODE *getPauseNode(TNODE *args);
TNODE *getRepeatNode(TNODE *args);
TNODE *getRightNode(TNODE *args);

#endif