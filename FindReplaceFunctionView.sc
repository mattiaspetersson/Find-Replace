FindReplaceFunctionView {
	var <parent, <bounds, <numPlayers;
	var <view, drawFunctionView;
	var <points, <radius, <angle;
	var <>functions;

	*new {|parent, bounds, numPlayers = 8|
		^super.newCopyArgs(parent, bounds, numPlayers).initFunctionView;
	}

	initFunctionView {
		view = UserView(parent, bounds).background_(Color.clear);
		radius = (view.bounds.width / 2) - 30;
		angle = 2pi / numPlayers;
		//functions = ["7", "11", "13", "F"];
		functions = numPlayers.collect{|i|
			["7", "11", "13", "F"][i%4];
		};

		drawFunctionView = {
			var x = 0, y = 0;

			points = [];

			numPlayers.do{|i|
				var a, sx, sy;
				a = i * angle;
				sx = x + cos(a) * radius;
				sy = y + sin(a) * radius;
				points = points.add(Point(sx, sy));
			};

			// rotate and center the ring
			points = points.collect{|p|
				p.rotate(-0.5pi).translate(Point(radius, radius));
			};

			Pen.strokeColor_(Color.gray);
			points.do{|p, i|
				this.functionView(
					view,
					Rect(0, 0, 60, 60),
					p.x,
					p.y,
					functions[i]
				);

				Pen.stroke;
			};
		};

		view.drawFunc = { drawFunctionView.() };
	}

	functionView {|parent, b, x, y, string, color|
		var fv = UserView(parent, b).background_(Color.white), textView;
		// white is a temporary hack here because i'm to stupid to figure out where to refresh...

		fv.drawFunc = {|view|
			Pen.strokeColor = Color.black;
			Pen.width = 6;
			Pen.addOval(Rect(3, 3, view.bounds.width - 6, view.bounds.height - 6));
			Pen.stroke;
		};
		textView = StaticText(fv, fv.bounds)
		.string_(string)
		.font_(Font("Monaco", 36, false))
		.stringColor_(Color.red)
		.align_(\center);

		fv.moveTo(x, y);

		^fv;
	}

	redrawWithNewFunctions {|arrayOfFunctions|
		functions = arrayOfFunctions;
		view.drawFunc = { drawFunctionView.() };
	}
}