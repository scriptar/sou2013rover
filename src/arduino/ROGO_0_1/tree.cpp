/*
	tree.c: Rogo tree node structures
 	Created by: Jeff Miller, 2/24/2013
 */

#include "tree.h"
#include "parse.h"
#include "rover.h"

PRIMTYPE prims[] = {
  { "", 0, 0, NULL  },
  { "-", 2, INFIX_PRIORITY + 3, execSubNode  },
  { "*", 2, INFIX_PRIORITY + 4, execMulNode  },
  { "/", 2, INFIX_PRIORITY + 4, execDivideNode  },
  { "+", 2, INFIX_PRIORITY + 3, execAddNode  },
  { "<", 2, INFIX_PRIORITY + 1, execLessNode  },
  { "<=", 2, INFIX_PRIORITY + 1, execLessEqualNode  },
  { "<>", 2, INFIX_PRIORITY + 1, execNotEqualNode  },
  { "=", 2, PREFIX_PRIORITY, execAssignNode  },
  { "==", 2, INFIX_PRIORITY + 1, execEqualNode  },
  { ">", 2, INFIX_PRIORITY + 1, execGreaterNode  },
  { ">=", 2, INFIX_PRIORITY + 1, execGreaterEqualNode  },
  { "AND", 2, INFIX_PRIORITY + 2, execAndNode  },
  { "BACK", 1, PREFIX_PRIORITY, execBackNode  },
  { "BK", 1, PREFIX_PRIORITY, execBackNode  },
  { "FD", 1, PREFIX_PRIORITY, execForwardNode  },
  { "FORWARD", 1, PREFIX_PRIORITY, execForwardNode  },
  { "IF", 2, CONTROL_PRIORITY, execIfNode  },
  { "IFELSE", 3, CONTROL_PRIORITY, execIfElseNode  },
  { "LEFT", 1, PREFIX_PRIORITY, execLeftNode  },
  { "LT", 1, PREFIX_PRIORITY, execLeftNode  },
  { "LZAIM", 1, PREFIX_PRIORITY, execLzAimNode  },
  { "LZFIRE", 1, PREFIX_PRIORITY, execLzFireNode  },
  { "NOT", 1, INFIX_PRIORITY + 3, execNotNode  },
  { "OR", 2, INFIX_PRIORITY + 2, execOrNode  },
  { "PAUSE", 0, PREFIX_PRIORITY, execPauseNode  },
  { "REPEAT", 2, CONTROL_PRIORITY, execRepeatNode  },
  { "RIGHT", 1, PREFIX_PRIORITY, execRightNode  },
  { "RT", 1, PREFIX_PRIORITY, execRightNode  }
};

double rogo_vars[NUM_ROGOVARS + 1];

TNODE *makeFlatTree(TEXTNODE *list)
{
  TEXTNODE *listnode = list;
  TEXTNODE *next;

  //no need to qsort, already presorted...
  qsort(prims, sizeof(prims)/sizeof(prims[0]), sizeof(PRIMTYPE), cmpPrim);
  if (DEBUG)
    Serial.print("Making Syntax Tree...\n");
  //create command tree from list array
  TNODE *tree = NIL;
  TNODE *current = NIL;
  //split text into expressions and commands...
  while (listnode)
  {
    if (DEBUG)
    {
      Serial.print(listnode->text);
      Serial.print(" ");
      Serial.println(dispPrimType(listnode->type));
    }
    if (tree == NIL)
    {
      current = tree = newTreeNode(listnode);
    }
    else
    {
      current = setNextNode(current, newTreeNode(listnode));
    }
    free(listnode->text);
    next = listnode->next;
    free(listnode);
    listnode = next;
  }
  return tree;
}

