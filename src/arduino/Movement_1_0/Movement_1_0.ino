//Movement - Version 1.0
//written by Shaun Wolff

#include <Servo.h>
#include <NewPing.h>

Servo servoR; //create servo objects
Servo servoL;
float bLevel = 0; //Battery Level
int setR = 90; //set servo speed to 0
int setL = 90;
boolean pingFlag = true;
#define ULTRA_TRIGGER_PIN  5  // Arduino pin tied to trigger pin on the ultrasonic sensor.
#define ULTRA_ECHO_PIN     2  // Arduino pin tied to echo pin on the ultrasonic sensor.
#define ULTRA_MAX_DISTANCE 10 // Maximum distance we want to ping for (in centimeters). Maximum sensor distance is rated at 400-500cm.
NewPing sonar(ULTRA_TRIGGER_PIN, ULTRA_ECHO_PIN, ULTRA_MAX_DISTANCE); // NewPing setup of pins and maximum distance.

void setup()
{
  Serial.begin(115200);
  servoR.attach(7); //attach right servo to pin 7
  servoL.attach(8); //attach left servo to pin 8
  pinMode(4, OUTPUT);
}

void loop()
{

/* ********************* READ HERE ****************************
   03/07 - Currently performing a Forward 5, Left 5, Forward 5
   then spins 360 degrees. Uses a battery check with led. Uses
   ping for perservation, forward facing only.
*/ 

 batCheck(); //Battery level check. HIGH = 3 blinks, MED = 2, LOW = 1, Led On if batteries need replaced. 
 
 for(int i=0;i<5;i++){ //forward 5
   pingCheck();
   if(pingFlag) forward();
   else {pause();}
 }
 pause();
 left(); //left 5
  for(int i=0;i<5;i++){
   pingCheck();
   if(pingFlag) forward();
   else {pause();}
 }
 pause();
 right();
  for(int i=0;i<5;i++){ //forward 5
   pingCheck();
   if(pingFlag) forward();
   else {pause();}
 }
 left(); //turn left 360
 left();
 left();
 left();
 for(int i=0;i<5;i++){ //wait 5 seconds
   pause();}

// for(int i=0;i<10;i++){
//   pingCheck();
//   if(pingFlag) reverse();
//   else {pause();}
// }
// for(int i=0;i<2;i++){
//   pause();}
}

void forward()
{
  setR = 180; //servo1 full forward
  setL = 0; //servo2 full forward
  servoR.write(setR); //feed servos speed setting
  servoL.write(setL);
  delay(333);
 }

void reverse()
{
  setR = 0; //servo1 full reverse
  setL = 180; //servo2 full reverse
  servoR.write(setR); //feed servos speed setting
  servoL.write(setL);
  delay(333);
}

void left()
{
  setR = 180;
  setL = 180;
  servoR.write(setR); //feed servos speed setting
  servoL.write(setL);
  delay(675);
  servoR.write(90);
  servoL.write(90);  
}

void right()
{
  setR = 0;
  setL = 0;
  servoR.write(setR); //feed servos speed setting
  servoL.write(setL);
  delay(675);
  servoR.write(90);
  servoL.write(90);  
}

void pause()
{
  setR = 90; //servo1 stop
  setL = 90; //servo2 stop
  servoR.write(setR); //feed servos speed setting
  servoL.write(setL);
  delay(1000);
}

void LZ_Up()
{
  
}
void batCheck()
{
  //Battery Monitor
  //warning led attached to pin 3
  bLevel = analogRead(A0); //Read analog input in var
  bLevel = bLevel * .0049; //convert to corresponding input voltage value
  if(bLevel >= 4.0) //check if above 4.0 input volts (8v), three blinks
  {
    digitalWrite(4, HIGH); 
    delay(125);
    digitalWrite(4, LOW);
    delay(125);
    digitalWrite(4, HIGH); 
    delay(125);
    digitalWrite(4, LOW);
    delay(125);
    digitalWrite(4, HIGH); 
    delay(125);
    digitalWrite(4, LOW);
    delay(125);
  }
  if(bLevel >= 3.0 && bLevel < 4.0) //check if between 3.0 and 4.0 input volts (6-8v), two blinks
  {
    digitalWrite(4, HIGH); 
    delay(125);
    digitalWrite(4, LOW);
    delay(125);
    digitalWrite(4, HIGH); 
    delay(125);
    digitalWrite(4, LOW);
    delay(125);
  }
  if(bLevel >= 2.6 && bLevel < 3.0) //check if between 2.6 and 3.0 input volts (5.2-6v), one blink
  {
    digitalWrite(4, HIGH); 
    delay(125);
    digitalWrite(4, LOW);
    delay(125);
  }
  if(bLevel < 2.5) //check if below 2.5 input volts(5v), warning led on
  {
    digitalWrite(4, HIGH); 
  }
//  Serial.println(2 * bLevel);  //possible sensor value to android app
}

int pingCheck()
{
  sonar.ping();  // Send ping. Possible sensor value to android app
  if(digitalRead(2) == LOW) pingFlag = false;
  else {pingFlag = true;}
}
