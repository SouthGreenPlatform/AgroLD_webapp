var rdm = false; // Indique si écran déja redimenssionné

		var dots = {
			nb: 0,
			distance: 0,
			d_radius: 400,
			array: []
		};
var canvas, ctx;
var canvasDots = function(nbDots,dist) {
		colorDot = 'grey',
		color = 'grey';
		canvas.width = window.innerWidth;
		canvas.height = window.innerHeight;
		canvas.style.display = 'block';
		ctx.fillStyle = colorDot;
		ctx.lineWidth = .3;
		ctx.strokeStyle = color;
		
		dots.nb = nbDots;
		dots.distance = dist;


		var mousePosition = {
			x: 30 * canvas.width / 100,
			y: 30 * canvas.height / 100
		};


		function Dot(){
			this.x = Math.random() * canvas.width;
			this.y = Math.random() * canvas.height;

			this.vx = -.5 + Math.random();
			this.vy = -.5 + Math.random();

			this.radius = Math.random();
		}

		Dot.prototype = {
			create: function(){
				ctx.beginPath();
				ctx.arc(this.x, this.y, 0, 0, Math.PI * 2, false);
				ctx.fill();
				//ctx.fillRect(10,10,1,1); // fill in the pixel at (10,10)
			},

			animate: function(){
				for(i = 0; i < dots.nb; i++){

					var dot = dots.array[i];

					if(dot.y < 0 || dot.y > canvas.height){
						dot.vx = dot.vx;
						dot.vy = - dot.vy;
					}
					else if(dot.x < 0 || dot.x > canvas.width){
						dot.vx = - dot.vx;
						dot.vy = dot.vy;
					}
					dot.x += dot.vx*0.8;
					dot.y += dot.vy*0.8;
				}
			},

			line: function(){
				for(i = 0; i < dots.nb; i++){
					for(j = 0; j < dots.nb; j++){
						i_dot = dots.array[i];
						j_dot = dots.array[j];

						if((i_dot.x - j_dot.x) < dots.distance && (i_dot.y - j_dot.y) < dots.distance && (i_dot.x - j_dot.x) > - dots.distance && (i_dot.y - j_dot.y) > - dots.distance){
							/*if((i_dot.x - mousePosition.x) < dots.d_radius && (i_dot.y - mousePosition.y) < dots.d_radius && (i_dot.x - mousePosition.x) > - dots.d_radius && (i_dot.y - mousePosition.y) > - dots.d_radius){*/
							if(1){
								ctx.beginPath();
								ctx.moveTo(i_dot.x, i_dot.y);
								ctx.lineTo(j_dot.x, j_dot.y);
								ctx.stroke();
								ctx.closePath();
							}
						}
					}
				}
			}
		};

		function createDots(){
			ctx.clearRect(0, 0, canvas.width, canvas.height);
			for(i = 0; i < dots.nb; i++){
				dots.array.push(new Dot());
				dot = dots.array[i];
				dot.create();
				//dots.array.splice(i,1);
			}

			dot.line();
			dot.animate();
		}

		window.onmousemove = function(parameter) {
			mousePosition.x = parameter.pageX;
			mousePosition.y = parameter.pageY;
		}

		mousePosition.x = window.innerWidth / 2;
		mousePosition.y = window.innerHeight / 2;


		setInterval(createDots, 1000/30);
	};
	function init(){
		automata = true;
		timeOutID = 0; // L'id du timout à supprimer à chaque utilisation
		console.log('init launched');
		var W = window.innerWidth;
		if(W > 450){
			if(W < 500)
				canvasDots(25,350);
			else if(W < 750)
				canvasDots(35,325);
			else if(W < 950)
				canvasDots(40,300);
			else if(W < 1050)
				canvasDots(45,275);
			else if(W < 1200)
				canvasDots(45,250);
			else
				canvasDots(50,225);
		}
		else
			console.log("écran trop petit pour l'affichage du canvas");
		
	}
	function cleanEntry(){
		for(var i = 0; i< dots.array.length ; i ++){
			dots.array.splice(i,1);
			if(i<300) break;
		}	
	}
	var stackResize = 0; // La pile d'évenements 
	function setStopTimeOut(){
		window.clearTimeout(timeOutID);
		console.log('TimouteId is cleared')
	}
	function cleanCanvas(){
		ctx.clearRect(0, 0, canvas.width, canvas.height);
		dots.array = [];
		console.log('--Clear Canvas');
		init();
	}
	function triggerResizingScreen(){
		timeOutID = window.setTimeout(function(){
			cleanCanvas();
			setStopTimeOut();
			},1500);
		console.log('---- resized GOOD ----');
	}
	function checkOverFlow(){
		console.log('Interval refresh');
		cleanCanvas();
	}
	window.onload = function() {
		canvas = document.querySelector('canvas');
		ctx = canvas.getContext('2d');
		init();
		window.setInterval(checkOverFlow,30000);
		$(window).resize(function(e){
			if(automata){
				automata = false;
				triggerResizingScreen();
			}
			e.stopPropagation();

			// init();
			stackResize++;
			console.log('resized CALL ' + stackResize);

		});
	};