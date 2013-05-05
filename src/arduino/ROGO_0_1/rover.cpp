/*
	rover.c: rover state and movement routine
*/

#include <WProgram.h>
#include "rover.h"

Servo servoR; //create servo objects
Servo servoL;
Servo servoLZ;
int setR = 90; //set servo speed to 0
int setL = 90;
boolean safetyFlagUltra = true;
boolean safetyFlagIR = true;
NewPing sonar(PIN_ULTRA_TRIGGER, PIN_ULTRA_ECHO, ULTRA_MAX_DISTANCE); // NewPing setup of pins and maximum distance.
ROVERSTATE rover = {0};
  
// ***************************************
// Functions
// ***************************************
void forward(int countF)
{
  
  for(int i=0;i<=countF;i++){
    if(Serial.available()){
      return;
    }
    setR = 180; //servo1 full forward
    setL = 0; //servo2 full forward
    sensorCheck();
    if(safetyFlagUltra && safetyFlagIR){
        servoR.write(setR); //feed servos speed setting
        servoL.write(setL); 
        delay(100);
      }
    else{
        pause(1000);
      }
  }
  pause(250);
}

void reverse(int countR)
{
    for(int i=0;i<=countR;i++){
      if(Serial.available()){
        return;
      }      
      setR = 0; //servo1 full reverse
      setL = 180; //servo2 full reverse
           servoR.write(setR); //feed servos speed setting
           servoL.write(setL); 
           delay(100);
    }
    pause(250);
}

void left(int degreeL)
{
  setR = 180;
  setL = 180;
  for(int i=0;i<=degreeL;i++){
    if(Serial.available()){
      return;
    }
    servoR.write(setR); 
    servoL.write(setL);
    delay(5);
  }
  pause(250);
}

void right(int degreeR)
{
  setR = 0;
  setL = 0;
  for(int i=0;i<=degreeR;i++){
    if(Serial.available()){
      return;
    }
    servoR.write(setR);
    servoL.write(setL);
    delay(5);
  }
  pause(250);
}

void pause(int Us)
{
  setR = 90; //servo1 stop
  setL = 90; //servo2 stop
  servoR.write(setR); //feed servos speed setting
  servoL.write(setL);
  delay(Us);
}

void LZAim(int degreeLZ)
{
  servoLZ.write(degreeLZ);
  delay(350);
}

void LZFire(int state)
{
  if(state == 1)
  {
    digitalWrite(PIN_LZ_FIRE, HIGH);
  }
  else
  {
    digitalWrite(PIN_LZ_FIRE, LOW);
  }
}
//void battCheck()
//{
//  //Battery Monitor
//  //warning led attached to pin 6
//  float bLevel = 0; //Battery Level
//  bLevel = analogRead(PIN_BATT_SENSE); //Read analog input in var
//  bLevel = bLevel * .0049; //convert to corresponding input voltage value
//  if(bLevel >= 4.0) //check if above 4.0 input volts (8v), three blinks
//  {
//    for (int i=0; i <= 3; i++){
//      digitalWrite(PIN_BATT_LED_INDICATOR, HIGH); 
//      delay(125);
//      digitalWrite(PIN_BATT_LED_INDICATOR, LOW);
//      delay(125);
//    }
//  }
//  if(bLevel >= 3.0 && bLevel < 4.0) //check if between 3.0 and 4.0 input volts (6-8v), two blinks
//  {
//    for (int i=0; i <= 2; i++){
//      digitalWrite(PIN_BATT_LED_INDICATOR, HIGH); 
//      delay(125);
//      digitalWrite(PIN_BATT_LED_INDICATOR, LOW);
//      delay(125);
//    }
//  }
//  if(bLevel >= 2.6 && bLevel < 3.0) //check if between 2.6 and 3.0 input volts (5.2-6v), one blink
//  {
//    digitalWrite(PIN_BATT_LED_INDICATOR, HIGH); 
//    delay(125);
//    digitalWrite(PIN_BATT_LED_INDICATOR, LOW);
//    delay(125);
//  }
//  if(bLevel < 2.5) //check if below 2.5 input volts(5v), warning led on
//  {
//    digitalWrite(PIN_BATT_LED_INDICATOR, HIGH); 
//  }
//  //  Serial.println(2 * bLevel);  //possible sensor value to android app
//}

void sensorCheck()
{
  pingCheck();
  irCheck();
}

void sensorSend()
{
  Serial.println("start");
  Serial.println("pingF");
  Serial.println(rover.pingRangeF);
  Serial.println("irFL");
  Serial.println(rover.irFL);
  Serial.println("irFR");
  Serial.println(rover.irFR);
  Serial.println("end");
}
void pingCheck()
{
  unsigned int uS =   sonar.ping_median();  // Send ping. Possible sensor value to android app
  rover.pingRangeF = uS / US_ROUNDTRIP_CM; // Convert ping time to distance in cm and print result (0 = outside set distance range)
  if(rover.pingRangeF == 0)
  {
//    Serial.println("Out of Range");
    safetyFlagUltra = true;
  } 

  else if(rover.pingRangeF <= 10) 
  {
//    Serial.print("Ping: ");
//    Serial.print(rover.pingRangeF); // Ping returned, uS result in ping_result, convert to cm with US_ROUNDTRIP_CM.
//    Serial.println("cm, STOP!!!");
    safetyFlagUltra = false; 
  }
  else
  {
//    Serial.print("Ping: ");
//    Serial.print(rover.pingRangeF); // Ping returned, uS result in ping_result, convert to cm with US_ROUNDTRIP_CM.
//    Serial.println("cm");
    safetyFlagUltra = true;
  }
}

void irCheck()
{
  rover.irFL = analogRead(PIN_LIR_SENSOR_READ); // read the state of the tcrt5000
  rover.irFR = analogRead(PIN_RIR_SENSOR_READ);
//  Serial.print(rover.irFL); //Print the state of the tcrt5000
//  Serial.print(", ");
//  Serial.println(rover.irFR);
  if(rover.irFL > 850 | rover.irFR > 850)
  {
    safetyFlagIR = false;
  }
  else
  {
    safetyFlagIR = true;
  }
}
