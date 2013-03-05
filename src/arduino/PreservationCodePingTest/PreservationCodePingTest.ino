//Preservation Code - Ping Test
//written by Shaun Wolff

//Moves forward until it gets within 10cm of something (in front of ultrasonic)
#include <Servo.h>
#include <NewPing.h>

Servo servo1; //create servo objects
Servo servo2;
float bLevel = 0; //Battery Level
int set1 = 90; //set servo speed to 0
int set2 = 90;
int pingFlag = 0;
#define ULTRA_TRIGGER_PIN  5  // Arduino pin tied to trigger pin on the ultrasonic sensor.
#define ULTRA_ECHO_PIN     2  // Arduino pin tied to echo pin on the ultrasonic sensor.
#define ULTRA_MAX_DISTANCE 10 // Maximum distance we want to ping for (in centimeters). Maximum sensor distance is rated at 400-500cm.
NewPing sonar(ULTRA_TRIGGER_PIN, ULTRA_ECHO_PIN, ULTRA_MAX_DISTANCE); // NewPing setup of pins and maximum distance.


void setup()
{
  Serial.begin(115200);
  servo1.attach(7); //attach right servo to pin 7
  servo2.attach(8); //attach left servo to pin 8
  pinMode(4, OUTPUT);
}

void loop()
{
  batCheck(); //Battery check. Led On if below 5v.
  if(pingFlag == 0)
  {
    //move rover forward if nothing in front of rover (ultrasonic sensor) 
    set1 = 180; //servo1 full forward
    set2 = 0; //servo2 full forward
    pingFlag = pingCheck(); //check ultrasonic sensor
    delay(100); //probably replaced with other code/function calls
    servo1.write(set1); //feed servos speed setting
    servo2.write(set2);
  }
  else //stop if something in front of rover (ultrasonic sensor)
  {
    pingFlag = pingCheck(); //check ultrasonic sensor
    delay(100); //probably replaced with other code/function calls
    servo1.write(90); //feed servos speed setting
    servo2.write(90);
  }
}
  
void batCheck()
{
  //Battery Monitor
  //warning led attached to pin 3
  bLevel = analogRead(A0); //Read analog input in var
  bLevel = bLevel * .0049; //convert to corresponding input voltage value
  if(bLevel < 2.50) analogWrite(4, 1024); //check if below 2.5 (5v), warning led on
  else {digitalWrite(4, LOW);} //no warning led
//  Serial.println(bLevel);  //possible sensor value to android app
}

int pingCheck()
{
  sonar.ping();  // Send ping. Possible sensor value to android app
  if(digitalRead(2) == LOW) return 1;
  else {return 0;}
}
