/*
 An Alternate ROGO rover sketch. 
 Combines code from various test sketches, as well implementing Jeff's interpreter
 
 Ryan D.
 
 Changelog
 *********
 13/03/29 - Initial Revision. Receives Rogo scripts over Bluetooth and prints them to console.
 */

// Arduino-Supplied Servo Library
// URL: 
#include <Servo.h>

// Third-Party Ultrasonic Sensor Library
// URL:
#include <NewPing.h>

// ***************************************
// Arduino Pin Declarations
// ***************************************
#define PIN_BLUETOOTH_RECEIVE           0
#define PIN_BLUETOOTH_TRANSMIT          1
#define PIN_ULTRASONIC_FORWARD_ECHO     2
#define PIN_ULTRASONIC_FORWARD_TRIGGER  3
#define PIN_LED_BATTERY_INDICATOR       4
#define PIN_SERVO_LEFT                  5
#define PIN_SERVO_RIGHT                 6
//Analog Battery Read = A0

// ***************************************
// Constant Declarations
// ***************************************
// Ultrasonic Maximum Ping Distance(in centimeters). Maximum sensor distance is rated at 400-500cm.
#define ULTRASONIC_MAX_DISTANCE       100
// Set Servo Max/Min Pulse Widths
#define SERVO_MIN_PULSE_WIDTH        1000
#define SERVO_MAX_PULSE_WIDTH        2000
// ***************************************
// Variables
// ***************************************
// Servo objects
Servo servoLeft; 
Servo servoRight;

// Battery Level
// TODO Explain Battery level value
float batteryLevel = 0; 
// Ultrasonic Sensor Object
NewPing ultrasonicForward(PIN_ULTRASONIC_FORWARD_TRIGGER, PIN_ULTRASONIC_FORWARD_ECHO, ULTRASONIC_MAX_DISTANCE);

// ***************************************
// Arduino Sketch Setup
// ***************************************
void setup(){
  // Set Pin Modes
  pinMode(PIN_LED_BATTERY_INDICATOR, OUTPUT);
  // Begin Bluetooth Serial Communication
  Serial.begin(115200);
  // Attach Servo Objects To Arduino Pins and Set Servo Min/Max Pulse Speed
  servoLeft.attach(PIN_SERVO_LEFT, SERVO_MIN_PULSE_WIDTH, SERVO_MAX_PULSE_WIDTH);
  servoRight.attach(PIN_SERVO_RIGHT, SERVO_MIN_PULSE_WIDTH, SERVO_MAX_PULSE_WIDTH);
  servoLeft.write(90);
  servoRight.write(90);
  // Perform Battery Check
  batteryCheck(PIN_LED_BATTERY_INDICATOR); 
}

// ***************************************
// Arduino Sketch Main Loop
// ***************************************
void loop(){
  if(Serial.available()){
    byte input = Serial.read(); 
  }
  
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

//servoLeft.writeMicroseconds(1500); 
}


// ***************************************
// Movement Functions
// ***************************************
void movementForward(int distance){
  for(int i=0;i<=distance;i++){
    servoLeft.write(75); 
    servoRight.write(180); 
    delay(50);
  }
  movementStop();
}

void movementReverse(int distance){
  for(int i=0;i<distance;i++){
    servoRight.write(59);
    servoLeft.write(180); 
    delay(333);
  }
  movementStop();
}

void movementRotateLeft(int angle){
  for(int i=0;i<=angle;i++){
    servoRight.write(180); 
    servoLeft.write(107);
    delay(8);
  }
  movementStop();
}

void movementRotateRight(int angle){
  for(int i=0;i<=angle;i++){
    servoLeft.write(73);
    servoRight.write(0);
    delay(8);
  }
  movementStop();
}

void movementStop(){
  servoLeft.write(90);
  servoRight.write(90);
}

// ***************************************
// Functions
// ***************************************
// Battery Check Routine
// Full charge = 3 blinks
// Medium charge = 2 blinks
// Low charge = 1 blink
// Led remains on if batteries need replacement 
void batteryCheck(int LED_Indicator_pin){
  batteryLevel = analogRead(A0) * .0049; //Read analog input and convert to input voltage value
  // If above 4.0 input volts (8v), three blinks
  if(batteryLevel >= 4.0){
    for(int i = 0;i<3;i++){
      digitalWrite(LED_Indicator_pin, HIGH); 
      delay(125);
      digitalWrite(LED_Indicator_pin, LOW);
      delay(125);
    }
  }
  // If between 3.0 and 4.0 input volts (6-8v), two blinks
  if(batteryLevel >= 3.0 && batteryLevel < 4.0){
    for(int i = 0;i<2;i++){
      digitalWrite(LED_Indicator_pin, HIGH); 
      delay(125);
      digitalWrite(LED_Indicator_pin, LOW);
      delay(125);
    }
  }
  // If between 2.6 and 3.0 input volts (5.2-6v), one blink
  if(batteryLevel >= 2.6 && batteryLevel < 3.0){
    digitalWrite(LED_Indicator_pin, HIGH); 
    delay(125);
    digitalWrite(LED_Indicator_pin, LOW);
    delay(125);
  }
  // If below 2.5 input volts(5v), warning led on
  if(batteryLevel < 2.5){
    digitalWrite(LED_Indicator_pin, HIGH); 
  }
}



