/*
	parse.c: Rogo parser routines
	Created by: Jeff Miller, 2/10/2013
*/
#include "parse.h"

/*
Description: This function reads a Rogo program from a serial buffer.
	It reads and filters-out characters that are not whitelisted.
*/
TEXTNODE *serialReadCommands(void)
{
	int good, i, start, end;
	char linebuf[MAXSTR + 1];
	char *chPtr;
	TEXTNODE *list = NULL;
	TEXTNODE *current = NULL;
	
	while (1)
	{
		for (i = 0; i < MAXSTR; i++)
			linebuf[i] = '\0';
		i = 0;
		delay(100);
		while (Serial.available() > 0)
		{
			linebuf[i++] = Serial.read();
			if (i == MAXSTR)
				break;
			delay(30);
		}
		if (!i)
			break;
		//ignore lines starting with ;
		if (linebuf[0] != ';')
		{
			//convert all non-whitelisted characters into '\0'
			chPtr = linebuf;
			end = 0;
			while (*chPtr)
			{
				good = 0;
				if (isalphalower(*chPtr))
					*chPtr ^= ' '; //convert to upper case (ascii trick)
				good |= isalphaupper(*chPtr);
				good |= isdigit1(*chPtr);
				switch (*chPtr)
				{
					case '.':
					case '+':
					case '-':
					case '*':
					case '/':
					case '=':
					case '<':
					case '>':
					case '[':
					case ']':
						good = 1;
						break;
				}
				if (!good)
					*chPtr = '\0';
				chPtr++;
				end++;
			}
			//read each string from buffer and add to text list
			for (i = 0, start = -1; i <= end; i++)
			{
				if (linebuf[i] != '\0' && start == -1)
					start = i;
				if (linebuf[i] == '\0' && start != -1)
				{
					//printf("%s\n", linebuf + start);
					if (list == NULL)
					{
						list = current = getNewTextNode((const char *)(linebuf + start));
					}
					else
					{
						current->next = getNewTextNode(linebuf + start);
						current->next->prev = current;
						current = current->next;
					}
					splitTextOnPrimitives(current);
					while (current->next)
						current = current->next;
					start = -1;
				}
			}
		}
	}
	
	return list;
}

TEXTNODE *getNewTextNode(const char *text)
{
	TEXTNODE *node = (TEXTNODE *) malloc(sizeof(TEXTNODE));
	node->text = (char *) malloc(strlen(text) + 1);
	strcpy(node->text, text);
	node->prev = NULL;
	node->next = NULL;
	return node;
}

void splitTextOnPrimitives(TEXTNODE *node)
{
	TEXTNODE *newNode = NULL;
	int i;
	char ch, lastCh = '\0';
	int hasDecimal = 0;
	PrimitiveType ptype = NONE;
	
	for (i = 0; i < strlen(node->text); i++)
	{
		ch = node->text[i];
		if (isalphaupper(ch))
		{
			if (ptype == NONE || ptype == ID)
				ptype = ID;
			else
				goto SPLIT_PRIMITIVE;
		}
		if (isdigit1(ch))
		{
			if (ptype == NONE || ptype == NEGATIVE || ptype == NUM)
				ptype = NUM;
			else if (ptype == ID)
				ptype = ID;
			else
				goto SPLIT_PRIMITIVE;
		}
		switch (ch)
		{
			case '.':
				if ((ptype == NONE || ptype == NUM) && !hasDecimal)
					hasDecimal = 1;
				else
					goto SPLIT_PRIMITIVE;
				break;
			case '[':
				if (ptype == NONE)
					ptype = LBR;
				else
					goto SPLIT_PRIMITIVE;
				break;
			case ']':
				if (ptype == NONE)
					ptype = RBR;
				else
					goto SPLIT_PRIMITIVE;
				break;
			case '-':
				if (ptype == NONE)
					ptype = NEGATIVE;
				else
					goto SPLIT_PRIMITIVE;
				break;
			case '*':
			case '+':
			case '/':
			case '<':
			case '>':
				if (ptype == NONE || ptype == OP)
					ptype = OP;
				else
					goto SPLIT_PRIMITIVE;
				break;
			case '=':
				if (ptype == NONE || ptype == OP || ptype == NEGATIVE)
					ptype = OP;
				else
					goto SPLIT_PRIMITIVE;
				break;
		}
		lastCh = ch;
	}
	if (ptype == NEGATIVE)
		ptype = OP;
	if (ptype == ID && strcmp(node->text, "AND") == 0)
		ptype = OP;
	if (ptype == ID && strcmp(node->text, "OR") == 0)
		ptype = OP;
	if (ptype == ID && strcmp(node->text, "NOT") == 0)
		ptype = OP;
	node->type = ptype;
	return;
//place chars starting from node->text[i] into new linked node
SPLIT_PRIMITIVE:
	newNode = getNewTextNode(node->text + i);
	node->text[i] = '\0';
	if (ptype == NEGATIVE)
		ptype = OP;
	if (ptype == ID && strcmp(node->text, "AND") == 0)
		ptype = OP;
	if (ptype == ID && strcmp(node->text, "OR") == 0)
		ptype = OP;
	if (ptype == ID && strcmp(node->text, "NOT") == 0)
		ptype = OP;
	node->type = ptype;
	newNode->prev = node;
	newNode->next = node->next;
	node->next = newNode;
	splitTextOnPrimitives(newNode);
}

void destroyTextList(TEXTNODE *list)
{
	while (list)
	{
		free(list->text);
		TEXTNODE *next = list->next;
		free(list);
		list = next;
	}
}

const char *dispPrimType(const PrimitiveType ptype)
{
	switch (ptype)
	{
		case NONE: return "None";
		case ID: return "Ident";
		case NUM: return "Number";
		case NEGATIVE: return "Negative";
		case OP: return "Operator";
		case LBR: return "Left Brace";
		case RBR: return "Right Brace";
		default: return "???";
	}
}

