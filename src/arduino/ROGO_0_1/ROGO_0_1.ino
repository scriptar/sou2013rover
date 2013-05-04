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
  servoLZ.attach(PIN_SERVO_LASER,600,2380);
//  battCheck(); //Battery level check. HIGH = 3 blinks, MED = 2, LOW = 1, Led On if batteries need replaced. 
  pinMode(PIN_LIR_SENSOR_READ, INPUT);  // initialize the tcrt5000 pins as an input, and 
  pinMode(PIN_RIR_SENSOR_READ, INPUT);
  digitalWrite(PIN_LIR_SENSOR_READ, HIGH); //turn on the internal pullup resistors
  digitalWrite(PIN_RIR_SENSOR_READ, HIGH);
  NewPing::timer_ms(SENSOR_CHECK_SPEED, sensorSend);
  servoLZ.write(0);
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
 04/20 - Added Ping and IR sensors to safety check
         for movement controls. pingCheck() and
         irCheck().
 04/24 - Bundled saftey check functions into one
         generic function call. Sensor data bundled
         into ROVERSTATE struct to be transmitted to
         app.
 05/01 - Using Timer2 to send data packet of sensor
         values(sensorSend()) regardless of roverstate
 */
// ***************************************
// Arduino Sketch Main Loop
// ***************************************
void loop()
{
  sensorCheck(); 
  delay(250);
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

