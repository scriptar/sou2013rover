//Forward and Reverse motion test with servos
//written by Shaun Wolff
#include <Servo.h>
#include <NewPing.h>

#define ULTRA_TRIGGER_PIN  5  // Arduino pin tied to trigger pin on the ultrasonic sensor.
#define ULTRA_ECHO_PIN     2  // Arduino pin tied to echo pin on the ultrasonic sensor.
#define ULTRA_MAX_DISTANCE 10 // Maximum distance we want to ping for (in centimeters). Maximum sensor distance is rated at 400-500cm.
#define SERVOR_PIN 7 //Arduino pin tied to right servo
#define SERVOL_PIN 8 //Arduino pin tied to left servo
#define BAT_MONITOR_LED_PIN 4 //Arduino pin tied to battery monitor warning led
#define BAT_MONITOR_PIN A0 //Arduino pin tied to battery monitor analog pickup

NewPing sonar(ULTRA_TRIGGER_PIN, ULTRA_ECHO_PIN, ULTRA_MAX_DISTANCE); // NewPing setup of pins and maximum distance.
Servo servoR; //create servo objects
Servo servoL;
float bLevel = 0; //Battery Level
volatile int set1 = 90; //set servo speed to 0
volatile int set2 = 90; 

void setup()
{
  Serial.begin(115200);
  servoR.attach(SERVOR_PIN); //attach right servo to pin
  servoL.attach(SERVOL_PIN); //attach left servo to pin
  pinMode(BAT_MONITOR_LED_PIN, OUTPUT);
  attachInterrupt(0, stopRover, LOW);
}

void loop()
{
  //Battery Monitor
  //warning led attached to pin 3
  bLevel = analogRead(BAT_MONITOR_PIN); //Read analog input in var
  bLevel = bLevel * .0049; //convert to corresponding voltage value
  if(bLevel < 2.50) analogWrite(BAT_MONITOR_LED_PIN, 10); //check if below 5v, warning led on
  else {digitalWrite(BAT_MONITOR_LED_PIN, LOW);} //no warning led
  
  //Start ultrasonic sensor pinging  
  unsigned int uS = sonar.ping();  // Send ping, get ping time in microseconds (uS).
  delay(200);
//  int pinVal = digitalRead(ULTRA_ECHO_PIN);  //Read state of echo pin
//  Serial.println(pinVal);

  //move rover forward 2s then backward 2s then stop 10s
  set1 = 180; //servo1 full forward
  set2 = 0; //servo2 full forward
  servoR.write(set1); //feed servos speed setting
  servoL.write(set2);
  delay(10000);
//  delay(2000); //Wait 2s
//  for(int i = 180; i > 90; i--) // fade stop
//  {  
//    servoR.write(i);
//    int x = 180 - i;
//    servoL.write(x);
//    delay(15);
//  }
//  delay(2000); //Wait 2s
//  set1 = 0; //servo1 full reverse
//  set2 = 180; //servo2 full reverse
//  servoR.write(set1); //feed servos speed setting
//  servoL.write(set2);
//  delay(2000); //Wait 2 sec
//  for(int i = 180; i > 90; i--) // fade stop
//  {  
//    servoL.write(i);
//    int x = 180 - i;
//    servoR.write(x);
//    delay(15);
//  }
//  delay(10000); //Wait 10s
  
}

void stopRover()
{
 servoR.write(90);
 servoL.write(90);
}

