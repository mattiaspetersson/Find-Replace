FindReplaceStarView {
	var <parent, <bounds, <numBeams;
	var <view, drawStar;
	var <outerPoints, <innerPoints, <outerRadius, <innerRadius;
	var <angle, halfAngle, polar;
	var <>snurr, spinArray;

	*new {|parent, bounds, numBeams = 8|
		^super.newCopyArgs(parent, bounds, numBeams).initStar;
	}

	initStar {
		spinArray = Array.series(numBeams);
		view = UserView(parent, bounds).background_(Color.clear);
		outerRadius = view.bounds.width / 2;
		innerRadius = view.bounds.width / 6;
		angle = 2pi / numBeams;
		halfAngle = angle / 2;

		drawStar = {
			var x = 0, y = 0;

			outerPoints = [];
			innerPoints = [];

			numBeams.do{|i|
				var a, sx, sy;
				a = i * angle;
				sx = x + cos(a) * outerRadius;
				sy = y + sin(a) * outerRadius;
				outerPoints = outerPoints.add(Point(sx, sy));

				sx = x + cos(a+halfAngle) * innerRadius;
				sy = y + sin(a+halfAngle) * innerRadius;
				innerPoints = innerPoints.add(Point(sx, sy));
			};

			Pen.fillColor_(Color.black);
			Pen.moveTo(outerPoints[0]);

			numBeams.do{|i|
				Pen.lineTo(innerPoints[i]);
				Pen.lineTo(outerPoints[(i+1)%numBeams]);
			};

			Pen.translate(view.bounds.width / 2, view.bounds.height / 2); // move to the center
			Pen.rotate(-0.5pi);
			Pen.fill;

			// outline beam 0
			Pen.moveTo(innerPoints.reverse[0]);
			Pen.width_(3);

			numBeams.do{|i|
				if(i==0) {
					Pen.strokeColor_(Color.red);
					Pen.lineTo(outerPoints[i]);
					Pen.lineTo(innerPoints[i]);
					Pen.stroke;
				};
			};

			Pen.translate(view.bounds.width / 2, view.bounds.height / 2); // move to the center
			Pen.rotate(-0.5pi);
		};

		view.drawFunc = { drawStar.() };
	}

	spin { // spins the star and returns the index for where it stopped
		//var stopAt = numBeams.rand;
		var stopAt = this.urn;

		var numLaps = 1 + (numBeams.reciprocal * stopAt);
		var env = Env([1/360, 1/60], numLaps * 360, -4);

		if(snurr.isNil, {
			snurr = {
				(numLaps * 360).do{|i|
					view.refresh;
					view.drawFunc = {
						Pen.rotate((((i+1) % 360) / 360) * 2pi, (view.bounds.width / 2), view.bounds.height / 2);
						drawStar.()
					};
					(env[i] * 0.05).wait;
					this.changed(\spin, (i/360)*numBeams);
				};
				snurr = nil;
				this.changed(\spinDone);
			}.fork(AppClock);
		});
		^stopAt;
	}

	urn {
		var n;
		n = spinArray.choose ?? {spinArray = Array.series(numBeams); n = spinArray.choose}; //? numBeams.rand;
		spinArray.remove(n);
		^n;
	}
}