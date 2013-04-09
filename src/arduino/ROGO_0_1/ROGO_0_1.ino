/*
  Official ROGO rover sketch.
 
  Project Information:
  https://github.com/scriptar/sou2013rover
*/
#include "parse.h"
#include "tree.h"
#include "rover.h"
#include <Servo.h>
#include <NewPing.h>

// ***************************************
// Arduino Sketch Setup
// ***************************************
void setup()
{
  Serial.begin(115200);
  servoR.attach(PIN_SERVO_RIGHT,SERVO_MIN, SERVO_MAX); //attach right servo to pin 7
  servoL.attach(PIN_SERVO_LEFT,SERVO_MIN,SERVO_MAX); //attach left servo to pin 8
  batCheck(); //Battery level check. HIGH = 3 blinks, MED = 2, LOW = 1, Led On if batteries need replaced. 
}

/* ************* READ HERE ***************
 03/9 - Changed movement functions to act more logoish' 
        Left and right accept degrees as parameters.
        Forward accepts "units"... 
        Currently has a program to "draw" a circle, 
        Repeat 18[Forward 2 Right 20]. Gets close...
        Forward sometimes doesnt execute
 03/29 - Parse Tree implemented and working. 
         Temporarily disabled ping stop motion
 */
// ***************************************
// Arduino Sketch Main Loop
// ***************************************
void loop()
{
  if(Serial.available()){
	TEXTNODE *list = serialReadCommands();
	TNODE *tree = makeFlatTree(list);
	tree = makeParseTree(tree);
	printTree(tree, 0);
	Serial.print("\nExecuting Tree...\n");
	execTree(tree);
	destroyTree(tree);
  }
}

