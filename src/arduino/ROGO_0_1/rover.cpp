/*
	rover.c: rover state and movement routines
	Created by: Jeff Miller, 2/24/2013
*/
#include <WProgram.h>
#include "rover.h"

Servo servoR; //create servo objects
Servo servoL;
int setR = 90; //set servo speed to 0
int setL = 90;
boolean pingFlag = true;
NewPing sonar(PIN_ULTRA_TRIGGER, PIN_ULTRA_ECHO, ULTRA_MAX_DISTANCE); // NewPing setup of pins and maximum distance.
  
// ***************************************
// Functions
// ***************************************
void forward(int countF)
{
  Serial.print("Executing Forward: ");
  Serial.println(countF);
  for(int i=0;i<=countF;i++){
    setR = 180; //servo1 full forward
    setL = 0; //servo2 full forward
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
      setR = 0; //servo1 full reverse
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

void LZ_Aim()
{

}
void batCheck()
{
  //Battery Monitor
  //warning led attached to pin 6
  float bLevel = 0; //Battery Level
  bLevel = analogRead(PIN_BATT_SENSE); //Read analog input in var
  bLevel = bLevel * .0049; //convert to corresponding input voltage value
  if(bLevel >= 4.0) //check if above 4.0 input volts (8v), three blinks
  {
    for (int i=0; i <= 3; i++){
      digitalWrite(PIN_BATT_LED_INDICATOR, HIGH); 
      delay(125);
      digitalWrite(PIN_BATT_LED_INDICATOR, LOW);
      delay(125);
    }
  }
  if(bLevel >= 3.0 && bLevel < 4.0) //check if between 3.0 and 4.0 input volts (6-8v), two blinks
  {
    for (int i=0; i <= 2; i++){
      digitalWrite(PIN_BATT_LED_INDICATOR, HIGH); 
      delay(125);
      digitalWrite(PIN_BATT_LED_INDICATOR, LOW);
      delay(125);
    }
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
