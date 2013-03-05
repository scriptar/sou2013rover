void setup(){
  Serial.begin(9600);
  
}

void loop(){
  int senVal = analogRead(A5);
  map(senVal,306, 315, 0, 1024);
  Serial.println(senVal);
  delay(50);
}

