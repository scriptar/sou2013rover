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

/*** defines ***/

#define STOP_PRIORITY		0
#define CONTROL_PRIORITY	1
#define PREFIX_PRIORITY		2

typedef long int NODETYPES;
#define NT_ROOT		(NODETYPES)0001
#define NT_CONTROL	(NODETYPES)0002
#define NT_OP		(NODETYPES)0004
#define NT_NUMBER	(NODETYPES)0010
#define NT_VAR		(NODETYPES)0020
#define NT_FUNC		(NODETYPES)0040
#define NT_LIST		(NODETYPES)0100

/*** type definitions ***/

typedef struct rogonode {
	NODETYPES nodetype;
	struct rogonode *params;
	struct rogonode *next;
	struct {
	    short minargs;
	    short defargs;
		short maxargs;
		short priority;
		struct rogonode *(*prim_funtion) ();
	} prim;
} RNODE;

typedef struct primitive {
    char *name;
    short minargs;
    short defargs;
    short maxargs;
    short priority;
    RNODE *(*prim_function) ();
} PRIMTYPE;

/*** function definitions ***/
RNODE *newnode(NODETYPES type);
RNODE *getRootNode(void);
RNODE *getMulNode(RNODE *args);
RNODE *getAddNode(RNODE *args);
RNODE *getSubNode(RNODE *args);
RNODE *getDivideNode(RNODE *args);
RNODE *getLessNode(RNODE *args);
RNODE *getEqualNode(RNODE *args);
RNODE *getGreaterNode(RNODE *args);
RNODE *getLessEqualNode(RNODE *args);
RNODE *getNotEqualNode(RNODE *args);
RNODE *getGreaterEqualNode(RNODE *args);
RNODE *getAndNode(RNODE *args);
RNODE *getBackNode(RNODE *args);
RNODE *getForwardNode(RNODE *args);
RNODE *getIfNode(RNODE *args);
RNODE *getIfElseNode(RNODE *args);
RNODE *getLeftNode(RNODE *args);
RNODE *getLzAimNode(RNODE *args);
RNODE *getLzFireNode(RNODE *args);
RNODE *getNotNode(RNODE *args);
RNODE *getOrNode(RNODE *args);
RNODE *getPauseNode(RNODE *args);
RNODE *getRepeatNode(RNODE *args);
RNODE *getRightNode(RNODE *args);

#endif