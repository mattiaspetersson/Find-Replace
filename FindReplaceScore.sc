// Find & Replace for live electronics ensemble.
// Mattias Petersson, (2019) 2022.

/*
Instructions

Preparations
• The players should be positioned in a circle, surrounding the audience
and be connected to a mono speaker.
• A copy of each player's signals should be sent to a separate computer,
on a discrete channel, feeding the FindReplaceDelay. The delay should be
sent back to the circle of speakers in a one-to-one fashion.
• Any sound produced by any player should bounce around clockwise a full circle,
starting in the speaker immediately to the left of the player.

Playing
• Run this line (adjust for the number of players):
f = FindReplaceScore(10);

• Start the piece in sync by playing a short, sound togehter and press space to
randomize a number.
• Play in sync with the delay and use the number to create a pattern where you
play on the first and the last count.
• F means "Find", and indicates that you should find and articulate a resulting
pattern that you hear in that moment.
• The wheel will gradually be filled by R:s. This means that you should replace
the percussive, rhythmic patterns with continuous sounds with softer envelopes.
• The piece ends when all rhythm has been replaced by a drone.
*/

FindReplaceScore {
	var numPlayers;
	var window, starView, functionView, faderView, infoTextView, fade;
	var width, height;

	*new {|numPlayers = 9|
		^super.newCopyArgs(numPlayers).init;
	}

	init {
		width = Window.screenBounds.width;
		height = Window.screenBounds.height;

		window = Window.new("Find & Replace", Rect(
			(width / 2) - (height / 2),
			(height / 2),
			height,
			height
		), false, false).background_(Color.white);

		starView = StarView(
			window,
			Rect(125, 145, window.bounds.width - 250, window.bounds.height - 250),
			numPlayers
		);
		functionView = FindReplaceFunctionView(
			window,
			Rect(66, 86, window.bounds.width - 130, window.bounds.height - 130),
			numPlayers
		);
		faderView = View(window, Rect(0, 0, width, height)).background_(Color(alpha: 1.0));
		infoTextView = StaticText(faderView, Rect(0, 0, width, height))
		.string_("Press space to start")
		.align_(\center)
		.font_(Font("Monaco", 36));

		window.refresh;
		window.front;

		window.view.keyUpAction = {|view, char, mod, unicode, keycode, key|
			case {unicode == 32} {
				if(fade.isNil, {
					var n;
					infoTextView.string_("");
					faderView.background_(Color(alpha: 0.0));
					functionView.redrawWithNewFunctions(functionView.functions);
					n = starView.spin;
					fade = {
						var num = rrand(1500, 3000);
						num.do{|i|
							faderView.background_(Color(alpha: i / num));
							(1/60).wait;
						};
						// instead: only numbers on the wheel. Show F or R in the middle (w. increasing probability for each spin)
						functionView.functions[n] = "R";
						window.refresh;
						fade = nil;
					}.fork(AppClock);
				});
			};
		};
	}
}