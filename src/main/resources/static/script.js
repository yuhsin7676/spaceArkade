var url = window.location.href;
var messages = document.getElementById("messages");
var playButton = document.getElementById("playButton");
//var leftButton = document.getElementById("leftButton");
//var rightButton = document.getElementById("rightButton");

///////////////////////////////////

class Graphic{
    
    canvas = document.getElementById("canvas");
    ctx = canvas.getContext('2d');
    turnOver = false;
    
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
                if(obj.className == "Tile1") this.ctx.fillStyle = "#080";
                else if(obj.className == "Tile2") this.ctx.fillStyle = "#cc0";
                else if(obj.className == "Tile3") this.ctx.fillStyle = "#f80";
                else if(obj.className == "Platform") this.ctx.fillStyle = "#000";
                
                this.ctx.fillRect(locationX - obj.size.x/2, locationY - obj.size.y/2, obj.size.x, obj.size.y);
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

///////////////////////////////////

var inputs = new Inputs();
var graphic = new Graphic();

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
            socket.send(JSON.stringify({x: inputs.velocityX, y: 0})); 
    }
};

socket.onclose = function(event) {
    console.log("[close] Соединение закрыто чисто, код: " + event.code + "; причина:" + event.reason);
};

socket.onerror = function(error) {
    console.error(error.message);
};