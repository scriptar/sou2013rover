/* 
  Official ROGO rover sketch.
 
  Project Information:
  https://github.com/scriptar/sou2013rover
*/
#include "parse.h"
#include "tree.h"
#include "rover.h"
#include <Servo.h>
#include <NewPing.h>

// ***************************************
// Arduino Sketch Setup
// ***************************************
void setup()
{
  Serial.begin(115200);
  servoR.attach(PIN_SERVO_RIGHT,SERVO_MIN, SERVO_MAX); //attach right servo to pin 7
  servoL.attach(PIN_SERVO_LEFT,SERVO_MIN,SERVO_MAX); //attach left servo to pin 8
  servoLZ.attach(PIN_SERVO_LASER,600,2380); 
  pinMode(PIN_LIR_SENSOR_READ, INPUT);  // initialize the tcrt5000 pins as an input, and 
  pinMode(PIN_RIR_SENSOR_READ, INPUT);
  pinMode(PIN_LZ_FIRE, OUTPUT);
  pinMode(PIN_BATT_LED_INDICATOR, OUTPUT);
  digitalWrite(PIN_LIR_SENSOR_READ, HIGH); //turn on the internal pullup resistors
  digitalWrite(PIN_RIR_SENSOR_READ, HIGH);
  servoLZ.write(0);
}

/* ************* READ HERE ***************
 03/9 - Changed movement functions to act more logoish' 
        Left and right accept degrees as parameters.
        Forward accepts "units"... 
        Currently has a program to "draw" a circle, 
        Repeat 18[Forward 2 Right 20]. Gets close...
        Forward sometimes doesnt execute
 03/29 - Parse Tree implemented and working. 
         Temporarily disabled ping stop motion
 04/20 - Added Ping and IR sensors to safety check
         for movement controls. pingCheck() and
         irCheck().
 04/24 - Bundled saftey check functions into one
         generic function call. Sensor data bundled
         into ROVERSTATE struct to be transmitted to
         app.
 05/01 - Using Timer2 to send data packet of sensor
         values(sensorSend()) regardless of roverstate
 */
// ***************************************
// Arduino Sketch Main Loop
// ***************************************
void loop()
{
  static unsigned int d = 0;
  delay(10);
  d += 1;
  if (d >= 25)
  {
    sensorCheck();
    sensorSend();
    d = 0;
  }
  //delay(250);
  
  if(Serial.available())
  {
    TEXTNODE *list = serialReadCommands();
    if (Serial.peek() == '\7')
    {
      destroyTextList(list);
      Serial.print("\nDumping Tree...\n");
    }
    else
    {
      TNODE *tree = makeFlatTree(list);
      tree = makeParseTree(tree);
      printTree(tree, 0);
      Serial.print("\nExecuting Tree...\n");
      LZFire(0);
      execTree(tree);
      destroyTree(tree);
      //temporary hack: dump everything in the buffer...
    }
    while (Serial.available() > 0)
      if (Serial.read() == '\7')
        break;
  }
}


