//Forward and Reverse motion test with servos
//written by Shaun Wolff
#include <Servo.h>

Servo servo1; //create servo objects
Servo servo2;
float bLevel = 0; //Battery Level
int set1 = 90; //set servo speed to 0
int set2 = 90; 

void setup()
{
  Serial.begin(9600);
  servo1.attach(2); //attach right servo to pin 2
  servo2.attach(4); //attach left servo to pin 4
  pinMode(3, OUTPUT);
}

void loop()
{
  //Battery Monitor
  //warning led attached to pin 3
  bLevel = analogRead(A0); //Read analog input in var
  bLevel = bLevel * .0049; //convert to corresponding voltage value
  if(bLevel < 2.50) analogWrite(3, 10); //check if below 5v, warning led on
  else {digitalWrite(3, LOW);} //no warning led
  
  //move rover forward 2s then backward 2s then stop 10s
  set1 = 180; //servo1 full forward
  set2 = 0; //servo2 full forward
  servo1.write(set1); //feed servos speed setting
  servo2.write(set2);
  delay(2000); //Wait 2s
  for(int i = 180; i > 90; i--) // fade stop
  {  
    servo1.write(i);
    int x = 180 - i;
    servo2.write(x);
    delay(15);
  }
  delay(2000); //Wait 2s
  set1 = 0; //servo1 full reverse
  set2 = 180; //servo2 full reverse
  servo1.write(set1); //feed servos speed setting
  servo2.write(set2);
  delay(2000); //Wait 2 sec
  for(int i = 180; i > 90; i--) // fade stop
  {  
    servo2.write(i);
    int x = 180 - i;
    servo1.write(x);
    delay(15);
  }
  delay(10000); //Wait 10s
  
}

