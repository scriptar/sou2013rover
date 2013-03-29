
// Arduino-Supplied Servo Library
// URL: 
#include <Servo.h>

// ***************************************
// Arduino Pin Declarations
// ***************************************
#define PIN_SERVO_LEFT                  5
#define PIN_SERVO_RIGHT                 6
//Analog Battery Read = A0

// ***************************************
// Constant Declarations
// ***************************************
// Set Servo Max/Min Pulse Widths
#define SERVO_MIN_PULSE_WIDTH        1000
#define SERVO_MAX_PULSE_WIDTH        2000
// ***************************************
// Variables
// ***************************************
// Servo objects
Servo servoLeft; 
Servo servoRight;


// ***************************************
// Arduino Sketch Setup
// ***************************************
void setup(){
  // Begin Bluetooth Serial Communication
  Serial.begin(115200);
  // Attach Servo Objects To Arduino Pins and Set Servo Min/Max Pulse Speed
  servoLeft.attach(PIN_SERVO_LEFT, SERVO_MIN_PULSE_WIDTH, SERVO_MAX_PULSE_WIDTH);
  servoRight.attach(PIN_SERVO_RIGHT, SERVO_MIN_PULSE_WIDTH, SERVO_MAX_PULSE_WIDTH);
  servoLeft.write(90);
  servoRight.write(90);
}

// ***************************************
// Arduino Sketch Main Loop
// ***************************************
void loop(){
  for(int i = 1350;i < 1650; i++){
    servoLeft.writeMicroseconds(i); 
    Serial.print("Left Servo: ");
    Serial.println(servoLeft.read());
    delay(10); 
  }
  for(int i = 1650;i > 1350; i--){
    servoLeft.writeMicroseconds(i); 
    Serial.print("Left Servo: ");
    Serial.println(servoLeft.read());
    delay(10); 
  }
}
