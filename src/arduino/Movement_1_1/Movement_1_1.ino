//Movement - Version 1.0
//written by Shaun Wolff

#include <Servo.h>
#include <NewPing.h>

#define ULTRA_TRIGGER_PIN  5  // Arduino pin tied to trigger pin on the ultrasonic sensor.
#define ULTRA_ECHO_PIN     2  // Arduino pin tied to echo pin on the ultrasonic sensor.
#define ULTRA_MAX_DISTANCE 10 // Maximum distance we want to ping for (in centimeters). Maximum sensor distance is rated at 400-500cm.

Servo servoR; //create servo objects
Servo servoL;
float bLevel = 0; //Battery Level
int setR = 90; //set servo speed to 0
int setL = 90;
boolean pingFlag = true;
NewPing sonar(ULTRA_TRIGGER_PIN, ULTRA_ECHO_PIN, ULTRA_MAX_DISTANCE); // NewPing setup of pins and maximum distance.

void setup()
{
  Serial.begin(115200);
  servoR.attach(7); //attach right servo to pin 7
  servoL.attach(8); //attach left servo to pin 8
  pinMode(4, OUTPUT);
  batCheck(); //Battery level check. HIGH = 3 blinks, MED = 2, LOW = 1, Led On if batteries need replaced. 
}

/* ********************* READ HERE ****************************
 03/9 - Changed movement functions to act more logoish' 
        Left and right accept degrees as parameters.
        Forward accepts "units"... 
        Currently has a program to "draw" a circle, 
        Repeat 18[Foward 2 Right 20]. Gets close...
        Forward sometimes doesnt execute
 */

void loop()
{
  for(int i=0;i<18;i++){
      forward(2);
      right(20);
    }
  pause(3000);
}


//---Functions-----------------------------------------------------
void forward(int countF)
{
  for(int i=0;i<countF;i++){
    setR = 180; //servo1 full forward
    setL = 59; //servo2 full forward
    pingCheck();
          if(pingFlag){
           servoR.write(setR); //feed servos speed setting
           servoL.write(setL); 
           delay(333);
          }
          else {
            pause(1000);
          }
  }
  pause(10);
}

void reverse(int countR)
{
    for(int i=0;i<countR;i++){
      setR = 59; //servo1 full reverse
      setL = 180; //servo2 full reverse
      pingCheck();
          if(pingFlag){
           servoR.write(setR); //feed servos speed setting
           servoL.write(setL); 
           delay(333);
           }
           else {
             pause(1000);
           }
    }
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

void pingCheck()
{
  sonar.ping();  // Send ping. Possible sensor value to android app
  if(digitalRead(2) == LOW) pingFlag = false;
  else {
    pingFlag = true;
  }
}


