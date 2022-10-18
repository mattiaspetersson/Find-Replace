FindReplace {
	var arrayOfPlayerInputs, numSpeakers;
	var server, <synths, ensembleBuffers, synthGroup;

	*new {|arrayOfPlayerInputs = 0, numSpeakers = 16|
		^super.newCopyArgs(arrayOfPlayerInputs, numSpeakers).init;
	}

	init {
		server = Server.default;
		synths = [];
		ensembleBuffers = [];
		server.waitForBoot{
			synthGroup = Group(server);
			arrayOfPlayerInputs.do{|n|
				this.buildSynth(n);
				server.sync;
				ensembleBuffers = ensembleBuffers.add(EnsembleBuffer(server, 3, 1, n));
			}
		};
	}

	buildSynth {|chan = 0|
		fork{
			SynthDef(\findReplaceDelay ++ chan, {|delTime = 0.5|
				var input, del, newArray = 0!numSpeakers;
				input = SoundIn.ar(chan);
				//input = Impulse.ar(0);
				del = DelayL.ar(input, 1, delTime);
				del = [0, del] ++ (numSpeakers - 2).collect{|i|
					del = DelayL.ar(del, 1, delTime);
				};
				// rearrange the delay array according to the players position
				del.do{|d, i|
					newArray[i] = del[(i-chan)%numSpeakers]
				};
				Out.ar(0, newArray);
			}).add;
			server.sync;
			synths = synths.add(Synth(\findReplaceDelay ++ chan, nil, synthGroup));
		};
	}

	setDelayTime {|delTime| // sets the delay time for all delays of one player.
		if(delTime.isArray, {
			delTime.do{|d, i|
				synths[i].set(\delTime, d.postln);
			};
		}, {
			synthGroup.set(\delTime, delTime);
		})
	}
}