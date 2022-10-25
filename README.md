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
Run this line (adjust for the number of players):

```f = FindReplaceScore(10);```

- Start the piece in sync by playing a short, sound togehter and press space to spin the 
star to randomize a number (or F).
- Play in sync with the delay and use the number to create a pattern where you
play on the first and the last count.
- F means "Find", and indicates that you should find and articulate a resulting
pattern that you hear in that moment.
- The wheel will gradually be filled by R:s. This means that you should replace
the percussive, rhythmic patterns with continuous sounds with softer envelopes.
- The piece ends when all rhythm has been replaced by a drone.
