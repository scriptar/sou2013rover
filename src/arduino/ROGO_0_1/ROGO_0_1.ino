/*
  Official ROGO rover sketch.
 
  Project Information:
  https://github.com/scriptar/sou2013rover
*/

// Arduino-Supplied Servo Library
// URL: http://arduino.cc/en/Reference/Servo
#include <Servo.h>

// Third-Party Ultrasonic Sensor Library
// URL:http://playground.arduino.cc/Code/NewPing
#include <NewPing.h>

#include "parse.h"
#include "tree.h"
#include "rover.h"

// ***************************************
// Arduino Pin Declarations
// ***************************************
#define PIN_BATT_SENSE          A0
//#define PIN_BLUETOOTH_RECEIVE  0
//#define PIN_BLUETOOTH_TRANSMIT 1
#define PIN_ULTRA_ECHO           4  // Arduino pin tied to echo pin on the ultrasonic sensor.
#define PIN_ULTRA_TRIGGER        5  // Arduino pin tied to trigger pin on the ultrasonic sensor.
#define PIN_BATT_LED_INDICATOR   6 // Indicator for battery check
#define PIN_SERVO_RIGHT          7
#define PIN_SERVO_LEFT           8

// ***************************************
// Constant Declarations
// ***************************************
#define ULTRA_MAX_DISTANCE 10 // Maximum distance we want to ping for (in centimeters). Maximum sensor distance is rated at 400-500cm.
#define SERVO_MAX 2000 // Servo Maximum Pulse Width. Maybe need to adjust/test this more
#define SERVO_MIN 1000 // Servo Minimum Pulse Width. Maybe need to adjust/test this more

// ***************************************
// Variables
// ***************************************
Servo servoR; //create servo objects
Servo servoL;
float bLevel = 0; //Battery Level
int setR = 90; //set servo speed to 0
int setL = 90;
boolean pingFlag = true;
NewPing sonar(PIN_ULTRA_TRIGGER, PIN_ULTRA_ECHO, ULTRA_MAX_DISTANCE); // NewPing setup of pins and maximum distance.

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
        Repeat 18[Foward 2 Right 20]. Gets close...
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

// ***************************************
// Functions
// ***************************************
void forward(int countF)
{
  Serial.print("Executing Forward: ");
  Serial.println(countF);
  for(int i=0;i<=countF;i++){
    setR = 180; //servo1 full forward
    setL = 59; //servo2 full forward
//    pingCheck();
//          if(pingFlag){
           servoR.write(setR); //feed servos speed setting
           servoL.write(setL); 
           delay(333);
          }
//          else {
//            pause(1000);
 //         }
//  }
  pause(10);
}

void reverse(int countR)
{
  Serial.print("Executing Reverse: ");
  Serial.println(countR);
    for(int i=0;i<countR;i++){
      setR = 59; //servo1 full reverse
      setL = 180; //servo2 full reverse
//      pingCheck();
 //         if(pingFlag){
           servoR.write(setR); //feed servos speed setting
           servoL.write(setL); 
           delay(333);
           }
  //         else {
   //          pause(1000);
  //         }
 //   }
    pause(10);
}

void left(int degreeL)
{
  setR = 180;
  setL = 107;
  for(int i=0;i<=degreeL;i++){
    servoR.write(setR); 
    servoL.write(setL);
    delay(8);
  }
  pause(10);
}

void right(int degreeR)
{
  setR = 0;
  setL = 73;
  for(int i=0;i<=degreeR;i++){
    servoR.write(setR);
    servoL.write(setL);
    delay(8);
  }
  pause(10);
}

void pause(int Us)
{
  setR = 90; //servo1 stop
  setL = 90; //servo2 stop
  servoR.write(setR); //feed servos speed setting
  servoL.write(setL);
  delay(Us);
}

void LZ_Up()
{

}
void batCheck()
{
  //Battery Monitor
  //warning led attached to pin 6
  bLevel = analogRead(PIN_BATT_SENSE); //Read analog input in var
  bLevel = bLevel * .0049; //convert to corresponding input voltage value
  if(bLevel >= 4.0) //check if above 4.0 input volts (8v), three blinks
  {
    digitalWrite(PIN_BATT_LED_INDICATOR, HIGH); 
    delay(125);
    digitalWrite(PIN_BATT_LED_INDICATOR, LOW);
    delay(125);
    digitalWrite(PIN_BATT_LED_INDICATOR, HIGH); 
    delay(125);
    digitalWrite(PIN_BATT_LED_INDICATOR, LOW);
    delay(125);
    digitalWrite(PIN_BATT_LED_INDICATOR, HIGH); 
    delay(125);
    digitalWrite(PIN_BATT_LED_INDICATOR, LOW);
    delay(125);
  }
  if(bLevel >= 3.0 && bLevel < 4.0) //check if between 3.0 and 4.0 input volts (6-8v), two blinks
  {
    digitalWrite(PIN_BATT_LED_INDICATOR, HIGH); 
    delay(125);
    digitalWrite(PIN_BATT_LED_INDICATOR, LOW);
    delay(125);
    digitalWrite(PIN_BATT_LED_INDICATOR, HIGH); 
    delay(125);
    digitalWrite(PIN_BATT_LED_INDICATOR, LOW);
    delay(125);
  }
  if(bLevel >= 2.6 && bLevel < 3.0) //check if between 2.6 and 3.0 input volts (5.2-6v), one blink
  {
    digitalWrite(PIN_BATT_LED_INDICATOR, HIGH); 
    delay(125);
    digitalWrite(PIN_BATT_LED_INDICATOR, LOW);
    delay(125);
  }
  if(bLevel < 2.5) //check if below 2.5 input volts(5v), warning led on
  {
    digitalWrite(PIN_BATT_LED_INDICATOR, HIGH); 
  }
  //  Serial.println(2 * bLevel);  //possible sensor value to android app
}

void pingCheck()
{
  sonar.ping();  // Send ping. Possible sensor value to android app
  if(digitalRead(PIN_ULTRA_ECHO) == LOW) pingFlag = false;
  else {
    pingFlag = true;
  }
}


