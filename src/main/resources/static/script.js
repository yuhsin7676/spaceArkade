var url = window.location.href;
var messages = document.getElementById("messages");
var playButton = document.getElementById("playButton");
//var leftButton = document.getElementById("leftButton");
//var rightButton = document.getElementById("rightButton");

///////////////////////////////////

class Graphic{
    
    canvas = document.getElementById("canvas");
    ctx = canvas.getContext('2d');
    imageTile1;
    imageTile2;
    imageTile3;
    imagePlatform;
    turnOver = false;
    
    constructor(){
        this.loadImages();
    }
    
    loadImages(){
        this.imageTile1 = new Image();
        this.imageTile1.src = "media/tile1.png";
        
        this.imageTile2 = new Image();
        this.imageTile2.src = "media/tile2.png";
        
        this.imageTile3 = new Image();
        this.imageTile3.src = "media/tile3.png";
        
        this.imagePlatform = new Image();
        this.imagePlatform.src = "media/platform.png";
    }
    
    update(data){
        this.clear();
        for(var key in data.worldPointer.components){
            var obj = data.worldPointer.components[key]; 
            var locationX, locationY;
            
            if(this.turnOver){
                locationX = data.worldPointer.width - obj.location.x;
                locationY = data.worldPointer.height - obj.location.y;
            }
            else{
                locationX = obj.location.x;
                locationY = obj.location.y;
            }
            
            if(obj.isCircle){
                this.ctx.beginPath();
                if(obj.className == "Ball") this.ctx.fillStyle = "#fff";
                else if(obj.className == "Bonus1") this.ctx.fillStyle = "#0b0";
                else if(obj.className == "Bonus2") this.ctx.fillStyle = "#0f8";
                else if(obj.className == "Bonus3") this.ctx.fillStyle = "#08f";
                else if(obj.className == "Bonus4") this.ctx.fillStyle = "#80f";
                
                else if(obj.className == "AntiBonus1") this.ctx.fillStyle = "#b80";
                else if(obj.className == "AntiBonus2") this.ctx.fillStyle = "#b00";
                else if(obj.className == "AntiBonus3") this.ctx.fillStyle = "#f00";
                else if(obj.className == "AntiBonus4") this.ctx.fillStyle = "#f40";
                
                this.ctx.arc(locationX, locationY, obj.r, 0, Math.PI*2, true);
                this.ctx.stroke();
                this.ctx.fill();
            }
            else{
                var image;
                if(obj.className == "Tile1") image = this.imageTile1;
                else if(obj.className == "Tile2") image = this.imageTile2;
                else if(obj.className == "Tile3") image = this.imageTile3;
                else if(obj.className == "Platform") image = this.imagePlatform;
                
                this.ctx.drawImage(image, locationX - obj.size.x/2, locationY - obj.size.y/2, obj.size.x, obj.size.y);
                //this.ctx.fillRect(locationX - obj.size.x/2, locationY - obj.size.y/2, obj.size.x, obj.size.y);
            }
        }
    }
    
    clear(){
        this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
    }
    
}

class Inputs{
    
    _toLeft = 0;
    _toRight = 0;
    velocityX = 0;
    turnOver = false;
    
    constructor(){
        document.addEventListener('keydown', (event) => {
            if(event.code === "KeyA")
                this._toLeft = -1;
            else if(event.code === "KeyD")
                this._toRight = 1;
        });
        //leftButton.addEventListener('touchstart', () => {this._toLeft = -1;});
        //rightButton.addEventListener('touchstart', () => {this._toRight = 1;});

        document.addEventListener('keyup', (event) => {
            if(event.code === "KeyA")
                this._toLeft = 0;
            else if(event.code === "KeyD")
                this._toRight = 0;
        });
        //leftButton.addEventListener('touchend', () => {this._toLeft = 0;});
        //rightButton.addEventListener('touchend', () => {this._toRight = 0;});
    }
    
    update(){
        if(this.turnOver)
            this.velocityX = -400 * (this._toLeft + this._toRight);
        else
            this.velocityX = 400 * (this._toLeft + this._toRight);
    }
    
}

class Sounds{
    
    soundBreakTile1;
    soundBreakTile2;
    soundBreakTile3;
    soundDamageTile2;
    soundDamageTile3;
    soundImpactPlatform;
    soundImpactWorld;
    
    constructor(){
        this.loadSounds();
    }
    
    loadSounds(){
        this.soundBreakTile1 = new Audio();
        this.soundBreakTile1.src = "media/breakTile1.mp3";
        
        this.soundBreakTile2 = new Audio();
        this.soundBreakTile2.src = "media/breakTile2.mp3";
        
        this.soundBreakTile3 = new Audio();
        this.soundBreakTile3.src = "media/breakTile3.mp3";
        
        this.soundDamageTile2 = new Audio();
        this.soundDamageTile2.src = "media/damageTile2.mp3";
        
        this.soundDamageTile3 = new Audio();
        this.soundDamageTile3.src = "media/damageTile3.mp3";
        
        this.soundImpactPlatform = new Audio();
        this.soundImpactPlatform.src = "media/impactPlatform.mp3";
        
        this.soundImpactWorld = new Audio();
        this.soundImpactWorld.src = "media/impactWorld.mp3";
    }
    
    play(data){
        
        for(var i = 0; i < data.worldPointer.sounds.length; i++){
            
            var sound = data.worldPointer.sounds[i];
            var audio;
            if(sound == "breakTile1") audio = this.soundBreakTile1.cloneNode();
            else if(sound == "breakTile2") audio = this.soundBreakTile2.cloneNode();
            else if(sound == "breakTile3") audio = this.soundBreakTile3.cloneNode();
            else if(sound == "damageTile2") audio = this.soundDamageTile2.cloneNode();
            else if(sound == "damageTile3") audio = this.soundDamageTile3.cloneNode();
            else if(sound == "impactPlatform") audio = this.soundImpactPlatform.cloneNode();
            else if(sound == "impactWorld") audio = this.soundImpactWorld.cloneNode();
                audio.play();
        }
        
    }
    
}

///////////////////////////////////

var inputs = new Inputs();
var graphic = new Graphic();
var sounds = new Sounds();

//////////////// Работа с сокетом //////////////////

// Сюда надо написать ip-адрес
var socket = new WebSocket(url.replace("http://", "ws://") + "websocket");

socket.onmessage = function(event) {
    messages.innerHTML = "";
    playButton.disabled = true;
    
    switch(event.data){
        case "NOPLAY":
            graphic.clear();
            playButton.disabled = false;
            break;
        case "WAIT":
            graphic.clear();
            messages.innerHTML = "Ждем второго игрока!";
            break;
        case "LOSE":
            graphic.clear();
            playButton.disabled = false;
            messages.innerHTML = "Вы проиграли!";
            break;
        case "WIN":
            graphic.clear();
            playButton.disabled = false;
            messages.innerHTML = "Вы выиграли!";
            break;
        default:
            var data = JSON.parse(event.data);
            if(data.playerNumber == 1)
                graphic.turnOver = inputs.turnOver = true;
            else
                graphic.turnOver = inputs.turnOver = false;
            
            graphic.update(data);
            inputs.update();
            sounds.play(data);
            socket.send(JSON.stringify({x: inputs.velocityX, y: 0})); 
    }
};

socket.onclose = function(event) {
    console.log("[close] Соединение закрыто чисто, код: " + event.code + "; причина:" + event.reason);
};

socket.onerror = function(error) {
    console.error(error.message);
};