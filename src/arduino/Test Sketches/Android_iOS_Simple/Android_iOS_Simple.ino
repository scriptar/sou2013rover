#include <Servo.h>

// ***********************
// Pin Declarations
// ***********************
// LED Display Pins
#define FORWARD_LED_PIN 7 
#define REVERSE_LED_PIN 6
#define LEFT_LED_PIN 5
#define RIGHT_LED_PIN 4
// Servo Control Pins
#define LEFT_SERVO 10
#define RIGHT_SERVO 11

// ****************************
// Variables
// ****************************
// Servo Objects
Servo leftServo;
Servo rightServo;
// Servo speeds, 90 = Still
int leftSpeed = 90;
int rightSpeed = 90;

// ****************************
// Program Initialization
// ****************************
void setup() {
  // Sets serial baud rate to 115200
  Serial.begin(115200);
  // Setting Pin Input/Output Mode
  pinMode(FORWARD_LED_PIN, OUTPUT);
  pinMode(REVERSE_LED_PIN, OUTPUT);
  pinMode(LEFT_LED_PIN, OUTPUT);
  pinMode(RIGHT_LED_PIN, OUTPUT);
  // Attach Servo Objects to Pins
  leftServo.attach(LEFT_SERVO); 
  rightServo.attach(RIGHT_SERVO);  
}

// ****************************
// Program Execution Loop
// ****************************
void loop() {

  // Receive Bluetooth Commands
  // ****************************
  // Sets LED indicators & Servo Action Flags
  // If buffered Bluetooth data exists, read it
  if(Serial.available()){
    byte input = Serial.read(); 
    switch (input){
    case 10: //Forward Start Received
      digitalWrite(FORWARD_LED_PIN, HIGH);
      forwardStart();
      break;
    case 15: //Forward Stop Received
      digitalWrite(FORWARD_LED_PIN, LOW);
      stopServos();
      break;
    case 20: //Reverse Start
      digitalWrite(REVERSE_LED_PIN, HIGH);
      reverseStart();
      break;
    case 25: //Reverse Stop
      digitalWrite(REVERSE_LED_PIN, LOW);
      stopServos();
      break;
    case 30: //Left Start
      digitalWrite(LEFT_LED_PIN, HIGH);
      leftStart();
      break;
    case 35: //Left Stop
      digitalWrite(LEFT_LED_PIN, LOW);
      stopServos();
      break;
    case 40: //Right Start
      digitalWrite(RIGHT_LED_PIN, HIGH);
      rightStart();
      break;
    case 45: //Right Stop
      digitalWrite(RIGHT_LED_PIN, LOW);
      stopServos();
      break;
    }
  } 
}

// ****************************
// Servo Functions
// ****************************
void forwardStart()
{
  leftSpeed = 0;
  rightSpeed = 180;
  leftServo.write(leftSpeed);
  rightServo.write(rightSpeed);
  delay(8);
}
void reverseStart()
{
  leftSpeed = 180;
  rightSpeed = 0;
  leftServo.write(leftSpeed);
  rightServo.write(rightSpeed);
}
void leftStart()
{
  leftSpeed = 180;
  rightSpeed = 180;
  leftServo.write(leftSpeed);
  rightServo.write(rightSpeed);
}
void rightStart()
{
  leftSpeed = 0;
  rightSpeed = 0;
  leftServo.write(leftSpeed);
  rightServo.write(rightSpeed);
}
void stopServos()
{
  leftSpeed = 90;
  rightSpeed = 90;
  leftServo.write(leftSpeed);
  rightServo.write(rightSpeed);
}