TNODE *makeParseTree(TNODE *tree)
{
  //set parent and child nodes...
  int priority;
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
    if (current->ntype == NT_LIST_START)
    {
      listCurrent = current;
      listCount = 0;
      while (listCurrent)
      {
        switch (listCurrent->ntype)
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
            if (right->next != NULL)
              right->next->prev = current;
            current->next = right->next; //list points to node after end of the list
            right->prev->next = NULL; //the "next" of the last element in list should point to nothing
            right->prev = NULL; //the list end should point to nothing
            right->next = NULL; //the list end should point to nothing
            left = makeParseTree(left); //inner list is its own tree, parse it
            if (current->left != left)
              current->left = left; //fix tree if root changed
          }
          break;
        default:
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
        switch (current->ntype)
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
          /* no break */
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
              //if (opcmd->ntype == NT_CONTROL)
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
        case NT_NUM:
        case NT_VAR:

          break;
        default:
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
      Serial.print("  ");
    switch (current->ntype)
    {
    case NT_OP:
    case NT_CONTROL:
      if (current->primIdx != -1)
        Serial.print(prims[current->primIdx].name);
      else
        Serial.print("OP/CMD?");
      break;
    case NT_NUM:
      switch (current->dtype)
      {
      case DT_INT:
        Serial.print(current->nInteger);
        break;
      case DT_DBL:
        Serial.print(current->nRational);
        break;
      default: 
        break;
      }
      break;
    case NT_VAR:
      Serial.print("V[");
      Serial.print(current->varIdx);
      Serial.print("]");
      break;
    case NT_LIST_START:
      Serial.print("[");
      break;
    case NT_LIST_END:
      Serial.print("]");
      break;
    default:
      Serial.print("ECHO?");
      break;
    }
    //printf(" Type: %s Priority: %d Depth: %d\n", dispNodeType(current->ntype), current->priority, depth);
    Serial.print(" ");
    Serial.println(dispNodeType(current->ntype));
    if (current->left != NULL)
    {
      for (i = 0; i < depth + 1; i++)
        Serial.print("  ");
      Serial.print("Left Child\n");
      printTree(current->left, depth + 1);
    }
    if (current->right != NULL)
    {
      for (i = 0; i < depth + 1; i++)
        Serial.print("  ");
      Serial.print("Right Child\n");
      printTree(current->right, depth + 1);
    }
    current = current->next;
  }
}

void destroyTree(TNODE *tree)
{
  TNODE *current = tree;
  TNODE *next;
  while (current)
  {
    if (current->left != NULL)
      destroyTree(current->left);
    if (current->right != NULL)
      destroyTree(current->right);
    next = current->next;
    current->prev = NULL;
    current->next = NULL;
    current->left = NULL;
    current->right = NULL;
    free(current);
    current = next;
  }
}

