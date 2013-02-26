/*
	parse.h: Rogo parser include file
	Created by: Jeff Miller, 2/10/2013
*/
#ifndef PARSE_H
#define PARSE_H

/*** includes ***/
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <math.h>
#include "tree.h"

/*** defines ***/
#define MAXSTR		100
#define MAXCMDS		100

/*** type definitions ***/

/*** function definitions ***/
char **fileReadCommands(const char* fileName);
RNODE *makeTree(char **cmds);
void appendNode(RNODE *parent, RNODE *child);
RNODE *makeNode(const char *txt);

#endif