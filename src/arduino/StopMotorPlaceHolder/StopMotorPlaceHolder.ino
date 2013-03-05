#include <Servo.h>

Servo servo1; //create servo objects
Servo servo2;
volatile int set1 = 90; //set servo speed to 0
volatile int set2 = 90;

void setup() {
  // put your setup code here, to run once:
  servo1.attach(7); //attach right servo to pin 2
  servo2.attach(8); //attach left servo to pin 4

}

void loop() {
  // put your main code here, to run repeatedly: 
  servo1.write(set1); //feed servos speed setting
  servo2.write(set2);
  
}
