/*
	main.c: main program
	Created by: Jeff Miller, 2/10/2013
*/

#include "parse.h"
#include "tree.h"
#include "rover.h"

char fName[MAXSTR + 1];

int main(int argc, char *argv[])
{
	if (argc != 2)
	{
		fprintf(stderr, "Usage: %s fileName\n", argv[0]);
		exit(1);
	}
	/* store the filename for later use */
	if (strlen(argv[1]) > MAXSTR)
	{
		fprintf(stderr, "Error: File name %s is too large\n", argv[1]);
		exit(1);
	}
	strcpy(fName, argv[1]);
	
	TEXTNODE *list = fileReadCommands(fName);
	TNODE *tree = makeFlatTree(list);
	//printTree(tree, 0);
	destroyTextList(list);
	tree = makeParseTree(tree);
	printTree(tree, 0);
	
	return 0;
} //int main