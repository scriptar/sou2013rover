#include <Servo.h>

Servo servoR; //create servo objects
Servo servoL;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);
  servoR.attach(7); //attach right servo to pin 7
  servoL.attach(8);
}

void loop() {
  // put your main code here, to run repeatedly: 
  servoR.writeMicroseconds(1500);
  servoL.writeMicroseconds(1500);
}
