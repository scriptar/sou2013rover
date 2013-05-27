/*
  Melody
 
 Plays a melody 
 
 circuit:
 * 8-ohm speaker on digital pin 8, or buzzer
 
 created 21 Jan 2010
 modified 30 Aug 2011
 by Tom Igoe 

This example code is in the public domain.
 
 http://arduino.cc/en/Tutorial/Tone
 
 */
 #include "pitches.h"

// notes in the melody:
//int melody[] = {
//  NOTE_C6, NOTE_G5,NOTE_G5, NOTE_A5, NOTE_G5,0, NOTE_B5, NOTE_C6};

// note durations: 4 = quarter note, 8 = eighth note, etc.:
//int noteDurations[] = {
//  4, 8, 8, 4,4,4,4,4 };

void setup() {
//  for (int thisNote = 0; thisNote < 8; thisNote++) { // 50 was number of notes in melody
    // to calculate the note duration, take one second 
    // divided by the note type.
    //e.g. quarter note = 1000 / 4, eighth note = 1000/8, etc.
    int noteDuration = 1000/4;
    tone(6, NOTE_C6, noteDuration); //tone(BuzzerPin, Note, Duration), or something... // iterate over the notes of the melody:
    // to distinguish the notes, set a minimum time between them.
    // the note's duration + 30% seems to work well:
    int pauseBetweenNotes = 150; // formally, 4 was noteDurations[thisNote]
    delay(pauseBetweenNotes);
    // stop the tone playing:
    noTone(6);
//  }

//  for (int thisNote = 0; thisNote < 8; thisNote++) {
    noteDuration = 1000/4;
    tone(6, NOTE_C4, noteDuration);
    pauseBetweenNotes = noteDuration * 1.30;
    delay(pauseBetweenNotes);
    noTone(6);
//  }
}
void loop() {
   }