const char *dispNodeType(const NodeType type)
{
  switch (type)
  {
  case NT_UNKNOWN: 
    return "UNKNOWN";
  case NT_CONTROL: 
    return "CONTROL";
  case NT_OP: 
    return "OP";
  case NT_NUM: 
    return "NUM";
  case NT_VAR: 
    return "VAR";
  case NT_FUNC: 
    return "FUNC";
  case NT_LIST_START: 
    return "LIST_START";
  case NT_LIST_END: 
    return "LIST_END";
  default: 
    return "TYPE?";
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
  DataType dtype = DT_INT;
  int primIdx = -1, i, isVar, nInt = 0;
  int varIdx = -1;
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
      primIdx = (int)(pos - prims);
      switch (node->type)
      {
      case OP: 
        ntype = NT_OP; 
        break;
      case ID: 
        ntype = NT_CONTROL; 
        break;
      default: 
        break;
      }
    }
    else
    {
      isVar = 0;
      if (strlen(node->text) == 2)
        isVar = (node->text[0] == 'V' && isdigit1(node->text[1]));
      else if (strlen(node->text) == 3)
        isVar = (node->text[0] == 'V' && isdigit1(node->text[1]) && isdigit1(node->text[2]));
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
    ntype = NT_NUM;
    dtype = DT_INT;
    nInt = atoi(node->text);
    if (nInt == INT_MAX || nInt == INT_MIN)
      dtype = DT_DBL;
    else
      for (i = 0; i < strlen(node->text); i++)
        if (node->text[i] == '.')
          dtype = DT_DBL;
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
  newNode->ntype = ntype;
  newNode->dtype = dtype;
  newNode->varIdx = varIdx;
  newNode->nInteger = nInt;
  newNode->nRational = nDbl;
  newNode->left = NULL;
  newNode->right = NULL;
  newNode->prev = NULL;
  newNode->next = NULL;
  newNode->primIdx = primIdx;
  newNode->numargs = 0;
  newNode->priority = 0;
  if (pos != NULL)
  {
    newNode->numargs = pos->numargs;
    newNode->priority = pos->priority;
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

void execTree(TNODE *tree)
{
  TNODE *current = tree;
  while (current)
  {
    switch (current->ntype)
    {
    case NT_OP:
    case NT_CONTROL:
      if (current->primIdx != -1)
        prims[current->primIdx].prim_function(current);
      else
        Serial.print("OP/CMD?");
      break;
    case NT_NUM:
      switch (current->dtype)
      {
      case DT_INT:
        Serial.print(current->nInteger);
        break;
      case DT_DBL:
        Serial.print(current->nRational);
        break;
      default: 
        break;
      }
      break;
    case NT_VAR:
      Serial.print("V[");
      Serial.print(current->varIdx);
      Serial.print("]");
      break;
    case NT_LIST_START:
      execTree(current->left);
      if (current->left != NULL)
      {
        current->dtype = current->left->dtype;
        switch (current->left->dtype)
        {
        case DT_INT:
          current->nInteger = current->left->nInteger;
          break;
        case DT_DBL:
          current->nRational = current->left->nRational;
          break;
        default: 
          break;
        }
      }
      break;
    case NT_LIST_END:
      Serial.print("]");
      break;
    default:
      Serial.print("EX?");
      break;
    }
    current = current->next;
  }
}

TNODE *loadVar(TNODE *current)
{
  if (current->varIdx != -1 && current->varIdx <= NUM_ROGOVARS)
  {
    current->nRational = rogo_vars[current->varIdx];
    current->nInteger = (int)round(current->nRational);
    current->dtype = DT_DBL;
    if ((int)ceil(current->nRational) == (int)floor(current->nRational))
      current->dtype = DT_INT;
    if (DEBUG)
      switch (current->dtype)
      {
      case DT_INT:
        Serial.print("\nLoading Variable #");
        Serial.print(current->varIdx);
        Serial.print("Value: ");
        Serial.print(current->nInteger);
        break;
      case DT_DBL: 
        Serial.print("\nLoading Variable #");
        Serial.print(current->varIdx);
        Serial.print("Value: ");
        Serial.print(current->nRational);
        break;
      default: 
        break;
      }
  }
  return current;
}

TNODE *exec(TNODE *current, char fcn)
{
  int retCurrentNode = 1,
  ival = 0, ileft = 0, iright = 0;
  double fval = 0.0,
  fleft = 0.0,
  fright = 0.0;
  DataType retDataType = DT_UNKNOWN;

  if (current->left != NULL)
  {
    if (current->left->primIdx != -1)
    {
      prims[current->left->primIdx].prim_function(current->left);
    }
    loadVar(current->left);
    switch (current->left->dtype)
    {
    case DT_INT:
      ileft = current->left->nInteger;
      fleft = (double)ileft;
      break;
    case DT_DBL:
      fleft = current->left->nRational;
      ileft = (int)fleft;
      break;
    default: 
      break;
    }
    if (retDataType != DT_DBL)
      retDataType = current->left->dtype;
  }
  if (current->right != NULL)
  {
    if (current->right->primIdx != -1)
    {
      prims[current->right->primIdx].prim_function(current->right);
    }
    loadVar(current->right);
    switch (current->right->dtype)
    {
    case DT_INT:
      iright = current->right->nInteger;
      fright = (double)iright;
      break;
    case DT_DBL:
      fright = current->right->nRational;
      iright = (int)fright;
      break;
    default: 
      break;
    }
    if (retDataType != DT_DBL)
      retDataType = current->right->dtype;
  }

  switch(fcn) {
  case '-': //subtract
    ival = ileft - iright;
    fval = fleft - fright;
    goto PRINT_FCN;
    break;
  case '+': //add
    ival = ileft + iright;
    fval = fleft + fright;
    goto PRINT_FCN;
    break;
  case '*': //multiply
    ival = ileft * iright;
    fval = fleft * fright;
    goto PRINT_FCN;
    break;
  case '/': //divide
    if (iright != 0)
      ival = ileft / iright;
    if (fright != 0.0)
      fval = fleft / fright;
    goto PRINT_FCN;
    break;
  case '>': //greater
    ival = (ileft > iright);
    goto PRINT_FCN;
    break; 
  case '<': //less
    ival = (ileft < iright);
    goto PRINT_FCN;
    break; 
  case ':': //assign
    if (DEBUG)
      switch (retDataType)
      {
      case DT_INT: 
        printf("\nv[%d]:=%d", current->left->varIdx, iright); 
        break;
      case DT_DBL: 
        printf("\nv[%d]:=%lf", current->left->varIdx, fright); 
        break;
      default: 
        break;
      }
    fval = fright;
    if (current->left->varIdx != -1 && current->left->varIdx <= NUM_ROGOVARS)
      rogo_vars[current->left->varIdx] = fval;
    break;
  case '=': //eq
    ival = (ileft == iright);
    goto PRINT_FCN;
    break;
  case '#': //neq
    ival = (ileft != iright);
    goto PRINT_FCN;
    break;
  case 'L': //lte
    ival = (ileft <= iright);
    goto PRINT_FCN;
    break;
  case 'G': //gte
    ival = (ileft >= iright);
    goto PRINT_FCN;
    break;
  case '&': //and
    //printf("\n%d%c%d", ileft, fcn, iright);
    ival = (ileft && iright);
    break;
  case '|': //or
    //printf("\n%d%c%d", ileft, fcn, iright);
    ival = (ileft || iright);
    break;
  case '!': //not
    //printf("\n%c%d", fcn, ileft);
    ival = !ileft;
    break;
  case '?': //if

    break;
  case '^': //ifelse

      break;
  case 'R': //repeat

    break;
  case 'P': //pause

      break;
  default:
    break;
  }
  goto SKIP_PRINT_FCN;
  PRINT_FCN:
  if (DEBUG)
    switch (retDataType)
    {
    case DT_INT: 
      Serial.print("\n");
      Serial.print(ileft);
      Serial.print(fcn);
      Serial.print(iright);
      Serial.print("=");
      Serial.print(ival); 
      break;
    case DT_DBL: 
      Serial.print("\n");
      Serial.print(fleft);
      Serial.print(fcn);
      Serial.print(fright);
      Serial.print("=");
      Serial.print(fval); 
      break;
    default: 
      break;
    }
  SKIP_PRINT_FCN:
  
  if (retDataType == DT_DBL)
    ival = (int)fval;
  else
    fval = (double)ival;

  if (retCurrentNode)
  {
    current->dtype = retDataType;
    current->nInteger = ival;
    current->nRational = fval;
    return current;
  }
  else
  {
    TNODE *newNode = (TNODE *)malloc(sizeof(TNODE));
    newNode->ntype = NT_NUM;
    newNode->dtype = retDataType;
    newNode->varIdx = -1;
    newNode->nInteger = ival;
    newNode->nRational = fval;
    newNode->left = NULL;
    newNode->right = NULL;
    newNode->prev = NULL;
    newNode->next = NULL;
    newNode->primIdx = -1;
    newNode->numargs = 0;
    newNode->priority = 0;
    return newNode;
  }
}

TNODE *evalNodeValue(TNODE *current)
{
  TNODE *node;
  if (current->ntype == NT_LIST_START && current->left != NULL)
    current = evalNodeValue(current->left);
  if (current->primIdx != -1)
    node = prims[current->primIdx].prim_function(current);
  else if (current->varIdx != -1)
    node = loadVar(current);
  else
    node = current;
  return node;
}

int evalNodeValueInt(TNODE *current)
{
  switch (current->dtype)
  {
  case DT_INT: 
    return current->nInteger;
  case DT_DBL: 
    return (int)round(current->nRational);
  default: 
    return 0;
  }
}

TNODE *execMulNode(TNODE *current)
{
  return exec(current, '*');
}

TNODE *execAddNode(TNODE *current)
{
  return exec(current, '+');
}

TNODE *execSubNode(TNODE *current)
{
  return exec(current, '-');
}

TNODE *execDivideNode(TNODE *current)
{
  return exec(current, '/');
}

TNODE *execLessNode(TNODE *current)
{
  return exec(current, '<');
}

TNODE *execAssignNode(TNODE *current)
{
  return exec(current, ':');
}

TNODE *execEqualNode(TNODE *current)
{
  return exec(current, '=');
}

TNODE *execGreaterNode(TNODE *current)
{
  return exec(current, '>');
}

TNODE *execLessEqualNode(TNODE *current)
{
  return exec(current, 'L');
}

TNODE *execNotEqualNode(TNODE *current)
{
  return exec(current, '#');
}

TNODE *execGreaterEqualNode(TNODE *current)
{
  return exec(current, 'G');
}

TNODE *execAndNode(TNODE *current)
{
  return exec(current, '&');
}

TNODE *execBackNode(TNODE *current)
{
  int i = evalNodeValueInt(evalNodeValue(current->left));
  Serial.print("BK ");
  Serial.println(i);
  reverse(i);
  return current;
}

TNODE *execForwardNode(TNODE *current)
{
  int i = evalNodeValueInt(evalNodeValue(current->left));
  Serial.print("FD ");
  Serial.println(i);
  forward(i);
  return current;
}

TNODE *execIfNode(TNODE *current)
{
  if (evalNodeValueInt(evalNodeValue(current->left)))
  {
    if (DEBUG)
      printf("\nTest successful.");
    execTree(current->right);
  }
  else
    if (DEBUG)
      printf("\nTest failed.");
  return current;
}

TNODE *execIfElseNode(TNODE *current)
{
  if (DEBUG)
    printf("\nIFELSE");
  //return exec(current, '^');
  return current;
}

TNODE *execLeftNode(TNODE *current)
{
  int degrees = evalNodeValueInt(evalNodeValue(current->left));
  Serial.print("LT ");
  Serial.println(degrees);
  left(degrees);
  return current;
}

TNODE *execLzAimNode(TNODE *current)
{
  int degrees = evalNodeValueInt(evalNodeValue(current->left));
  Serial.print("LZAIM ");
  Serial.println(degrees);
  LZAim(degrees);
  return current;
}

TNODE *execLzFireNode(TNODE *current)
{
  if (DEBUG)
    printf("\nLZFIRE");
  return current;
}

TNODE *execNotNode(TNODE *current)
{
  return exec(current, '!');
}

TNODE *execOrNode(TNODE *current)
{
  return exec(current, '|');
}

TNODE *execPauseNode(TNODE *current)
{
  if (DEBUG)
    printf("\nPAUSE");
  return exec(current, 'P');
}

TNODE *execRepeatNode(TNODE *current)
{
  int i = evalNodeValueInt(evalNodeValue(current->left));
  if (DEBUG)
    printf("\nfor(1..%d) {", i);
  for (; i > 0; i--)
  {
    execTree(current->right);
  }
  if (DEBUG)
    printf("\n}");
  return current;
}

TNODE *execRightNode(TNODE *current)
{
  int degrees = evalNodeValueInt(evalNodeValue(current->left));
  Serial.print("RT ");
  Serial.println(degrees);
  right(degrees);
  return current;
}




