# Find & Replace for live electronics ensemble
 Mattias Petersson, (2019) 2022.
 
 This piece was composed for the Royal Live Electronics ensemble at The Royal College of Music in Stockholm. 
 It is an excercise in rhythm where a shared groove is established by individual patterns, played by the ensemble members,
 fed through a multichannel multi-tap delay.

## Instructions

### Preparations
- The players should be positioned in a circle, surrounding the audience
and be connected to one mono speaker each.
- A copy of each player's signals should be sent to a separate computer,
on a discrete channel, feeding the FindReplaceDelay. The delay should be
sent back to the circle of speakers in a one-to-one fashion.
- Any sound produced by any player should bounce around clockwise a full circle,
starting in the speaker immediately to the left of the player.

### Playing
Run this line (the first argument allows for changing the number of beams in the star):

```f = FindReplaceScore();```

- Start the piece in sync by simultaneously playing a short, sound together and pressing space. 
- The star spins and randomizes a number or a function.
- Play in sync with the delay and use the numbers to count the beats and create a pattern where you play on the first and the last count.
- The gradual fade to black indicates a diminuendo.
- F means "Find", and means that you should find and play a rhythmic pattern in the currently shared texture. The found pattern should be repeated and be as many beats long as the star indicates. If it points to a blank circle – improvise!
- R means "Replace". This entails replacing the percussive, rhythmic patterns with continuous sounds with softer envelopes. Repeat the sound after the number of beats indicated by the star. Blank circle indicates that the sound should be continuous for the whole period.
- After 18 spins the piece ends with an R and a slow fade.
