#include <Servo.h> 
 
Servo myservo1;  // create servo object to control a servo 
Servo myservo2;  // a maximum of eight servo objects can be created 
//continuous rotation servos position value, 0 =  
int pos1 = 90;    // variable to store the servo position 
int pos2 = 90;    // variable to store the servo position

void setup() 
{ 
  myservo1.attach(2);  // attaches the servo on pin 2 to the servo object
  myservo2.attach(4);  // attaches the servo on pin 4 to the servo object 
} 
 
 
void loop() 
{ 
  myservo1.write(pos1);
  myservo2.write(pos2);
} 
