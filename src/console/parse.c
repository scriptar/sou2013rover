/*
	parse.c: Rogo parser routines
	Created by: Jeff Miller, 2/10/2013
*/

#include "parse.h"

#define isdigit(ch)			(ch >= '0' && ch <= '9')
#define isalphaupper(ch)	(ch >= 'A' && ch <= 'Z')
#define isalphalower(ch)	(ch >= 'a' && ch <= 'z')

/*
Description: This function reads a Rogo program from a file.
	It reads and filters-out characters that are not whitelisted.
*/
char **fileReadCommands(const char* fileName)
{
	/* open the file for reading */
	FILE* fp;
	if ((fp = fopen(fileName, "r")) == NULL)
	{
		fprintf(stderr, "ERROR: file %s not found\n", fileName);
		exit(1);
	}
	
	printf("Parsing %s\n", fileName);
	fflush(stdout);
	
	int good, i, start, end, count = 0;
	char linebuf[MAXSTR + 1];
	char *chPtr;
	char **cmds = malloc(sizeof(char)*(MAXCMDS + 1)); //TODO: convert cmds to linked list...
	for (i = 0; i < MAXCMDS + 1; i++)
		cmds[i] = NULL;
	
	while (fgets(linebuf, MAXSTR + 1, fp) != NULL)
	{
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
				good |= isdigit(*chPtr);
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
				}
				if (!good)
					*chPtr = '\0';
				*chPtr++;
				end++;
			}
			//read each command from buffer and add to cmds array
			for (i = 0, start = -1; i <= end; i++)
			{
				if (linebuf[i] != '\0' && start == -1)
					start = i;
				if (linebuf[i] == '\0' && start != -1)
				{
					//printf("%s\n", linebuf + start);
					if (count < MAXCMDS)
					{
						cmds[count] = (char *) malloc(strlen(linebuf + start) + 1);
						strcpy(cmds[count], linebuf + start);
						count += 1;
					}
					start = -1;
				}
			}
		}
		if (feof(fp))
			break;
	}
	
	fclose(fp);
	
	return cmds;
}
