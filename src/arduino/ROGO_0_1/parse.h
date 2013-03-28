/*
	parse.h: Rogo parser include file
	Created by: Jeff Miller, 2/10/2013
*/
#ifndef PARSE_H
#define PARSE_H

/*** includes ***/
#include "Arduino.h"
#include <WProgram.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

/*** defines ***/
#define MAXSTR			64
#define isdigit1(ch)		(ch >= '0' && ch <= '9')
#define isalphaupper(ch)	(ch >= 'A' && ch <= 'Z')
#define isalphalower(ch)	(ch >= 'a' && ch <= 'z')

/*** type definitions ***/
typedef enum {NONE, ID, NUM, NEGATIVE, OP, LBR, RBR} PrimitiveType;

typedef struct textNode {
	char *text;
	PrimitiveType type;
	struct textNode *prev;
	struct textNode *next;
} TEXTNODE;

/*** function definitions ***/
TEXTNODE *serialReadCommands(void);
TEXTNODE *getNewTextNode(const char* text);
void splitTextOnPrimitives(TEXTNODE *list);
void destroyTextList(TEXTNODE *list);
const char *dispPrimType(const PrimitiveType ptype);

#endif

