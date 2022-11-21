// Find & Replace for live electronics ensemble.
// Mattias Petersson, (2019 - 2022).

FindReplaceScore {
	var numBeams;
	var window, starView, functionView, faderView, <centerView, infoTextView, fade;
	var width, height;
	var counter, frCounter, currentCenterViewString, currentSpinPos;

	*new {|numBeams = 9|
		^super.newCopyArgs(numBeams).init;
	}

	init {
		width = Window.screenBounds.width;
		height = Window.screenBounds.height;
		counter = 0;
		frCounter = -1;

		window = Window.new("Find & Replace", Rect(
			(width / 2) - (height / 2),
			0,
			height,
			height
		), false, false).background_(Color.white);

		starView = FindReplaceStarView(
			window,
			Rect(125, 145, window.bounds.width - 250, window.bounds.height - 250),
			numBeams
		);

		starView.addDependant(this);

		centerView = StaticText(starView.view, Rect((starView.bounds.width / 2) - 200, (starView.bounds.height / 2) - 100, 400, 200))
		.string_("")
		.stringColor_(Color.red)
		.align_(\center)
		.font_(Font("Monaco", 108));

		functionView = FindReplaceFunctionView(
			window,
			Rect(66, 86, window.bounds.width - 130, window.bounds.height - 130),
			numBeams
		);

		faderView = View(window, Rect(0, 0, height, height)).background_(Color(alpha: 1.0));

		infoTextView = StaticText(faderView, Rect((faderView.bounds.width / 2) - 150, (faderView.bounds.height / 2) - 50, 300, 100))
		.string_("Press space to start")
		.stringColor_(Color.red)
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
					currentSpinPos = starView.spin;
					fade = {
						var num = rrand(900, 2700); // 1500, 3000
						num.do{|i|
							faderView.background_(Color(alpha: i / num));
							(1/60).wait;
						};
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

	update {|theChanger, par, val|
		switch(par,
			\spin, {
				currentSpinPos = (val.round) % functionView.functions.size;
				currentCenterViewString = functionView.functions[currentSpinPos];
				centerView.string = currentCenterViewString;
			},

			\spinDone, {
				var prob;
				counter = (counter + 1);
				prob = (counter / numBeams).clip(0.18, 0.72);
				prob.postln;

				if(counter < 18) {
					if(prob.coin)
					{ // if true, choose F(ind) or R(eplace)
						var frProb;
						frCounter = (frCounter + 1).clip(0, numBeams);
						frProb = frCounter / (numBeams);
						currentCenterViewString = ["F", "R"].wchoose([1 - frProb, frProb * (counter * 0.5)].normalizeSum);
						centerView.string = currentCenterViewString;
						if(currentCenterViewString == "R") {functionView.functions[currentSpinPos] = ""};
					}
					{centerView.string = currentCenterViewString};
				} {
					if(fade.notNil) {fade.stop};
					centerView.string = "R";
					fade = {
						var num = 2700;
						num.do{|i|
							faderView.background_(Color(alpha: i / num));
							(1/60).wait;
						};
						infoTextView.string_("You have reach the end. Press escape to reset.");
						window.refresh;
						fade = nil;
					}.fork(AppClock);
				};
			}
		)
	}
}