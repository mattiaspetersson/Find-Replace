// Find & Replace for live electronics ensemble.
// Mattias Petersson, (2019 - 2022).

FindReplaceScore {
	var numPlayers;
	var window, starView, functionView, faderView, infoTextView, fade;
	var width, height;
	var counter;

	*new {|numPlayers = 9|
		^super.newCopyArgs(numPlayers).init;
	}

	init {
		width = Window.screenBounds.width;
		height = Window.screenBounds.height;
		counter = 0;

		window = Window.new("Find & Replace", Rect(
			(width / 2) - (height / 2),
			0, //(height / 2),
			height,
			height
		), false, false).background_(Color.white);

		starView = FindReplaceStarView(
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
					var n, prevFunc;
					infoTextView.string_("");
					faderView.background_(Color(alpha: 0.0));
					functionView.redrawWithNewFunctions(functionView.functions);
					n = starView.spin;
					prevFunc = functionView.functions[n];
					counter = (counter + 1) % (numPlayers * 2);

					fade = {
						var num = rrand(1500, 3000);
						num.do{|i|
							faderView.background_(Color(alpha: i / num));
							(1/60).wait;
						};
						// instead: only numbers on the wheel. Show F or R in the middle (with increasing probability for each spin)
						functionView.functions[n] = if(((counter) / (numPlayers * 2)).coin)
						{"R"} {prevFunc};
						window.refresh;
						fade = nil;
					}.fork(AppClock);
				});
			};

			case {unicode == 27} {this.reset};
		};
	}

	reset {
		if(fade.notNil) {fade.stop; fade = nil};
		if(starView.notNil) {starView.snurr.stop; starView.snurr = nil};
		window.close;
		this.init;
	}
}