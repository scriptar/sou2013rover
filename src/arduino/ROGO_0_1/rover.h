/*
	rover.h: rover include file
	Created by: Jeff Miller, 2/10/2013
*/
#ifndef ROVER_H
#define ROVER_H

/*** includes ***/
#include "Arduino.h"
#include <WProgram.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
// Arduino-Supplied Servo Library
// URL: http://arduino.cc/en/Reference/Servo
#include <Servo.h>
// Third-Party Ultrasonic Sensor Library
// URL:http://playground.arduino.cc/Code/NewPing
#include <NewPing.h>

// ***************************************
// Arduino Pin Declarations
// ***************************************
#define PIN_BATT_SENSE          A0
//#define PIN_BLUETOOTH_RECEIVE  0
//#define PIN_BLUETOOTH_TRANSMIT 1
#define PIN_ULTRA_ECHO           4  // Arduino pin tied to echo pin on the ultrasonic sensor.
#define PIN_ULTRA_TRIGGER        5  // Arduino pin tied to trigger pin on the ultrasonic sensor.
#define PIN_BATT_LED_INDICATOR   6 // Indicator for battery check
#define PIN_SERVO_RIGHT          9 // By design. PWM is disabled on these pins because of Servo.h. 
#define PIN_SERVO_LEFT           10 // http://arduino.cc/en/Reference/Servo

// ***************************************
// Constant Declarations
// ***************************************
#define ULTRA_MAX_DISTANCE 10 // Maximum distance we want to ping for (in centimeters). Maximum sensor distance is rated at 400-500cm.
#define SERVO_MAX 2000 // Servo Maximum Pulse Width. Maybe need to adjust/test this more
#define SERVO_MIN 1000 // Servo Minimum Pulse Width. Maybe need to adjust/test this more

/*** type definitions ***/
struct rover_state {
	double x, y, heading;
};

extern Servo servoR; //create servo objects
extern Servo servoL;
extern int setR;
extern int setL;
extern boolean pingFlag;
extern NewPing sonar; // NewPing setup of pins and maximum distance.


/*** function definitions ***/

void forward(int countF);
void reverse(int countR);
void left(int degreeL);
void right(int degreeR);
void pause(int Us);
void LZ_Aim();
void batCheck();
void pingCheck();

#endif
